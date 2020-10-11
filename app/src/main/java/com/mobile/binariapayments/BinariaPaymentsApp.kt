package com.mobile.binariapayments

import android.app.Application
import com.mobile.binariapayments.di.dataModule
import com.mobile.binariapayments.di.domainModule
import com.mobile.binariapayments.di.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BinariaPaymentsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BinariaPaymentsApp)
            modules(listOf(dataModule, domainModule, viewmodelModule))
        }
    }
}