/**
 * Manages user authentication and login
 * Assigned to: Group Member C (Marcin Kreza)
 */
class LoginSystem {
    private val users: MutableList<User> = mutableListOf()
    private var currentUser: User? = null

    init {
        // Initialize with hardcoded users as per requirements
        users.add(User("admin", "admin123", true))
        users.add(User("manager", "manager456", true))
        users.add(User("john", "john789", false))
        users.add(User("alice", "alice321", false))

        println("LoginSystem initialized with ${users.size} users")
    }

    /**
     * Attempts to log in a user
     * @param username the username
     * @param password the password
     * @return true if login successful
     */
    fun login(username: String, password: String): Boolean {
        // Check if someone is already logged in
        if (currentUser != null && currentUser!!.isLoggedIn) {
            println("✗ Another user is already logged in. Please logout first.")
            return false
        }

        // Find the user
        val user = users.find { it.username.equals(username, ignoreCase = true) }

        if (user == null) {
            println("✗ Login failed: User '$username' not found")
            return false
        }

        // Attempt login
        val success = user.login(password)
        if (success) {
            currentUser = user
        }
        return success
    }

    /**
     * Logs out the current user
     * @return true if logout successful
     */
    fun logout(): Boolean {
        if (currentUser == null) {
            println("✗ No user is currently logged in")
            return false
        }

        currentUser?.logout()
        currentUser = null
        return true
    }

    /**
     * Gets the currently logged-in user
     * @return current User or null if no one logged in
     */
    fun getCurrentUser(): User? {
        return currentUser
    }

    /**
     * Checks if an admin is currently logged in
     * @return true if admin is logged in
     */
    fun isAdminLoggedIn(): Boolean {
        return currentUser?.isAdmin == true && currentUser?.isLoggedIn == true
    }

    /**
     * Checks if any user is logged in
     * @return true if a user is logged in
     */
    fun isUserLoggedIn(): Boolean {
        return currentUser?.isLoggedIn == true
    }

    /**
     * Displays all users (admin function for testing)
     */
    fun displayAllUsers() {
        println("\n========================================")
        println("           ALL SYSTEM USERS")
        println("========================================")
        println("%-15s %-15s %-15s".format("Username", "Role", "Status"))
        println("-".repeat(45))

        for (user in users) {
            val role = if (user.isAdmin) "Admin" else "Regular"
            val status = if (user.isLoggedIn) "Logged In" else "Logged Out"
            println("%-15s %-15s %-15s".format(user.username, role, status))
        }
        println("========================================\n")
    }

    /**
     * Gets current user information
     */
    fun getCurrentUserInfo(): String? {
        return currentUser?.getUserInfo()
    }

    /**
     * Checks credentials without logging in
     * @param username the username to check
     * @param password the password to verify
     * @return true if credentials are valid
     */
    fun verifyCredentials(username: String, password: String): Boolean {
        val user = users.find { it.username.equals(username, ignoreCase = true) }
        return user?.verifyPassword(password) == true
    }

    /**
     * Adds a new user (for testing/admin purposes)
     */
    fun addUser(username: String, password: String, isAdmin: Boolean): Boolean {
        // Check if username already exists
        if (users.any { it.username.equals(username, ignoreCase = true) }) {
            println("✗ User '$username' already exists")
            return false
        }

        if (username.isBlank() || password.isBlank()) {
            println("✗ Username and password cannot be empty")
            return false
        }

        users.add(User(username, password, isAdmin))
        println("✓ User '$username' added successfully")
        return true
    }
}
