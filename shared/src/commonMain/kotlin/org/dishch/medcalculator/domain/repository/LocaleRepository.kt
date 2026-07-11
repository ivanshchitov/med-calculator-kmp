package org.dishch.medcalculator.domain.repository

interface LocaleRepository {
    fun getCurrentLanguageCode(): String
}
