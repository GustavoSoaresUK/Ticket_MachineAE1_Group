
class TicketMachine(val originStation: String) {
    private val destinations: MutableList<Destination> = mutableListOf()
    private var insertedMoney: Double = 0.0

    init {
        // Hardcoded destinations as per requirements
        destinations.add(Destination("London", 25.50, 45.00))
        destinations.add(Destination("Manchester", 32.00, 58.00))
        destinations.add(Destination("Birmingham", 28.50, 52.00))
        destinations.add(Destination("Liverpool", 35.00, 63.00))
        destinations.add(Destination("Leeds", 30.00, 55.00))
        destinations.add(Destination("Edinburgh", 45.00, 85.00))
        destinations.add(Destination("Glasgow", 42.00, 78.00))
        destinations.add(Destination("Bristol", 22.50, 40.00))
    }


    fun searchTicket(destinationName: String, ticketType: String): Ticket? {
        val dest = destinations.find { it.name.equals(destinationName, ignoreCase = true) }

        return if (dest != null) {
            val price = dest.getPrice(ticketType)
            if (price > 0.0) {
                Ticket(originStation, dest.name, price, ticketType.capitalize())
            } else {
                null
            }
        } else {
            println("Destination not found.")
            null
        }
    }


    fun insertMoney(amount: Double) {
        if (amount > 0) {
            insertedMoney += amount
            println("Inserted: £${"%.2f".format(amount)}")
            println("Total inserted: £${"%.2f".format(insertedMoney)}")
        } else {
            println("Please insert a positive amount.")
        }
    }


    fun buyTicket(ticket: Ticket): Boolean {
        if (insertedMoney >= ticket.price) {
            // Print the ticket
            ticket.printTicket()

            // Record the sale
            val dest = destinations.find { it.name.equals(ticket.destination, ignoreCase = true) }
            dest?.recordSale(ticket.price)

            // Calculate and return change
            val change = insertedMoney - ticket.price
            insertedMoney = 0.0

            if (change > 0) {
                println("\nYour change: £${"%.2f".format(change)}")
            }

            println("\nThank you for your purchase!")
            return true
        } else {
            val shortfall = ticket.price - insertedMoney
            println("Insufficient funds. You need £${"%.2f".format(shortfall)} more.")
            return false
        }
    }


    fun getInsertedMoney(): Double {
        return insertedMoney
    }


    fun returnChange(): Double {
        val change = insertedMoney
        insertedMoney = 0.0
        if (change > 0) {
            println("Returning: £${"%.2f".format(change)}")
        }
        return change
    }


    fun displayAvailableDestinations() {
        println("\n=== Available Destinations from $originStation ===")
        println("%-20s %-15s %-15s".format("Destination", "Single", "Return"))
        println("-".repeat(50))
        for (dest in destinations) {
            println("%-20s £%-14.2f £%-14.2f".format(dest.name, dest.singlePrice, dest.returnPrice))
        }
        println()
    }


    fun getAllDestinations(): List<Destination> {
        return destinations
    }


    fun addDestination(destination: Destination) {
        destinations.add(destination)
        println("Destination '${destination.name}' added successfully.")
    }
}