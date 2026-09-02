# 02 — Spec clean-room de la app

## Nombre de trabajo
Provisional: **Pixora Volume Max**  
Paquete temporal: `com.pixora.volumemax`  
Cambiable antes de release/Play Console.

## Objetivo
Crear una app Android que mejore y controle el volumen percibido de audio en el dispositivo con enfoque transparente:

- “Volumen”: controla niveles reales del stream de música.
- “Boost”: amplificación percibida/local con efectos controlados y limiter.
- “EQ”: ecualizador básico.
- “Player”: player local para demostrar boost dentro de la app.

## Requerimientos funcionales MVP

1. Leer volumen actual de `STREAM_MUSIC`.
2. Leer volumen máximo de `STREAM_MUSIC`.
3. Cambiar volumen usando `AudioManager`.
4. Mostrar volumen en % real (0–100%).
5. Botón Boost con 3 presets: Leve / Fuerte / Máximo.
6. Toast/alerta de seguridad antes de boost agresivo.
7. Foreground service para “boost activo”.
8. Player local con una canción de prueba o selector de archivos.
9. EQ básico con 5 bandas si la sesión propia lo permite.

## UI propuesta

Pantalla principal:
- Hero slider circular o vertical.
- Estado: `Volumen actual: 74%`.
- Botones rápidos: `50`, `75`, `100`.
- Tarjeta Boost: `Amplificación percibida: OFF / +3dB / +6dB / +9dB`.
- Toggle: `Limiter anti-clipping` activado.
- Tarjeta Spotify/externa: “Detectando reproducción externa”.

Pantalla EQ:
- 5 sliders verticales.
- Presets: Flat, Bass +, Vocal, Night, Custom.

Pantalla Player:
- Lista local básica.
- Reproducir/pausar/siguiente.
- Info: sesión de audio activa.

Settings:
- Recordatorio de seguridad auditiva.
- Preferencia de start-at-boot opcional.
- Privacy.

## Modelo de datos mínimo

```kotlin
data class BoostState(
    val streamVolumeIndex: Int,
    val streamVolumeMax: Int,
    val volumePercent: Int,
    val boostMb: Int,
    val limiterEnabled: Boolean,
    val eqEnabled: Boolean,
    val foregroundActive: Boolean
)
```

## Clases propuestas

- `MainActivity`
- `VolumeViewModel`
- `VolumeController`
- `BoostEngine`
- `AudioEffectsEngine`
- `LocalPlayerController`
- `ForegroundBoostService`
- `SafetyWarnings`
- `PrefsStore`

## Reglas de seguridad

- No decir histórico que se salta el límite físico; usar “máximo permitido por Android/dispositivo”.
- Limitar boost inicial a +3dB; confirmación para +6dB+.
- Mostrar advertencia si usuario habilita boost sin limiter.
- No activar RECORD_AUDIO por default.

## Monetización limpia

- Free: boost bajo + EQ limitado + banner pequeño.
- Pro one-time o suscripción barata: presets, bass+, automatic max, temas, sin ads.
- No interstitial al oprimir botones de volumen.

## Criterio de éxito MVP

- Instala en Samsung.
- Slider cambia volumen real del sistema.
- Player propio suena y puede aplicar gain.
- Foreground service sobrevive 30 min con pantalla apagada.
- Sin crash en Android 13/14.
