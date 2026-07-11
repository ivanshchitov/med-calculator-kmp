package org.dishch.medcalculator.data.repository

import org.dishch.medcalculator.data.local.LocaleHelper
import org.dishch.medcalculator.domain.repository.LocaleRepository

class LocaleRepositoryImpl : LocaleRepository {

    override fun getCurrentLanguageCode(): String = LocaleHelper.getSystemLanguageCode()
}