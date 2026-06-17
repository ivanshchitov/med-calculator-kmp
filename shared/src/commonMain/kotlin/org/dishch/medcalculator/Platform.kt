package org.dishch.medcalculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform