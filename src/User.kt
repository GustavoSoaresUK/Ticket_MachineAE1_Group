/**
 * Represents a system user with login credentials
 * Assigned to: Group Member C (Marcin Kreza)
 */
class User(
    val username: String,
    private val password: String,
    val isAdmin: Boolean
) {
    var isLoggedIn: Boolean = false
        private set

    /**
     * Attempts to log in the user with provided password
     * @param inputPassword the password to verify
     * @return true if login successful
     */
    fun login(inputPassword: String): Boolean {
        return if (verifyPassword(inputPassword)) {
            isLoggedIn = true
            println("✓ Login successful! Welcome, $username")
            true
        } else {
            println("✗ Login failed: Incorrect password")
            false
        }
    }

    /**
     * Logs out the user
     */
    fun logout() {
        if (isLoggedIn) {
            isLoggedIn = false
            println("✓ User '$username' logged out successfully")
        } else {
            println("✗ User '$username' is not logged in")
        }
    }

    /**
     * Verifies if the provided password matches
     * @param inputPassword password to check
     * @return true if password matches
     */
    fun verifyPassword(inputPassword: String): Boolean {
        return password == inputPassword
    }

    /**
     * Gets user information for display
     */
    fun getUserInfo(): String {
        val role = if (isAdmin) "Admin" else "Regular User"
        val status = if (isLoggedIn) "Logged In" else "Logged Out"
        return "Username: $username | Role: $role | Status: $status"
    }
}
