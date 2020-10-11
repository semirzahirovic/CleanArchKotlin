package com.mobile.binariapayments.di

import com.mobile.binariapayments.viewmodel.SendTransactionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel { SendTransactionViewModel(get(), get()) }
}