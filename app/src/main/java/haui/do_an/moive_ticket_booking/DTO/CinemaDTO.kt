package haui.do_an.moive_ticket_booking.DTO

import com.google.gson.annotations.SerializedName

data class CinemaDTO (
    @SerializedName("cinemaId")
    val id: Int,
    val name: String,
    val address: String?,
    val contactInfo: String?,
    val halls : List<HallDTO>?
) {
    override fun toString(): String {
        return name
    }
}

data class HallDTO(
    @SerializedName("hallId")
    val id: Int,
    val name: String,
    val capacity: Int?
){
    override fun toString(): String {
        return name
    }
}