import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Represents a special promotional offer for a station
 * Assigned to: Group Member C (Marcin Kreza)
 */
class SpecialOffer(
    val offerId: Int,
    val offerName: String,
    val stationName: String,
    val discountPercentage: Double,
    val startDate: String,  // Format: "YYYY-MM-DD"
    val endDate: String     // Format: "YYYY-MM-DD"
) {
    var isActive: Boolean = true
        private set

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Deactivates this offer
     */
    fun deactivate() {
        isActive = false
        println("✓ Offer #$offerId '$offerName' has been deactivated")
    }

    /**
     * Activates this offer
     */
    fun activate() {
        isActive = true
        println("✓ Offer #$offerId '$offerName' has been activated")
    }

    /**
     * Checks if this offer is valid on a given date
     * @param date the date to check (format: "YYYY-MM-DD")
     * @return true if offer is valid on this date
     */
    fun isValidOnDate(date: String): Boolean {
        return try {
            val checkDate = LocalDate.parse(date, dateFormatter)
            val start = LocalDate.parse(startDate, dateFormatter)
            val end = LocalDate.parse(endDate, dateFormatter)

            isActive && !checkDate.isBefore(start) && !checkDate.isAfter(end)
        } catch (e: DateTimeParseException) {
            println("✗ Invalid date format. Use YYYY-MM-DD")
            false
        }
    }

    /**
     * Checks if offer is valid today
     */
    fun isValidToday(): Boolean {
        val today = LocalDate.now().format(dateFormatter)
        return isValidOnDate(today)
    }

    /**
     * Applies the discount to a price
     * @param originalPrice the original ticket price
     * @return the discounted price
     */
    fun applyDiscount(originalPrice: Double): Double {
        if (!isActive) {
            return originalPrice
        }
        val discount = originalPrice * (discountPercentage / 100.0)
        return String.format("%.2f", originalPrice - discount).toDouble()
    }

    /**
     * Gets the discount amount for a price
     * @param originalPrice the original price
     * @return the amount saved
     */
    fun getDiscountAmount(originalPrice: Double): Double {
        if (!isActive) {
            return 0.0
        }
        val discount = originalPrice * (discountPercentage / 100.0)
        return String.format("%.2f", discount).toDouble()
    }

    /**
     * Displays offer information to console
     */
    fun displayInfo() {
        val status = if (isActive) "ACTIVE" else "INACTIVE"
        val validToday = if (isValidToday()) "✓ Valid Today" else "✗ Not Valid Today"

        println("─".repeat(60))
        println("Offer ID:     #$offerId")
        println("Name:         $offerName")
        println("Station:      $stationName")
        println("Discount:     ${discountPercentage}%")
        println("Valid From:   $startDate")
        println("Valid Until:  $endDate")
        println("Status:       $status")
        println("Today:        $validToday")
        println("─".repeat(60))
    }

    /**
     * Gets a short summary of the offer
     */
    fun getSummary(): String {
        val status = if (isActive) "Active" else "Inactive"
        return "Offer #$offerId: $offerName - ${discountPercentage}% off $stationName ($startDate to $endDate) [$status]"
    }

    /**
     * Checks if the offer has expired
     */
    fun hasExpired(): Boolean {
        return try {
            val today = LocalDate.now()
            val end = LocalDate.parse(endDate, dateFormatter)
            today.isAfter(end)
        } catch (e: DateTimeParseException) {
            false
        }
    }

    /**
     * Gets days until offer expires
     */
    fun daysUntilExpiry(): Long? {
        return try {
            val today = LocalDate.now()
            val end = LocalDate.parse(endDate, dateFormatter)
            if (today.isBefore(end)) {
                java.time.temporal.ChronoUnit.DAYS.between(today, end)
            } else {
                null
            }
        } catch (e: DateTimeParseException) {
            null
        }
    }
}
