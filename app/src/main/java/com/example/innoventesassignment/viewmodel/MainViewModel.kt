package com.example.innoventesassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class MainViewModel : ViewModel() {

    private val _isDateValid = MutableLiveData<Boolean>()
    val isDateValid: LiveData<Boolean> = _isDateValid

    private val _isPanValid = MutableLiveData<Boolean>()
    val isPanValid: LiveData<Boolean> = _isPanValid

    private val _isNextButtonEnabled = MutableLiveData<Boolean>()
    val isNextButtonEnabled: LiveData<Boolean> = _isNextButtonEnabled

    fun validateDate(day: String?, month: String?, year: String?) {
        val dayInt = day?.toIntOrNull()
        val monthInt = month?.toIntOrNull()
        val yearInt = year?.toIntOrNull()

        if (dayInt != null && monthInt != null && yearInt != null &&
            isDayValid(dayInt, monthInt, yearInt) && !isFutureDate(dayInt, monthInt, yearInt)
        ) {
            _isDateValid.value = true
            updateNextButtonState()
        } else {
            _isDateValid.value = false
            _isNextButtonEnabled.value = false
        }
    }

    fun validatePan(pan: String?) {
        _isPanValid.value = pan?.matches(Regex("[A-Z]{5}[0-9]{4}[A-Z]")) == true
        updateNextButtonState()
    }

    private fun isDayValid(day: Int, month: Int, year: Int): Boolean {
        return when (month) {
            4, 6, 9, 11 -> day in 1..30
            2 -> if (isLeapYear(year)) day in 1..29 else day in 1..28
            else -> day in 1..31
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    private fun isFutureDate(day: Int, month: Int, year: Int): Boolean {
        val currentDate = Calendar.getInstance()
        val inputDate = Calendar.getInstance().apply {
            set(year, month - 1, day)
        }
        return inputDate.after(currentDate)
    }

    private fun updateNextButtonState() {
        _isNextButtonEnabled.value = _isDateValid.value == true && _isPanValid.value == true
    }
}
