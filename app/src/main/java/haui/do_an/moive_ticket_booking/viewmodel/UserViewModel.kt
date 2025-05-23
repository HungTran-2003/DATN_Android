package haui.do_an.moive_ticket_booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import haui.do_an.moive_ticket_booking.DTO.BookingDTO
import haui.do_an.moive_ticket_booking.DTO.CouponDTO
import haui.do_an.moive_ticket_booking.DTO.HallDTO
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.DTO.SeatDTO
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.DTO.TicketDTO
import haui.do_an.moive_ticket_booking.DTO.TicketInfo
import haui.do_an.moive_ticket_booking.model.Booking
import haui.do_an.moive_ticket_booking.model.ShowTime
import haui.do_an.moive_ticket_booking.repository.BookingRepository
import haui.do_an.moive_ticket_booking.repository.CinemaRepository
import haui.do_an.moive_ticket_booking.repository.CouponRepository
import haui.do_an.moive_ticket_booking.repository.MovieRepository
import haui.do_an.moive_ticket_booking.repository.PayOsRepository
import haui.do_an.moive_ticket_booking.repository.ShowTimeRepository
import haui.do_an.moive_ticket_booking.repository.TicketRepository
import haui.do_an.moive_ticket_booking.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.Continuation

@HiltViewModel
class UserViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val showTimeRepository: ShowTimeRepository,
    private val cinemaRepository: CinemaRepository,
    private val ticketRepository: TicketRepository,
    private val bookingRepository: BookingRepository,
    private val couponRepository: CouponRepository,
    private val payOsRepository: PayOsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _Message = MutableLiveData<String?>()
    val Message: MutableLiveData<String?> = _Message

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _movies = MutableLiveData<List<MovieDTO>?>()
    val movies: MutableLiveData<List<MovieDTO>?> = _movies

    private val _movieShowing = MutableLiveData<List<MovieDTO>?>()
    val movieShowing: MutableLiveData<List<MovieDTO>?> = _movieShowing

    private val _movieComing = MutableLiveData<List<MovieDTO>?>()
    val movieComing: MutableLiveData<List<MovieDTO>?> = _movieComing

    private val _movieSpecial = MutableLiveData<List<MovieDTO>?>()
    val movieSpecial: MutableLiveData<List<MovieDTO>?> = _movieSpecial

    private val _movie = MutableLiveData<MovieDTO>()
    val movie: MutableLiveData<MovieDTO> = _movie

    private val _showTimes = MutableLiveData<List<ShowTimeDTO>?>()
    val showTimes: MutableLiveData<List<ShowTimeDTO>?> = _showTimes

    private val _seatIdReserved = MutableLiveData<List<Int>>()
    val seatIdReserved: MutableLiveData<List<Int>> = _seatIdReserved

    private val _seats = MutableLiveData<List<SeatDTO>>()
    val seats: MutableLiveData<List<SeatDTO>> = _seats

    private val _tickets = MutableLiveData<List<TicketDTO>>()
    val tickets: MutableLiveData<List<TicketDTO>> = _tickets

    private val _ticketInfo = MutableLiveData<TicketInfo>()
    val ticketInfo: LiveData<TicketInfo> = _ticketInfo

    private val _coupons = MutableLiveData<List<CouponDTO>>()
    val coupons: LiveData<List<CouponDTO>> = _coupons

    private val _bookings = MutableLiveData<List<BookingDTO>>()
    val bookings: LiveData<List<BookingDTO>> = _bookings

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    var CouponCode: String? = null
    var discound: Double? = null

    fun getMovieShowing(){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.getMovieShowing()
                _movieShowing.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getMovieComming(){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.getMovieComing()
                _movieComing.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getMovieSpecial(){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.getMovieSpecial()
                _movieSpecial.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getMovieDetail(id: Int, userId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.getDetailMovie(id, userId)
                _movie.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getShowTimes(movieId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = showTimeRepository.getShowTime(movieId)
                _showTimes.postValue(result!!)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getSeatReserved(showtimeId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = ticketRepository.getSeatReserved(showtimeId)
                _seatIdReserved.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getSeats(hallId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = showTimeRepository.getSeats(hallId)
                _seats.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun createBooking(booking: Booking){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = bookingRepository.addBooking(booking)
                val request = mapOf(
                    "orderCode" to result.get("bookingId")!!.toInt(),
                    "productName" to movie.value!!.title,
                    "description" to "CineX",
                    "returnUrl" to "myapp://success",
                    "cancelUrl" to "myapp://cancel",
                    "price" to booking.totalPrice
                )
                createLinkPayment(request)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }

        }
    }

    fun getTicket(userId: Int, bookingId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = ticketRepository.getAll(userId, bookingId)
                _tickets.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getTicketInfo(showTimeId: Int, userId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = showTimeRepository.getTicketInfo(showTimeId, userId)
                _ticketInfo.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getCoupon(userId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = couponRepository.getAllByUser(userId)
                _coupons.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getBooking(userId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = bookingRepository.getAllByUser(userId)
                _bookings.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getUsed(userId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = bookingRepository.getUsed(userId)
                _bookings.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun createLinkPayment(request: Map<String, Any>){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = payOsRepository.createLinkPayment(request)
                _url.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun addMovieFavorite(userId: Int, movieId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = userRepository.addFavourite(userId, movieId)
                _Message.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun deleteMovieFavorite(userId: Int, movieId: Int){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = userRepository.deleteFavourite(userId, movieId)
                _Message.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun changePassword(userId: Int, newPassword: String){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = userRepository.changePassword(userId, newPassword)
                _Message.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun getIdByEmail(email: String){
        _error.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = userRepository.getIdByEmail(email)
                _Message.postValue(result.toString())
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
            }
        }
    }

    fun saveCode(code: String, discount: Double){
        CouponCode = code
        this.discound = discount
    }

    fun clearErrorMessage(){
        _error.postValue(null)
        _Message.postValue(null)
    }
}