/**
 * Manages special offers for the ticket system
 * Assigned to: Group Member C (Marcin Kreza)
 */
class OfferManager {
    private val specialOffers: MutableList<SpecialOffer> = mutableListOf()
    private var nextOfferId: Int = 1

    /**
     * Adds a new special offer
     * @param offerName name of the offer
     * @param stationName station the offer applies to
     * @param discountPercentage discount percentage (e.g., 20.0 for 20%)
     * @param startDate start date (format: "YYYY-MM-DD")
     * @param endDate end date (format: "YYYY-MM-DD")
     * @return the created SpecialOffer
     */
    fun addSpecialOffer(
        offerName: String,
        stationName: String,
        discountPercentage: Double,
        startDate: String,
        endDate: String
    ): SpecialOffer? {
        // Validate inputs
        if (offerName.isBlank()) {
            println("✗ Offer name cannot be empty")
            return null
        }

        if (stationName.isBlank()) {
            println("✗ Station name cannot be empty")
            return null
        }

        if (discountPercentage <= 0 || discountPercentage > 100) {
            println("✗ Discount percentage must be between 0 and 100")
            return null
        }

        // Validate date format
        try {
            val start = java.time.LocalDate.parse(startDate, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val end = java.time.LocalDate.parse(endDate, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            if (end.isBefore(start)) {
                println("✗ End date must be after start date")
                return null
            }
        } catch (e: Exception) {
            println("✗ Invalid date format. Use YYYY-MM-DD")
            return null
        }

        // Create the offer
        val newOffer = SpecialOffer(
            nextOfferId,
            offerName,
            stationName,
            discountPercentage,
            startDate,
            endDate
        )

        specialOffers.add(newOffer)
        nextOfferId++

        println("✓ Special offer created successfully!")
        println("  Offer ID: #${newOffer.offerId}")
        println("  Name: $offerName")
        println("  Station: $stationName")
        println("  Discount: ${discountPercentage}%")
        println("  Valid: $startDate to $endDate")

        return newOffer
    }

    /**
     * Searches for offers by keyword (searches in offer name and station name)
     * @param searchTerm the term to search for
     * @return list of matching offers
     */
    fun searchOffers(searchTerm: String): List<SpecialOffer> {
        if (searchTerm.isBlank()) {
            println("✗ Search term cannot be empty")
            return emptyList()
        }

        val results = specialOffers.filter {
            it.offerName.contains(searchTerm, ignoreCase = true) ||
                    it.stationName.contains(searchTerm, ignoreCase = true)
        }

        if (results.isEmpty()) {
            println("✗ No offers found matching '$searchTerm'")
        } else {
            println("✓ Found ${results.size} offer(s) matching '$searchTerm'")
        }

        return results
    }

    /**
     * Searches for offers by station name
     * @param stationName the station to search for
     * @return list of offers for this station
     */
    fun searchOffersByStation(stationName: String): List<SpecialOffer> {
        if (stationName.isBlank()) {
            println("✗ Station name cannot be empty")
            return emptyList()
        }

        val results = specialOffers.filter {
            it.stationName.equals(stationName, ignoreCase = true)
        }

        if (results.isEmpty()) {
            println("✗ No offers found for station '$stationName'")
        } else {
            println("✓ Found ${results.size} offer(s) for '$stationName'")
        }

        return results
    }

    /**
     * Gets all active offers
     * @return list of active offers
     */
    fun getActiveOffers(): List<SpecialOffer> {
        return specialOffers.filter { it.isActive }
    }

    /**
     * Gets all offers (active and inactive)
     */
    fun getAllOffers(): List<SpecialOffer> {
        return specialOffers.toList()
    }

    /**
     * Deletes an offer by ID
     * @param offerId the ID of the offer to delete
     * @return true if deleted successfully
     */
    fun deleteOffer(offerId: Int): Boolean {
        val offer = specialOffers.find { it.offerId == offerId }

        if (offer == null) {
            println("✗ Offer #$offerId not found")
            return false
        }

        specialOffers.remove(offer)
        println("✓ Offer #$offerId '${offer.offerName}' deleted successfully")
        return true
    }

    /**
     * Displays all offers
     */
    fun displayAllOffers() {
        if (specialOffers.isEmpty()) {
            println("\n✗ No special offers available\n")
            return
        }

        println("\n========================================")
        println("        ALL SPECIAL OFFERS")
        println("========================================")
        println()

        for (offer in specialOffers) {
            offer.displayInfo()
            println()
        }

        println("Total Offers: ${specialOffers.size}")
        println("Active Offers: ${specialOffers.count { it.isActive }}")
        println("========================================\n")
    }

    /**
     * Displays only active offers
     */
    fun displayActiveOffers() {
        val activeOffers = getActiveOffers()

        if (activeOffers.isEmpty()) {
            println("\n✗ No active offers available\n")
            return
        }

        println("\n========================================")
        println("        ACTIVE SPECIAL OFFERS")
        println("========================================")
        println()

        for (offer in activeOffers) {
            offer.displayInfo()
            println()
        }

        println("Total Active Offers: ${activeOffers.size}")
        println("========================================\n")
    }

    /**
     * Gets an offer by ID
     * @param offerId the offer ID
     * @return the SpecialOffer or null if not found
     */
    fun getOfferById(offerId: Int): SpecialOffer? {
        val offer = specialOffers.find { it.offerId == offerId }
        if (offer == null) {
            println("✗ Offer #$offerId not found")
        }
        return offer
    }

    /**
     * Finds all applicable offers for a station on a specific date
     * @param stationName the station name
     * @param date the date to check (format: "YYYY-MM-DD")
     * @return list of applicable offers
     */
    fun findApplicableOffers(stationName: String, date: String): List<SpecialOffer> {
        return specialOffers.filter {
            it.stationName.equals(stationName, ignoreCase = true) &&
                    it.isValidOnDate(date)
        }
    }

    /**
     * Gets the best offer for a station on a date (highest discount)
     */
    fun getBestOffer(stationName: String, date: String): SpecialOffer? {
        val applicableOffers = findApplicableOffers(stationName, date)
        return applicableOffers.maxByOrNull { it.discountPercentage }
    }

    /**
     * Displays a summary table of all offers
     */
    fun displayOffersSummary() {
        if (specialOffers.isEmpty()) {
            println("\n✗ No special offers available\n")
            return
        }

        println("\n========================================")
        println("      SPECIAL OFFERS SUMMARY")
        println("========================================")
        println()
        println("%-5s %-20s %-15s %-10s %-12s %-8s".format(
            "ID", "Name", "Station", "Discount", "End Date", "Status"
        ))
        println("-".repeat(75))

        for (offer in specialOffers) {
            val status = if (offer.isActive) "Active" else "Inactive"
            println("%-5d %-20s %-15s %-10s %-12s %-8s".format(
                offer.offerId,
                offer.offerName.take(20),
                offer.stationName.take(15),
                "${offer.discountPercentage}%",
                offer.endDate,
                status
            ))
        }

        println("-".repeat(75))
        println("Total: ${specialOffers.size} offers | Active: ${specialOffers.count { it.isActive }}")
        println("========================================\n")
    }

    /**
     * Deactivates an offer by ID
     */
    fun deactivateOffer(offerId: Int): Boolean {
        val offer = getOfferById(offerId)
        if (offer != null) {
            offer.deactivate()
            return true
        }
        return false
    }

    /**
     * Activates an offer by ID
     */
    fun activateOffer(offerId: Int): Boolean {
        val offer = getOfferById(offerId)
        if (offer != null) {
            offer.activate()
            return true
        }
        return false
    }
}
