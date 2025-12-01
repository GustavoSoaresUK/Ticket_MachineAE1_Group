/**
 * Admin class for managing ticket machine operations
 * Assigned to: Group Member B
 */

class Admin {

    /**
     * Views all destinations in the ticket machine
     */
    fun viewAllDestinations(ticketMachine: TicketMachine) {
        val destinations = ticketMachine.getAllDestinations()

        if (destinations.isEmpty()) {
            println("\n No destinations available\n")
            return
        }

        println("\n========================================")
        println("        ALL DESTINATIONS")
        println("========================================")
        println()
        println("%-20s %-15s %-15s %-15s".format(
            "Destination", "Single", "Return", "Total Sales"
        ))
        println("-".repeat(65))

        for (dest in destinations) {
            println("%-20s �%-14.2f �%-14.2f �%-14.2f".format(
                dest.name,
                dest.singlePrice,
                dest.returnPrice,
                dest.totalTakings
            ))
        }

        println("-".repeat(65))
        println("Total Destinations: ${destinations.size}")
        println("========================================\n")
    }

    /**
     * Adds a new destination to the ticket machine
     */
    fun addDestination(
        ticketMachine: TicketMachine,
        name: String,
        singlePrice: Double,
        returnPrice: Double
    ) {
        if (name.isBlank()) {
            println(" Destination name cannot be empty")
            return
        }

        if (singlePrice <= 0 || returnPrice <= 0) {
            println(" Prices must be positive")
            return
        }

        val newDestination = Destination(name, singlePrice, returnPrice)
        ticketMachine.addDestination(newDestination)
        println(" Destination '$name' added successfully")
        println("  Single: �${"%.2f".format(singlePrice)}")
        println("  Return: �${"%.2f".format(returnPrice)}")
    }

    /**
     * Updates prices for a specific destination
     */
    fun updateDestination(
        ticketMachine: TicketMachine,
        name: String,
        singlePrice: Double,
        returnPrice: Double
    ) {
        val destinations = ticketMachine.getAllDestinations()
        val destination = destinations.find { it.name.equals(name, ignoreCase = true) }

        if (destination == null) {
            println(" Destination '$name' not found")
            return
        }

        if (singlePrice <= 0 || returnPrice <= 0) {
            println(" Prices must be positive")
            return
        }

        destination.updatePrices(singlePrice, returnPrice)
        println(" Destination '$name' updated successfully")
        println("  New Single: �${"%.2f".format(singlePrice)}")
        println("  New Return: �${"%.2f".format(returnPrice)}")
    }

    /**
     * Adjusts all destination prices by a factor
     * @param factor multiplier (e.g., 1.1 for 10% increase, 0.9 for 10% decrease)
     */
    fun adjustAllPrices(ticketMachine: TicketMachine, factor: Double) {
        if (factor <= 0) {
            println(" Factor must be positive")
            return
        }

        val destinations = ticketMachine.getAllDestinations()

        if (destinations.isEmpty()) {
            println(" No destinations to update")
            return
        }

        println("\n--- Adjusting All Prices ---")
        println("Factor: $factor")

        val percentChange = ((factor - 1.0) * 100)
        val changeText = if (percentChange > 0) {
            "+${"%.1f".format(percentChange)}% increase"
        } else {
            "${"%.1f".format(percentChange)}% decrease"
        }
        println("Change: $changeText\n")

        for (dest in destinations) {
            val oldSingle = dest.singlePrice
            val oldReturn = dest.returnPrice
            val newSingle = oldSingle * factor
            val newReturn = oldReturn * factor

            dest.updatePrices(newSingle, newReturn)

            println("${dest.name}:")
            println("  Single: �${"%.2f".format(oldSingle)} � �${"%.2f".format(newSingle)}")
            println("  Return: �${"%.2f".format(oldReturn)} � �${"%.2f".format(newReturn)}")
        }

        println("\n All prices adjusted successfully")
    }

    /**
     * Displays system summary including total sales
     */
    fun displaySystemSummary(ticketMachine: TicketMachine) {
        val destinations = ticketMachine.getAllDestinations()

        println("\nTPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPW")
        println("Q                    SYSTEM SUMMARY                         Q")
        println("ZPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP]")

        println("\nStation: ${ticketMachine.originStation}")
        println("Total Destinations: ${destinations.size}")

        val totalSales = destinations.sumOf { it.totalTakings }
        println("Total Revenue: �${"%.2f".format(totalSales)}")

        if (destinations.isNotEmpty()) {
            val avgSingle = destinations.map { it.singlePrice }.average()
            val avgReturn = destinations.map { it.returnPrice }.average()

            println("\nAverage Prices:")
            println("  Single: �${"%.2f".format(avgSingle)}")
            println("  Return: �${"%.2f".format(avgReturn)}")

            val topDestination = destinations.maxByOrNull { it.totalTakings }
            if (topDestination != null && topDestination.totalTakings > 0) {
                println("\nTop Destination:")
                println("  ${topDestination.name} - �${"%.2f".format(topDestination.totalTakings)} in sales")
            }
        }

        println("\nCurrent Balance: �${"%.2f".format(ticketMachine.getInsertedMoney())}")
        println()
    }
}