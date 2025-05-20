package haui.do_an.moive_ticket_booking.utils

class Validatior {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")

    private val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}\$")

    fun isValidEmail(email: String?): Boolean {
        return email != null && emailRegex.matches(email)
    }

    fun isValidPassword(password: String?): Boolean {
        return password != null && passwordRegex.matches(password)
    }

    fun isValidUsername(username: String?): Boolean {
        return username != null && username.length in 7..49
    }



}