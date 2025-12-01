class Admin {

    fun viewAllDestinations(ticketMachine: TicketMachine) {
        val destinations = ticketMachine.getAllDestinations()

        println("\n========================================")
        println("       ALL DESTINATIONS REPORT")
        println("========================================")
        println()
        println("%-20s %-12s %-12s %-10s %-15s".format(
            "Station Name",
            "Single (£)",
            "Return (£)",
            "Sales",
            "Takings (£)"
        ))
        println("-".repeat(75))

        var totalSales = 0
        var totalTakings = 0.0

        for (dest in destinations) {
            println("%-20s £%-11.2f £%-11.2f %-10d £%-14.2f".format(
                dest.name,
                dest.singlePrice,
                dest.returnPrice,
                dest.salesCount,
                dest.totalTakings
            ))
            totalSales += dest.salesCount
            totalTakings += dest.totalTakings
        }

        println("-".repeat(75))
        println("%-20s %-12s %-12s %-10d £%-14.2f".format(
            "TOTAL",
            "",
            "",
            totalSales,
            totalTakings
        ))
        println("========================================\n")
    }


    fun addDestination(
        ticketMachine: TicketMachine,
        name: String,
        singlePrice: Double,
        returnPrice: Double
    ): Boolean {
        // Validate inputs
        if (name.isBlank()) {
            println("Error: Destination name cannot be empty.")
            return false
        }

        if (singlePrice <= 0 || returnPrice <= 0) {
            println("Error: Prices must be positive values.")
            return false
        }

        if (returnPrice < singlePrice) {
            println("Warning: Return price is typically higher than single price.")
        }

        // Check if destination already exists
        val existingDestinations = ticketMachine.getAllDestinations()
        if (existingDestinations.any { it.name.equals(name, ignoreCase = true) }) {
            println("Error: Destination '$name' already exists.")
            return false
        }

        // Create and add new destination
        val newDestination = Destination(name, singlePrice, returnPrice)
        ticketMachine.addDestination(newDestination)

        println("✓ Destination '$name' added successfully!")
        println("  Single: £${"%.2f".format(singlePrice)}")
        println("  Return: £${"%.2f".format(returnPrice)}")

        return true
    }


    fun updateDestination(
        ticketMachine: TicketMachine,
        destinationName: String,
        newSinglePrice: Double,
        newReturnPrice: Double
    ): Boolean {
        // Validate inputs
        if (newSinglePrice <= 0 || newReturnPrice <= 0) {
            println("Error: Prices must be positive values.")
            return false
        }

        // Find the destination
        val destinations = ticketMachine.getAllDestinations()
        val destination = destinations.find { it.name.equals(destinationName, ignoreCase = true) }

        if (destination == null) {
            println("Error: Destination '$destinationName' not found.")
            return false
        }

        // Store old prices for confirmation message
        val oldSingle = destination.singlePrice
        val oldReturn = destination.returnPrice

        // Update prices
        destination.singlePrice = newSinglePrice
        destination.returnPrice = newReturnPrice

        println("✓ Destination '$destinationName' updated successfully!")
        println("  Single: £${"%.2f".format(oldSingle)} → £${"%.2f".format(newSinglePrice)}")
        println("  Return: £${"%.2f".format(oldReturn)} → £${"%.2f".format(newReturnPrice)}")

        return true
    }


    fun adjustAllPrices(ticketMachine: TicketMachine, factor: Double): Boolean {
        // Validate factor
        if (factor <= 0) {
            println("Error: Factor must be a positive number.")
            return false
        }

        if (factor == 1.0) {
            println("Warning: Factor of 1.0 means no change in prices.")
            return false
        }

        val destinations = ticketMachine.getAllDestinations()

        if (destinations.isEmpty()) {
            println("Error: No destinations to update.")
            return false
        }

        // Calculate percentage change for display
        val percentChange = (factor - 1.0) * 100
        val changeDirection = if (percentChange > 0) "increase" else "decrease"

        println("\n========================================")
        println("  ADJUSTING ALL PRICES")
        println("  ${changeDirection.uppercase()}: ${"%.1f".format(kotlin.math.abs(percentChange))}%")
        println("========================================\n")

        // Update all destinations
        for (dest in destinations) {
            val oldSingle = dest.singlePrice
            val oldReturn = dest.returnPrice

            dest.singlePrice = String.format("%.2f", dest.singlePrice * factor).toDouble()
            dest.returnPrice = String.format("%.2f", dest.returnPrice * factor).toDouble()

            println("%-20s £%-8.2f → £%-8.2f | £%-8.2f → £%-8.2f".format(
                dest.name,
                oldSingle, dest.singlePrice,
                oldReturn, dest.returnPrice
            ))
        }

        println("\n✓ All prices adjusted successfully!\n")
        return true
    }


    fun displaySystemSummary(ticketMachine: TicketMachine) {
        val destinations = ticketMachine.getAllDestinations()

        val totalDestinations = destinations.size
        val totalSales = destinations.sumOf { it.salesCount }
        val totalRevenue = destinations.sumOf { it.totalTakings }
        val avgPriceSingle = if (destinations.isNotEmpty())
            destinations.map { it.singlePrice }.average() else 0.0
        val avgPriceReturn = if (destinations.isNotEmpty())
            destinations.map { it.returnPrice }.average() else 0.0

        // Find most popular destination
        val mostPopular = destinations.maxByOrNull { it.salesCount }

        println("\n========================================")
        println("       SYSTEM SUMMARY")
        println("========================================")
        println("Total Destinations:     $totalDestinations")
        println("Total Tickets Sold:     $totalSales")
        println("Total Revenue:          £${"%.2f".format(totalRevenue)}")
        println("Avg Single Price:       £${"%.2f".format(avgPriceSingle)}")
        println("Avg Return Price:       £${"%.2f".format(avgPriceReturn)}")

        if (mostPopular != null && mostPopular.salesCount > 0) {
            println("\nMost Popular Destination:")
            println("  ${mostPopular.name} (${mostPopular.salesCount} tickets sold)")
        }
        println("========================================\n")
    }
}
