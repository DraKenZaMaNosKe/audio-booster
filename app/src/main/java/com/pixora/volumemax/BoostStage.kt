package com.pixora.volumemax

enum class BoostStage(val label: String, val estimatedDb: Int, val minVolumePercent: Int) {
    OFF("Off", 0, 0),
    LOW("Leve", 3, 80),
    HIGH("Fuerte", 6, 100);

    companion object {
        fun fromProgress(progress: Int): BoostStage = when {
            progress <= 10 -> OFF
            progress <= 65 -> LOW
            else -> HIGH
        }
    }
}
