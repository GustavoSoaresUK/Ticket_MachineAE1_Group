import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    // Initialize all system components
    val ticketMachine = TicketMachine("Oxford Station")
    val admin = Admin()
    val loginSystem = LoginSystem()
    val offerManager = OfferManager()

    // Add some sample special offers
    offerManager.addSpecialOffer(
        "Christmas Sale",
        "London",
        20.0,
        "2025-12-01",
        "2025-12-31"
    )

    offerManager.addSpecialOffer(
        "New Year Special",
        "Manchester",
        15.0,
        "2025-12-20",
        "2026-01-15"
    )

    println("╔═══════════════════════════════════════════════════════════╗")
    println("║                                                           ║")
    println("║       TRAIN TICKET VENDING MACHINE SYSTEM                ║")
    println("║              Integrated System v1.0                       ║")
    println("║                                                           ║")
    println("╚═══════════════════════════════════════════════════════════╝")
    println()

    var running = true

    while (running) {
        displayMainMenu()

        print("\nEnter your choice: ")
        val choice = scanner.nextLine().trim()

        when (choice) {
            "1" -> customerMode(scanner, ticketMachine, offerManager)
            "2" -> adminMode(scanner, ticketMachine, admin, loginSystem, offerManager)
            "3" -> viewSpecialOffers(offerManager)
            "0" -> {
                println("\n╔═══════════════════════════════════════════════════════════╗")
                println("║  Thank you for using Train Ticket Vending Machine!       ║")
                println("║                   Have a safe journey!                    ║")
                println("╚═══════════════════════════════════════════════════════════╝")
                running = false
            }
            else -> println("\n❌ Invalid choice. Please try again.")
        }
    }
}

fun displayMainMenu() {
    println("\n" + "=".repeat(63))
    println("                        MAIN MENU")
    println("=".repeat(63))
    println("  1. Customer - Buy Tickets")
    println("  2. Admin Panel - System Management")
    println("  3. View Special Offers")
    println("  0. Exit")
    println("=".repeat(63))
}

/**
 * Customer Mode - Member A's Functionality
 */
fun customerMode(scanner: Scanner, ticketMachine: TicketMachine, offerManager: OfferManager) {
    println("\n╔═══════════════════════════════════════════════════════════╗")
    println("║                    CUSTOMER MODE                          ║")
    println("╚═══════════════════════════════════════════════════════════╝")

    var inCustomerMode = true

    while (inCustomerMode) {
        println("\n--- Customer Menu ---")
        println("1. View Available Destinations")
        println("2. Search and Buy Ticket")
        println("3. Check Special Offers")
        println("0. Back to Main Menu")

        print("\nChoice: ")
        val choice = scanner.nextLine().trim()

        when (choice) {
            "1" -> ticketMachine.displayAvailableDestinations()
            "2" -> purchaseTicket(scanner, ticketMachine, offerManager)
            "3" -> offerManager.displayActiveOffers()
            "0" -> inCustomerMode = false
            else -> println("❌ Invalid choice.")
        }
    }
}

/**
 * Ticket Purchase Flow - Member A's Core Functionality
 */
fun purchaseTicket(scanner: Scanner, ticketMachine: TicketMachine, offerManager: OfferManager) {
    println("\n--- Purchase Ticket ---")

    ticketMachine.displayAvailableDestinations()

    print("\nEnter destination: ")
    val destination = scanner.nextLine().trim()

    print("Enter ticket type (Single/Return): ")
    val ticketType = scanner.nextLine().trim()

    val ticket = ticketMachine.searchTicket(destination, ticketType)

    if (ticket == null) {
        println("❌ Ticket not found. Please check destination name.")
        return
    }

    // Check for special offers (Member C's functionality integration)
    val bestOffer = offerManager.getBestOffer(destination, java.time.LocalDate.now().toString())
    var finalPrice = ticket.price

    if (bestOffer != null && bestOffer.isActive) {
        println("\n🎉 Special Offer Available!")
        println("   ${bestOffer.offerName} - ${bestOffer.discountPercentage}% OFF")
        val discount = bestOffer.getDiscountAmount(ticket.price)
        finalPrice = bestOffer.applyDiscount(ticket.price)
        println("   Original Price: £${"%.2f".format(ticket.price)}")
        println("   Discount: -£${"%.2f".format(discount)}")
        println("   Final Price: £${"%.2f".format(finalPrice)}")
    } else {
        println("\nTicket Price: £${"%.2f".format(finalPrice)}")
    }

    println("\nCurrent balance: £${"%.2f".format(ticketMachine.getInsertedMoney())}")
    print("Enter amount to insert (or 0 to cancel): £")
    val amountStr = scanner.nextLine().trim()

    try {
        val amount = amountStr.toDouble()
        if (amount == 0.0) {
            println("❌ Purchase cancelled.")
            return
        }

        ticketMachine.insertMoney(amount)

        val discountedTicket = if (bestOffer != null && bestOffer.isActive) {
            Ticket(ticket.origin, ticket.destination, finalPrice, ticket.ticketType)
        } else {
            ticket
        }

        val success = ticketMachine.buyTicket(discountedTicket)

        if (!success) {
            print("\nWould you like to add more money? (yes/no): ")
            val response = scanner.nextLine().trim().lowercase()
            if (response == "yes" || response == "y") {
                print("Enter additional amount: £")
                val additional = scanner.nextLine().trim().toDouble()
                ticketMachine.insertMoney(additional)
                ticketMachine.buyTicket(discountedTicket)
            } else {
                val refund = ticketMachine.returnChange()
                if (refund > 0) {
                    println("💰 Refunded: £${"%.2f".format(refund)}")
                }
            }
        }

    } catch (e: NumberFormatException) {
        println("❌ Invalid amount entered.")
    }
}

/**
 * Admin Mode - Member B's Functionality + Member C's Login
 */
fun adminMode(scanner: Scanner, ticketMachine: TicketMachine, admin: Admin,
              loginSystem: LoginSystem, offerManager: OfferManager) {

    println("\n╔═══════════════════════════════════════════════════════════╗")
    println("║                     ADMIN PANEL                           ║")
    println("║              Authentication Required                      ║")
    println("╚═══════════════════════════════════════════════════════════╝")

    print("\nUsername: ")
    val username = scanner.nextLine().trim()

    print("Password: ")
    val password = scanner.nextLine().trim()

    if (!loginSystem.login(username, password)) {
        println("❌ Login failed. Access denied.")
        return
    }

    if (!loginSystem.isAdminLoggedIn()) {
        println("❌ Admin privileges required. Access denied.")
        loginSystem.logout()
        return
    }

    println("✓ Login successful! Welcome, ${loginSystem.getCurrentUser()?.username}")

    var inAdminMode = true

    while (inAdminMode) {
        displayAdminMenu()

        print("\nChoice: ")
        val choice = scanner.nextLine().trim()

        when (choice) {
            "1" -> admin.viewAllDestinations(ticketMachine)
            "2" -> addNewDestination(scanner, ticketMachine, admin)
            "3" -> updateDestinationPrices(scanner, ticketMachine, admin)
            "4" -> adjustAllPrices(scanner, ticketMachine, admin)
            "5" -> admin.displaySystemSummary(ticketMachine)
            "6" -> manageSpecialOffers(scanner, offerManager)
            "0" -> {
                loginSystem.logout()
                println("✓ Logged out successfully.")
                inAdminMode = false
            }
            else -> println("❌ Invalid choice.")
        }
    }
}

fun displayAdminMenu() {
    println("\n" + "=".repeat(63))
    println("                      ADMIN MENU")
    println("=".repeat(63))
    println("  Destination Management:")
    println("    1. View All Destinations")
    println("    2. Add New Destination")
    println("    3. Update Destination Prices")
    println("    4. Adjust All Prices")
    println("    5. View System Summary")
    println()
    println("  Special Offers Management:")
    println("    6. Manage Special Offers")
    println()
    println("  0. Logout")
    println("=".repeat(63))
}

fun addNewDestination(scanner: Scanner, ticketMachine: TicketMachine, admin: Admin) {
    println("\n--- Add New Destination ---")

    print("Destination name: ")
    val name = scanner.nextLine().trim()

    print("Single ticket price: £")
    val singlePrice = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0

    print("Return ticket price: £")
    val returnPrice = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0

    admin.addDestination(ticketMachine, name, singlePrice, returnPrice)
}

fun updateDestinationPrices(scanner: Scanner, ticketMachine: TicketMachine, admin: Admin) {
    println("\n--- Update Destination Prices ---")

    admin.viewAllDestinations(ticketMachine)

    print("\nDestination to update: ")
    val name = scanner.nextLine().trim()

    print("New single ticket price: £")
    val singlePrice = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0

    print("New return ticket price: £")
    val returnPrice = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0

    admin.updateDestination(ticketMachine, name, singlePrice, returnPrice)
}

fun adjustAllPrices(scanner: Scanner, ticketMachine: TicketMachine, admin: Admin) {
    println("\n--- Adjust All Prices ---")
    println("Enter factor (e.g., 1.1 for 10% increase, 0.9 for 10% decrease)")

    print("Factor: ")
    val factor = scanner.nextLine().trim().toDoubleOrNull() ?: 1.0

    admin.adjustAllPrices(ticketMachine, factor)
}

fun manageSpecialOffers(scanner: Scanner, offerManager: OfferManager) {
    var inOfferMenu = true

    while (inOfferMenu) {
        println("\n--- Special Offers Management ---")
        println("1. View All Offers")
        println("2. Add New Offer")
        println("3. Search Offers")
        println("4. Delete Offer")
        println("5. Activate/Deactivate Offer")
        println("0. Back")

        print("\nChoice: ")
        val choice = scanner.nextLine().trim()

        when (choice) {
            "1" -> offerManager.displayAllOffers()
            "2" -> addNewOffer(scanner, offerManager)
            "3" -> searchOffers(scanner, offerManager)
            "4" -> deleteOffer(scanner, offerManager)
            "5" -> toggleOffer(scanner, offerManager)
            "0" -> inOfferMenu = false
            else -> println("❌ Invalid choice.")
        }
    }
}

fun addNewOffer(scanner: Scanner, offerManager: OfferManager) {
    println("\n--- Add New Special Offer ---")

    print("Offer name: ")
    val name = scanner.nextLine().trim()

    print("Station name: ")
    val station = scanner.nextLine().trim()

    print("Discount percentage: ")
    val discount = scanner.nextLine().trim().toDoubleOrNull() ?: 0.0

    print("Start date (YYYY-MM-DD): ")
    val startDate = scanner.nextLine().trim()

    print("End date (YYYY-MM-DD): ")
    val endDate = scanner.nextLine().trim()

    offerManager.addSpecialOffer(name, station, discount, startDate, endDate)
}

fun searchOffers(scanner: Scanner, offerManager: OfferManager) {
    println("\n--- Search Offers ---")
    print("Enter search term: ")
    val term = scanner.nextLine().trim()

    val results = offerManager.searchOffers(term)
    if (results.isNotEmpty()) {
        println("\nSearch Results:")
        results.forEach { it.displayInfo() }
    }
}

fun deleteOffer(scanner: Scanner, offerManager: OfferManager) {
    println("\n--- Delete Offer ---")
    offerManager.displayOffersSummary()

    print("\nEnter Offer ID to delete: ")
    val id = scanner.nextLine().trim().toIntOrNull() ?: 0

    offerManager.deleteOffer(id)
}

fun toggleOffer(scanner: Scanner, offerManager: OfferManager) {
    println("\n--- Activate/Deactivate Offer ---")
    offerManager.displayOffersSummary()

    print("\nEnter Offer ID: ")
    val id = scanner.nextLine().trim().toIntOrNull() ?: 0

    val offer = offerManager.getOfferById(id)
    if (offer != null) {
        if (offer.isActive) {
            offerManager.deactivateOffer(id)
        } else {
            offerManager.activateOffer(id)
        }
    }
}

fun viewSpecialOffers(offerManager: OfferManager) {
    println("\n╔═══════════════════════════════════════════════════════════╗")
    println("║                  CURRENT SPECIAL OFFERS                   ║")
    println("╚═══════════════════════════════════════════════════════════╝")

    offerManager.displayActiveOffers()
}