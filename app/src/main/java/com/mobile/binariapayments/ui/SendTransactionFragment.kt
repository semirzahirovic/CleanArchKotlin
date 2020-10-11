package com.mobile.binariapayments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mobile.binariapayments.R
import com.mobile.binariapayments.viewmodel.SendTransactionViewModel
import com.mobile.binariapayments.viewmodel.SendTransactionViewModel.Companion.DEFAULT_AMOUNT
import kotlinx.android.synthetic.main.fragment_send_transaction.*
import org.koin.android.viewmodel.ext.android.viewModel

class SendTransactionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val sendTransactionViewModel: SendTransactionViewModel by viewModel()

    //region lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupSpinnerData()
        registerObservers()

        button_send.setOnClickListener {
            sendTransactionViewModel.sendMoney()
        }
    }

    //endregion lifecycle
    //region listener methods
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sendTransactionViewModel.onCountryChanged(position)
        calculateReceivedAmount()
        edittext_amount.isEnabled = position > 0
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    //endregion listener methods
    //region private methods
    private fun calculateReceivedAmount() {
        sendTransactionViewModel.calculateReceivedAmount(
            edittext_amount.text.toString().trim(),
            spinner_country.selectedItemPosition
        )
    }

    private fun clearFields() {
        edittext_first_last_name.setText("")
        edittext_amount.setText("")
        edittext_phone.setText("")
    }

    private fun checkPhonePrefix(prefix: String) {
        sendTransactionViewModel.checkPhonePrefix(prefix)
    }

    private fun registerObservers() {
        with(sendTransactionViewModel) {
            validationLiveData.observe(
                viewLifecycleOwner,
                Observer<Boolean> { valid ->
                    button_send.isEnabled = valid
                })

            countryRecognisedLiveData.observe(
                viewLifecycleOwner,
                Observer<Int> { position ->
                    spinner_country.setSelection(position + 1)
                })

            receivedAmountLiveData.observe(
                viewLifecycleOwner,
                Observer<String> { amount ->
                    with(textview_amount_recevied) {
                        text = amount
                        setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            getEndIconResId(amount != DEFAULT_AMOUNT),
                            0
                        )
                    }
                })

            validationNameLiveData.observe(
                viewLifecycleOwner,
                Observer<Boolean> { validated ->
                    text_input_name.setEndIconDrawable(getEndIconResId(validated))
                })
            validationPhoneLiveData.observe(
                viewLifecycleOwner,
                Observer<Boolean> { validated ->
                    text_input_phone.setEndIconDrawable(getEndIconResId(validated))
                })
            validationAmountLiveData.observe(
                viewLifecycleOwner,
                Observer<Boolean> { validated ->
                    text_input_amount.setEndIconDrawable(getEndIconResId(validated))
                })

            validationLiveData.observe(
                viewLifecycleOwner,
                Observer<Boolean> { validated ->
                    button_send.isEnabled = validated
                })

            sendTransactionLiveData.observe(
                viewLifecycleOwner,
                Observer<Boolean> {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.transcation_successful_placeholder),
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                    clearFields()
                })

            countryChangedLiveData.observe(viewLifecycleOwner, Observer<String> {
                with(edittext_phone) {
                    setText(it)
                    setSelection(it.length)
                }
            })

            countriesLiveData.observe(viewLifecycleOwner, Observer<List<String>> { countries ->
                spinner_country.adapter =
                    CountrySpinnerAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        adapter.addAll(
                            listOf(getString(R.string.spinner_country_hint)).plus(
                                countries
                            )
                        )
                    }
            })
        }
    }

    private fun setupSpinnerData() {
        sendTransactionViewModel.loadData()
    }

    private fun setupViews() {
        text_input_name.setEndIconDrawable(R.drawable.ic_validate_error_24dp)
        text_input_phone.setEndIconDrawable(R.drawable.ic_validate_error_24dp)
        text_input_amount.setEndIconDrawable(R.drawable.ic_validate_error_24dp)

        edittext_first_last_name.doAfterTextChanged {
            validateRecipientName(it.toString())
        }
        edittext_amount.doAfterTextChanged {
            calculateReceivedAmount()
            validateAmount(it.toString())
        }

        with(edittext_phone) {
            doAfterTextChanged {
                checkPhonePrefix(it.toString())
                validateRecipientPhone(it.toString())
            }
        }
        spinner_country.onItemSelectedListener = this
    }

    private fun validateRecipientName(fullName: String) {
        sendTransactionViewModel.validateNameData(fullName)
    }

    private fun validateRecipientPhone(phone: String) {
        sendTransactionViewModel.validatePhoneData(phone)
    }

    private fun validateAmount(amount: String) {
        sendTransactionViewModel.validateAmountData(amount)
    }

    private fun getEndIconResId(validated: Boolean) =
        if (validated) R.drawable.ic_validate_success_24dp else R.drawable.ic_validate_error_24dp
    //endregion private methods
}