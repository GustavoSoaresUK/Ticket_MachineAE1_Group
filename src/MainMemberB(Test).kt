fun main() {
    println("===========================================")
    println("  MEMBER B (GUSTAVO) - TESTING")
    println("===========================================\n")

    // Create ticket machine (Member A's class)
    val machine = TicketMachine("Oxford")

    // Test Admin class methods
    val admin = Admin()

    // Test viewing destinations
    admin.viewAllDestinations(machine)

    // Test adding destination
    admin.addDestination(machine, "Cardiff", 20.00, 36.00)

    // Test updating destination
    admin.updateDestination(machine, "Cardiff", 22.00, 40.00)

    // Test adjusting all prices
    admin.adjustAllPrices(machine, 1.1)

    println("\n✓ Member B testing complete!")
}