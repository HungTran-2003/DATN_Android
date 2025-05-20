package haui.do_an.moive_ticket_booking.view.admin

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import haui.do_an.moive_ticket_booking.BuildConfig
import haui.do_an.moive_ticket_booking.DTO.Genres
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.adapter.admin.ListMovieAdapter
import haui.do_an.moive_ticket_booking.adapter.admin.SearchMovieTmDBAdapter
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import haui.do_an.moive_ticket_booking.databinding.FragmentHomeFilmBinding
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class HomeFilmFragment : Fragment() {

    private lateinit var binding: FragmentHomeFilmBinding
    private val viewModel : AdminViewModel by activityViewModels()

    private val DELAY_MS: Long = 1000
    private var searchJob: Job? = null

    private lateinit var adapterSearchMovie: SearchMovieTmDBAdapter
    private lateinit var adapterListMovie: ListMovieAdapter

    private val selectGenres = mutableListOf<Int>()
    private var filter = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeFilmBinding.bind(view)
        adapterSearchMovie = SearchMovieTmDBAdapter(requireContext())
        binding.movieNameAdd.setAdapter(adapterSearchMovie)

        binding.addButton.setOnClickListener {
            binding.addMovieLayout.visibility = View.VISIBLE
        }

        viewModel.getAllMovie()
        viewModel.getAllGenre()

        setAdapterAutoCpl()
        setSearchAddInput()
        setAdapterListMovie()
        setUpSpinnerStatic()
        setupGenreChips()

        clickAddButton()
        clickItemOnAutoComPlView()
        clickFlitterButton()
        clickBtnSelectYear()
        clickBtnApplyFilter()
        clickBtnSearchMovie()

        clearFilter()

        messeage()
    }

    fun clickAddButton(){
        binding.addMovieButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nameMovie", binding.movieNameAdd.text.toString())
            (activity as? AdminActivity)?.navigateToAddMovie(bundle)
        }
    }

    fun clickFlitterButton(){
        binding.filterButton.setOnClickListener {
            if(!filter){
                binding.filterLayout.visibility = View.VISIBLE
                filter = true
            } else {
                binding.filterLayout.visibility = View.GONE
                filter = false
            }
        }
    }

    fun setSearchAddInput(){
        binding.movieNameAdd.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                searchJob?.cancel()
                val query = s.toString().trim()
                if(query.isNotEmpty()){
                    searchJob = CoroutineScope(Dispatchers.IO).launch {
                        delay(DELAY_MS)
                        viewModel.searchMovieTMDB(query, BuildConfig.API_KEY)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setAdapterAutoCpl(){
        viewModel.moviesTMDB.observe(viewLifecycleOwner) { result ->
            result?.let {
                adapterSearchMovie.update(result)
                adapterSearchMovie.filter.filter(binding.movieNameAdd.text)
                binding.movieNameAdd.showDropDown()
            }
        }
    }

    private fun clickItemOnAutoComPlView(){
        binding.movieNameAdd.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as MovieTmdbDTO
            binding.movieNameAdd.setText(null)
            val bundle = Bundle()
            bundle.putBoolean("selectedMovie", true)
            viewModel.getMovieDetailTMDB(selectedItem.id, BuildConfig.API_KEY)
            (activity as? AdminActivity)?.navigateToAddMovie(bundle)
        }
    }

    private fun setAdapterListMovie(){
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        adapterListMovie = ListMovieAdapter(requireContext(),
            onItemClick = {
                val bundle = Bundle()
                bundle.putInt("movieId", it.id)
                (activity as? AdminActivity)?.navigateToFilmDetail(bundle)
            },
            onLongItemClick = {
                deleteMovie(it.id)
            })
        binding.recyclerViewItems.adapter = adapterListMovie
        viewModel.movies.observe(viewLifecycleOwner) { result ->
            result?.let {
                adapterListMovie.submitList(result)
                if (filter){
                    binding.filterLayout.visibility = View.GONE
                    filter = false
                }
            }
        }
    }

    private fun setUpSpinnerStatic(){
        val listStatus = listOf("Chọn trạng thái phim","Đang chiếu", "Sắp chiếu", "Đặc biệt", "Chưa xác định", "Đã chiếu")
        val listType = listOf("Chọn loại phim", "P", "C13", "C16", "C18")

        val adapterStatus = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listStatus)
        val adapterType = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listType)

        binding.snStatus.adapter = adapterStatus
        binding.snType.adapter = adapterType

    }

    private fun setupGenreChips(){
        viewModel.genres.observe(viewLifecycleOwner) { result ->
            binding.genreChipGroup.removeAllViews()
            val genres = result
            for(genre in genres) {
                val chip = Chip(requireContext())
                chip.text = genre.name
                chip.isCheckable = true
                binding.genreChipGroup.addView(chip)
                chip.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectGenres.add(genre.genreId)
                    } else {
                        selectGenres.remove(genre.genreId)
                    }
                }
            }
        }
    }

    private fun clickBtnSelectYear(){
        binding.btnSelectYear.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_year_picker, null)
            val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberPickerYear)

            val thisYear = Calendar.getInstance().get(Calendar.YEAR)
            numberPicker.minValue = 1900
            numberPicker.maxValue = thisYear
            numberPicker.value = thisYear

            AlertDialog.Builder(context)
                .setTitle("Chọn năm")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ -> binding.btnSelectYear.text = numberPicker.value.toString()}
                .setNegativeButton("Hủy", null)
                .show()
        }
    }

    private fun clickBtnApplyFilter(){
        binding.buttonApplyFilter.setOnClickListener {
            val selectedStatus = when(binding.snStatus.selectedItemPosition){
                1 -> "NOW_SHOWING"
                2 -> "COMING_SOON"
                3 -> "SPECIAL"
                4 -> "UNDETERMINED"
                5 -> "FINISHED_SHOWING"
                else -> null
            }
            var selectedType : String? = null

            if (binding.snType.selectedItemPosition != 0){
                selectedType = binding.snType.selectedItem.toString()
            }
            val textSearch = binding.etMovieName.text.toString()
            val year = binding.btnSelectYear.text.toString().toIntOrNull()
            Log.d("TAG", "clickBtnApplyFilter")
            viewModel.filterMovie(textSearch, selectedType, year, selectedStatus, selectGenres)
        }

    }

    private fun clearFilter(){
        binding.buttonClearFilter.setOnClickListener {
            binding.etMovieName.text?.clear()
            binding.snStatus.setSelection(0)
            binding.snType.setSelection(0)
            binding.btnSelectYear.text = "CHỌN NĂM PHÁT HÀNH"
            binding.genreChipGroup.clearCheck()
            viewModel.getAllMovie()
        }
    }

    private fun clickBtnSearchMovie(){
        binding.searchButton.setOnClickListener {
            val searchText = binding.findMovieEditText.text.toString()
            viewModel.searchMovie(searchText)
        }
    }

    private fun messeage(){
        viewModel.Message.observe(viewLifecycleOwner) {
            if (it != null) {
                AlertDialog.Builder(context)
                    .setTitle("Thông báo")
                    .setMessage(it)
                    .setPositiveButton("OK", null)
                    .show()
                if (it == "Xoá phim thành công"){
                    viewModel.getAllMovie()
                }
                Log.d("TAG", it)
            }
        }
    }

    private fun deleteMovie(movieId : Int){
        AlertDialog.Builder(context)
            .setTitle("Xóa phim")
            .setMessage("Bạn có chắc chắn muốn xóa phim này?")
            .setPositiveButton("Có") { _, _ ->
                viewModel.deleteMovie(movieId)
            }
            .setNegativeButton("Không", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearData()
    }
}
