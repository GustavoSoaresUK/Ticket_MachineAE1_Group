
class Ticket(
    val origin: String,
    val destination: String,
    val price: Double,
    val ticketType: String  // "Single" or "Return"
) {

    fun printTicket() {
        println("***")
        println(origin)
        println("to")
        println(destination)
        println("Price: £${"%.2f".format(price)} $ticketType")
        println("***")
    }
}