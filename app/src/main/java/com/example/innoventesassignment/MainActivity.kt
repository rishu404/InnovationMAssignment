package com.example.innoventesassignment

import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.innoventesassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        observeViewModel()
    }


    private fun setupUI() {
        binding.edtDate.filters = arrayOf(InputFilter.LengthFilter(2))
        binding.edtMonth.filters = arrayOf(InputFilter.LengthFilter(2))
        binding.edtYear.filters = arrayOf(InputFilter.LengthFilter(4))
        binding.panNumberTextInputEditText.filters = arrayOf(InputFilter.LengthFilter(10))
        setupValidationListeners()
        setupClickListeners()
    }

    private fun setupValidationListeners() {
        binding.edtDate.addTextChangedListener { onDateChanged() }
        binding.edtMonth.addTextChangedListener { onDateChanged() }
        binding.edtYear.addTextChangedListener { onDateChanged() }
        binding.panNumberTextInputEditText.addTextChangedListener { onPanChanged() }
    }

    private fun onDateChanged() {
        val day = binding.edtDate.text.toString()
        val month = binding.edtMonth.text.toString()
        val year = binding.edtYear.text.toString()
        viewModel.validateDate(day, month, year)
    }

    private fun onPanChanged() {
        val pan = binding.panNumberTextInputEditText.text.toString()
        viewModel.validatePan(pan)
    }

    private fun setupClickListeners() {
        binding.txtNoPan.setOnClickListener { finish() }
        binding.btnNext.setOnClickListener {
            Toast.makeText(this, "Details submitted successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.isDateValid.observe(this) { isValid ->
            if (!isValid) {
                binding.edtDate.error = "Invalid date"
                binding.edtMonth.error = "Invalid month"
                binding.edtYear.error = "Invalid year"
            } else {
                binding.edtDate.error = null
                binding.edtMonth.error = null
                binding.edtYear.error = null
            }
        }

        viewModel.isPanValid.observe(this) { isValid ->
            binding.panNumberTextInputEditText.error = if (!isValid) "Invalid PAN number" else null
        }

        viewModel.isNextButtonEnabled.observe(this) { isEnabled ->
            binding.btnNext.isEnabled = isEnabled
            binding.btnNext.alpha = if (isEnabled) 1.0f else 0.5f
        }
    }


}