package com.gmail.eamosse.imdb.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.eamosse.idbdata.data.Movie
import com.gmail.eamosse.idbdata.data.Video
import com.gmail.eamosse.idbdata.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.gmail.eamosse.idbdata.utils.Result


class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movieDetails: MutableLiveData<Movie> = MutableLiveData()
    val movieDetails: LiveData<Movie>
        get() = _movieDetails

    var newMovieDetails:Movie? = null

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error


    fun getMovieDetails(movieId: Int) {
        println("EEEE")

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getMovieDetails(movieId)) {
                is Result.Succes -> {
                    _movieDetails.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }

    fun getMovieVideos(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getMovieVideos(movieId)) {
                is Result.Succes -> {
                    newMovieDetails = movieDetails.value
                    newMovieDetails?.videos = result.data
                    _movieDetails.postValue(newMovieDetails)
                }
                is Result.Error -> {

                    _error.postValue(result.message)
                }
            }
        }
    }
}