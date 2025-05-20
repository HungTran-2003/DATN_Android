package haui.do_an.moive_ticket_booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import haui.do_an.moive_ticket_booking.DTO.CityDTO
import haui.do_an.moive_ticket_booking.repository.CinemaRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WcViewModel @Inject constructor(
    private val cinemaRepository: CinemaRepository
) : ViewModel() {
    private val _cities = MutableLiveData<List<CityDTO>>()
    val cities: MutableLiveData<List<CityDTO>> = _cities


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        loadCitys()
    }

    private fun loadCitys(){
        viewModelScope.launch {
            try {
                val result = cinemaRepository.getCitys()
                _cities.value = result as List<CityDTO>?

            } catch (e: Exception) {
                _errorMessage.value = e.message

            }
        }
    }


}