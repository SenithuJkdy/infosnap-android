package com.example.reporter_news_app.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reporter_news_app.R

class LandingViewModel : ViewModel() {

    private val _imageIndex = MutableLiveData(0)
    val imageIndex: LiveData<Int> = _imageIndex

    private val imageList = listOf(
        R.drawable.landing3,
        R.drawable.landing2,
        R.drawable.landing1
    )

    fun getImage(index: Int): Int = imageList[index]

    fun nextImage() {
        _imageIndex.value?.let {
            if (it < imageList.size - 1) _imageIndex.postValue(it + 1)
        }
    }

    fun prevImage() {
        _imageIndex.value?.let {
            if (it > 0) _imageIndex.postValue(it - 1)
        }
    }

    fun isLastImage(): Boolean = _imageIndex.value == imageList.lastIndex

    fun isFirstImage(): Boolean = _imageIndex.value == 0

    fun getImageCount(): Int {
        return imageList.size
    }
}
