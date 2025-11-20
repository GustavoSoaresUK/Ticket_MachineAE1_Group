class Destination(
    val name: String,
    var singlePrice: Double,
    var returnPrice: Double
) {
    var salesCount: Int = 0
        private set
    var totalTakings: Double = 0.0
        private set


    fun getPrice(ticketType: String): Double {
        return when (ticketType.lowercase()) {
            "single" -> singlePrice
            "return" -> returnPrice
            else -> {
                println("Invalid ticket type. Please choose 'Single' or 'Return'.")
                0.0
            }
        }
    }


    fun recordSale(price: Double) {
        salesCount++
        totalTakings += price
    }


    fun displayInfo() {
        println("Destination: $name")
        println("Single Price: £${"%.2f".format(singlePrice)}")
        println("Return Price: £${"%.2f".format(returnPrice)}")
        println("Total Sales: $salesCount")
        println("Total Takings: £${"%.2f".format(totalTakings)}")
    }
}