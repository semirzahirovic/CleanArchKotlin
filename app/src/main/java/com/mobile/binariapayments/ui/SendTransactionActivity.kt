package com.mobile.binariapayments.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.binariapayments.R

class SendTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}