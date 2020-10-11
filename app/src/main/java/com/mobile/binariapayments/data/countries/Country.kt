package com.mobile.binariapayments.data.countries

import java.util.regex.Pattern

private const val KENYA_COUNTRY_NAME = "Kenya"
private const val KENYA_CURRENCY_SYMBOL = "KES"
private const val KENYA_PHONE_PREFIX = "+254"
private val KENYA_PHONE_PREFIX_PATTERN by lazy { Pattern.compile("^(?:00254|\\+254)(.*)") }
private val KENYA_PHONE_PATTERN by lazy { Pattern.compile("^(?:00254|\\+254)[0-9]{9}\$") }
private const val NIGERIA_COUNTRY_NAME = "Nigeria"
private const val NIGERIA_CURRENCY_SYMBOL = "NGN"
private const val NIGERIA_PHONE_PREFIX = "+234"
private val NIGERIA_PHONE_PREFIX_PATTERN by lazy { Pattern.compile("^(?:00234|\\+234)(.*)") }
private val NIGERIA_PHONE_PATTERN by lazy { Pattern.compile("^(?:00234|\\+234)[0-9]{7}\$") }
private const val TANZANIA_COUNTRY_NAME = "Tanzania"
private const val TANZANIA_CURRENCY_SYMBOL = "TZS"
private const val TANZANIA_PHONE_PREFIX = "+255"
private val TANZANIA_PHONE_PREFIX_PATTERN by lazy { Pattern.compile("^(?:00255|\\+255)(.*)") }
private val TANZANIA_PHONE_PATTERN by lazy { Pattern.compile("^(?:00255|\\+255)[0-9]{9}\$") }
private const val UGANDA_COUNTRY_NAME = "Uganda"
private const val UGANDA_CURRENCY_SYMBOL = "UGX"
private const val UGANDA_PHONE_PREFIX = "+256"
private val UGANDA_PHONE_PREFIX_PATTERN by lazy { Pattern.compile("^(?:00256|\\+256)(.*)") }
private val UGANDA_PHONE_PATTERN by lazy { Pattern.compile("^(?:00256|\\+256)[0-9]{7}\$") }

enum class Country(
    val countryName: String,
    val currencyCode: String,
    val phonePrefix: String,
    val prefixPattern: Pattern,
    val phonePattern: Pattern
) {

    KENYA(
        KENYA_COUNTRY_NAME,
        KENYA_CURRENCY_SYMBOL,
        KENYA_PHONE_PREFIX,
        KENYA_PHONE_PREFIX_PATTERN,
        KENYA_PHONE_PATTERN
    ),
    NIGERIA(
        NIGERIA_COUNTRY_NAME,
        NIGERIA_CURRENCY_SYMBOL,
        NIGERIA_PHONE_PREFIX,
        NIGERIA_PHONE_PREFIX_PATTERN,
        NIGERIA_PHONE_PATTERN
    ),
    TANZANIA(
        TANZANIA_COUNTRY_NAME,
        TANZANIA_CURRENCY_SYMBOL,
        TANZANIA_PHONE_PREFIX,
        TANZANIA_PHONE_PREFIX_PATTERN,
        TANZANIA_PHONE_PATTERN
    ),
    UGANDA(
        UGANDA_COUNTRY_NAME,
        UGANDA_CURRENCY_SYMBOL,
        UGANDA_PHONE_PREFIX,
        UGANDA_PHONE_PREFIX_PATTERN,
        UGANDA_PHONE_PATTERN
    );
}