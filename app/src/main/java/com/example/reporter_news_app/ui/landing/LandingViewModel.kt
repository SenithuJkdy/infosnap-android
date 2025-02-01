package com.example.reporter_news_app.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reporter_news_app.R

class LandingViewModel : ViewModel() {

    private val _imageIndex = MutableLiveData(0)
    val imageIndex: LiveData<Int> = _imageIndex

    private val imageList = listOf(
        R.drawable.image1,  // Replace with actual image resources
        R.drawable.image2,
        R.drawable.image3
    )

    fun getImage(): Int {
        return imageList[_imageIndex.value ?: 0]
    }

    fun nextImage() {
        if (_imageIndex.value!! < imageList.size - 1) {
            _imageIndex.value = _imageIndex.value!! + 1
        }
    }

    fun prevImage() {
        if (_imageIndex.value!! > 0) {
            _imageIndex.value = _imageIndex.value!! - 1
        }
    }

    fun isLastImage(): Boolean {
        return _imageIndex.value == imageList.size - 1
    }

    fun isFirstImage(): Boolean {
        return _imageIndex.value == 0
    }
}