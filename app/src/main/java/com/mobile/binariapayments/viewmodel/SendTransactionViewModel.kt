package com.mobile.binariapayments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.binariapayments.domain.Resource
import com.mobile.binariapayments.domain.countries.CountriesInteractor
import com.mobile.binariapayments.domain.rates.RatesInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SendTransactionViewModel(
    private val ratesInteractor: RatesInteractor,
    private val countriesInteractor: CountriesInteractor
) : ViewModel() {

    private val _countryRecognisedLiveData = MutableLiveData<Int>()
    val countryRecognisedLiveData: LiveData<Int> = _countryRecognisedLiveData
    private val _validationLiveData = MutableLiveData<Boolean>()
    val validationLiveData = _validationLiveData
    private val _validationNameLiveData = MutableLiveData<Boolean>()
    val validationNameLiveData: LiveData<Boolean> = _validationNameLiveData
    private val _validationPhoneLiveData = MutableLiveData<Boolean>()
    val validationPhoneLiveData: LiveData<Boolean> = _validationPhoneLiveData
    private val _validationAmountLiveData = MutableLiveData<Boolean>()
    val validationAmountLiveData = _validationAmountLiveData
    private val _receivedAmountLiveData = MutableLiveData<String>()
    val receivedAmountLiveData: LiveData<String> = _receivedAmountLiveData
    private val _sendTransactionLiveData = MutableLiveData<Boolean>()
    val sendTransactionLiveData: LiveData<Boolean> = _sendTransactionLiveData
    private val _countryChangedLiveData = MutableLiveData<String>()
    val countryChangedLiveData: LiveData<String> = _countryChangedLiveData
    private val _countiesLiveData = MutableLiveData<List<String>>()
    val countriesLiveData: LiveData<List<String>> = _countiesLiveData
    private val countriesFlowData by lazy { countriesInteractor.getCountries() }

    //region viewmodel public methods
    fun calculateReceivedAmount(amount: String, countrySelectedPosition: Int) {
        val amountInDecimal = if (amount.isNotBlank()) amount.toInt(BINARY_RADIX) else 0
        if (amountInDecimal > 0 && countrySelectedPosition > 0) {
            viewModelScope.launch {
                countriesInteractor.getCountries().mapLatest { countries ->
                    countries[countrySelectedPosition - 1].currencyCode
                }.collect { currencyCode ->
                    ratesInteractor.getRates(currencyCode)
                        .collect {
                            val amountToReceive = when (it.status) {
                                Resource.Status.SUCCESS -> "$currencyCode ${BigDecimal.valueOf(
                                    (it.data ?: 0.0) * amount.toInt(BINARY_RADIX)
                                ).toBigInteger().toString(BINARY_RADIX)}"
                                Resource.Status.ERROR -> DEFAULT_AMOUNT
                            }
                            _receivedAmountLiveData.value = amountToReceive
                            validateInput()
                        }
                }
            }
        } else {
            _receivedAmountLiveData.value = DEFAULT_AMOUNT
            validateInput()
        }
    }

    fun checkPhonePrefix(phoneNumber: String) = viewModelScope.launch {
        countriesFlowData.mapLatest { countries ->
            countries
                .indexOfFirst { it.prefixPattern.matcher(phoneNumber).matches() }
        }.collect { _countryRecognisedLiveData.value = it }
    }

    fun loadData() = viewModelScope.launch {
        countriesFlowData.mapLatest { countries ->
            countries.map { it.countryName }
        }.collect { _countiesLiveData.value = it }
    }

    fun onCountryChanged(position: Int) = viewModelScope.launch {
        if (position > 0) {
            countriesFlowData.mapLatest { countries ->
                countries[position - 1].phonePrefix
            }.collect { prefix ->
                _countryChangedLiveData.value = prefix
            }
        }
    }

    fun validateNameData(fullName: String) {
        _validationNameLiveData.value = fullName.isNotBlank()
        validateInput()
    }

    fun validatePhoneData(phoneNumber: String) =
        viewModelScope.launch {
            countriesFlowData.mapLatest { countries ->
                countries
                    .any {
                        it.phonePattern.matcher(phoneNumber.replace(" ", "")).matches()
                    }
            }.collect { anyValid ->
                _validationPhoneLiveData.value = anyValid
                validateInput()
            }
        }

    fun validateAmountData(amount: String) {
        _validationAmountLiveData.value =
            amount.isNotBlank() && amount.trim().toInt(BINARY_RADIX) > 0
        validateInput()
    }

    fun sendMoney() {
        _sendTransactionLiveData.value = true
    }

    //endregion viewmodel public methods
    //region viewmodel private methods
    private fun validateInput() {
        _validationLiveData.value =
            _validationNameLiveData.value == true
                    && _validationPhoneLiveData.value == true
                    && _validationAmountLiveData.value == true
                    && _receivedAmountLiveData.value != DEFAULT_AMOUNT
    }

    //endregion viewmodel private methods
    companion object {

        private const val BINARY_RADIX = 2
        const val DEFAULT_AMOUNT = "0"
    }
}