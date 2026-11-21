package org.dishch.medcalculator

class WasmPlatform : Platform {
    override val name: String = "Web (WasmJS)"
}

actual fun getPlatform(): Platform = WasmPlatform()
