fun main() {
    println("===========================================")
    println("  MEMBER A (GEORGE) - TESTING")
    println("===========================================\n")

    // Test Destination class
    val dest = Destination("London", 25.50, 45.00)
    dest.displayInfo()

    // Test Ticket class
    val ticket = Ticket("Oxford", "London", 25.50, "Single")
    ticket.printTicket()

    // Test TicketMachine class
    val machine = TicketMachine("Oxford")
    machine.displayAvailableDestinations()

    // Demo buying a ticket
    val searchedTicket = machine.searchTicket("London", "Single")
    if (searchedTicket != null) {
        machine.insertMoney(30.00)
        machine.buyTicket(searchedTicket)
    }

    println("\n✓ Member A testing complete!")
}