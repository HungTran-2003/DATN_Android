package haui.do_an.moive_ticket_booking.api

object ApiRoutes {
    const val BASE_URL = "http://192.168.1.16:8080"
//    const val BASE_URL = "https://d789-113-185-50-55.ngrok-free.app"
    const val TMDB_URL = "https://api.themoviedb.org/3/"
    const val PAYOS = "https://api-merchant.payos.vn"

    object Base{
        const val GET_OTP = "/api/otp"
        const val SEND_OTP = "/api/otp/verify-otp"
    }
    object City{
        const val ALL = "/api/cities"
    }

    object Cinema{
        const val INCITY = "/api/cinemas/by-city/{cityId}"
        const val ALL = "/api/cinemas/getAll"
        const val GET_HALL = "/api/cinemas/getHall/{cinemaId}"
        const val GET_HALL_BY_SHOWTIME = "/api/cinemas/getHallByShowtime/{showtimeId}"
    }

    object User{
        const val ADD = "/api/users/add"
        const val LOGIN = "/api/users/login"
        const val FAVOURITE = "/api/users/favorite-movie"
        const val GETALL = "/api/users/getAll"
    }

    object TMDB{
        const val SEARCH = "search/movie?language=vi"
        const val MOVIE_DETAIL = "movie/{movie_id}?language=vi"
        const val CREDITS = "movie/{movie_id}/credits"
    }

    object Movie{
        const val ADD = "/api/movies/add"
        const val ALL = "/api/movies/getAll"
        const val DETAIL = "/api/movies/detail/{userId}/{movieId}"
        const val IMAGE = "/api/images/"
        const val SEARCH = "/api/movies/search"
        const val POPULAR = "/api/movies/popular"
        const val MOVIE_SHOWING = "/api/movies/showing"
        const val MOVIE_COMING = "/api/movies/coming"
        const val SPECAIL = "/api/movies/special"
        const val UPDATE = "/api/movies/update"
        const val ALLGENRE = "/api/movies/getAllGenre"
        const val FILTER = "/api/movies/filter"
        const val DELETE = "/api/movies/delete/{movieId}"

    }

    object ShowTime{
        const val ADD = "/api/showtime/add"
        const val ALL = "/api/showtime/list"
        const val DETAIL = "/api/showtime/{id}"
        const val GET_SEATS = "/api/showtime/seats/{hallId}"
        const val GET_TICKETS_BY_USER = "/api/showtime/{showtimeId}/user/{userId}/tickets"

    }

    object Booking{
        const val ADD = "/api/bookings/create"
        const val ALL = "/api/bookings/list"
        const val DETAIL = "/api/bookings/{id}"
        const val GET_ALL_BY_USER = "/api/bookings/getAll/{userId}"
        const val GET_USED = "/api/bookings/getBookingUsed/{userId}"

    }

    object Ticket{
        const val GET_SEAT_BY_SHOWTIME = "/api/tickets/getSeatsByShowTimeId/{showtimeId}"
        const val GET_ALL = "/api/tickets/{userId}/{bookingId}"
    }

    object Coupon{
        const val ADD = "/api/coupons/create"
        const val ALL = "/api/coupons/getAll"
        const val ALL_BY_USER = "/api/coupons/available/{userId}"
        const val DETAIL = "/api/coupons/detail/{couponId}"
        const val UPDATE = "/api/coupons/update"
    }

    object PayOs{
        const val GET_LINK = "/payment/create"
        const val GET_QR = "/api/vietqr/get-qr"
    }


}