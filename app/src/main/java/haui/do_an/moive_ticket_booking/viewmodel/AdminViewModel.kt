package haui.do_an.moive_ticket_booking.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import haui.do_an.moive_ticket_booking.DTO.CinemaDTO
import haui.do_an.moive_ticket_booking.DTO.CouponDTO
import haui.do_an.moive_ticket_booking.DTO.Director
import haui.do_an.moive_ticket_booking.DTO.Genres
import haui.do_an.moive_ticket_booking.DTO.HallDTO
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import haui.do_an.moive_ticket_booking.DTO.SearchMovies
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.DTO.UserDTO
import haui.do_an.moive_ticket_booking.model.Coupon
import haui.do_an.moive_ticket_booking.model.ShowTime
import haui.do_an.moive_ticket_booking.repository.CinemaRepository
import haui.do_an.moive_ticket_booking.repository.CouponRepository
import haui.do_an.moive_ticket_booking.repository.MovieRepository
import haui.do_an.moive_ticket_booking.repository.ShowTimeRepository
import haui.do_an.moive_ticket_booking.repository.TmdbRepository
import haui.do_an.moive_ticket_booking.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val tmbdRepository: TmdbRepository,
    private val movieRepository: MovieRepository,
    private val cinemaRepository: CinemaRepository,
    private val showTimeRepository: ShowTimeRepository,
    private val couponRepository: CouponRepository,
    private val userRepository: UserRepository

) : ViewModel() {
    private val _Message = MutableLiveData<String?>()
    val Message: MutableLiveData<String?> = _Message

    private val _moviesTMDB = MutableLiveData<SearchMovies?>()
    val moviesTMDB: MutableLiveData<SearchMovies?> = _moviesTMDB

    private val _movieDetail = MutableLiveData<MovieTmdbDTO?>()
    val movieDetail: MutableLiveData<MovieTmdbDTO?> = _movieDetail

    private val _movies = MutableLiveData<List<MovieDTO>?>()
    val movies: MutableLiveData<List<MovieDTO>?> = _movies

    private val _movie = MutableLiveData<MovieDTO>()
    val movie: MutableLiveData<MovieDTO> = _movie

    private val _cinemas = MutableLiveData<List<CinemaDTO>?>()
    val cinemas: MutableLiveData<List<CinemaDTO>?> = _cinemas

    private val _hall = MutableLiveData<List<HallDTO>?>()
    val hall: MutableLiveData<List<HallDTO>?> = _hall

    private val _showTimes = MutableLiveData<List<ShowTimeDTO>?>()
    val showTimes: MutableLiveData<List<ShowTimeDTO>?> = _showTimes

    private val _genres = MutableLiveData<List<Genres>>()
    val genres: MutableLiveData<List<Genres>> = _genres

    private val _coupons = MutableLiveData<List<CouponDTO>?>()
    val coupons: MutableLiveData<List<CouponDTO>?> = _coupons

    private val _users = MutableLiveData<List<UserDTO>?>()
    val users: MutableLiveData<List<UserDTO>?> = _users

    fun searchMovieTMDB(name: String, apikey: String) {
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = tmbdRepository.searchMovie(name, apikey)
                result?.let {
                    val limitedResult = it.copy(results = it.results.take(5))
                    _moviesTMDB.postValue(limitedResult)
                }
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getMovieDetailTMDB(movieId: Int, apiKey: String) {
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movie = tmbdRepository.getMovieDetail(movieId, apiKey)
                val credits = tmbdRepository.getCredits(movieId, apiKey)
                movie?.let { mv ->
                    credits?.let {
                        mv.actors = credits.actors
                        mv.director = credits.crew
                            .filter { it.job == "Director" }
                            .map { crewMember ->
                                Director(
                                    name = crewMember.name,
                                    id = crewMember.id,
                                )
                            }
                        _movieDetail.postValue(mv)
                    }
                }
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun addMovie(movie: MovieTmdbDTO) {
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.addMovie(movie)
                Message.postValue(result)
            } catch (e: Exception) {
                Message.postValue(e.message.toString())
            }
        }
    }

    fun updateMovie(
        id: Int,
        type: String,
        status: String,
        uriPoster: Uri?,
        uriBanner: Uri?,
        context: Context
    ) {
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.updateMovie(id, type, status, uriPoster, uriBanner, context)
                _Message.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getAllMovie(){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.getAll()
                _movies.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }

        }
    }

    fun getMovieDetail(id: Int, userId: Int){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.getDetailMovie(id, userId)
                _movie.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun searchMovie(searchText: String){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.searchMovie(searchText)
                _movies.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun filterMovie(
        searchText: String?,
        type: String?,
        year: Int?,
        status: String?,
        genres: List<Int>?){
        _Message.postValue(null)
        Log.d("filter", "đã vào viewmodel")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.filterMovie(searchText, type, year, status, genres)
                _movies.postValue(result)
                Log.d("filter", "đã vào viewmodel")
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun deleteMovie(movieId: Int){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.deleteMovie(movieId)
                _Message.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getCinemas(){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = cinemaRepository.getAllCinema()
                _cinemas.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getHall(cinemaId: Int){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = cinemaRepository.getHall(cinemaId)
                _hall.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun addShowTime(showTime: ShowTime) {
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = showTimeRepository.addShowTime(showTime)
                _Message.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getShowTimes(){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = showTimeRepository.getShowTimes()
                _showTimes.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun updateShowTime(id: Int, showTime: ShowTime){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = showTimeRepository.updateShowTime(id, showTime)
                _Message.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getAllGenre(){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = movieRepository.getAllGenre()
                _genres.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getAllCoupon(){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = couponRepository.getAll()
                _coupons.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun createCoupon(coupon: Coupon){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = couponRepository.createCoupon(coupon)
                _Message.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun getAllUser(){
        _Message.postValue(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = userRepository.getAllUser()
                result.sortedByDescending { it.totalPay }
                _users.postValue(result)
            } catch (e: Exception) {
                _Message.postValue(e.message.toString())
            }
        }
    }

    fun clearData(){
        _Message.postValue(null)
    }

}