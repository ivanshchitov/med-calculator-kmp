package org.dishch.medcalculator.updates

sealed interface UpdateState {
    data object Idle : UpdateState
    data object Available : UpdateState

    data class Downloading(
        val bytesDownloaded: Long,
        val totalBytes: Long
    ) : UpdateState {
        val progress: Float?
            get() = if (totalBytes > 0) {
                (bytesDownloaded.toFloat() / totalBytes).coerceIn(0f, 1f)
            } else {
                null
            }
    }

    data object ReadyToInstall : UpdateState
    data object Installing : UpdateState
}
