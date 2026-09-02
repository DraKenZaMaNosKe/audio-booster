# Proyecto Audio Booster — Proceso completo desde análisis hasta lanzamiento

**Fecha:** 2026-09-01  
**Alcance:** Crear una app Android propia, clean-room, funcionalmente inspirada en el comportamiento de apps tipo volume booster, con look/feel, nombre, assets y mejoras propias.

## Reglas del proyecto
- No copiar código, assets, textos, layouts, nombres de clases ni recursos de terceros.
- Sí usar hallazgos funcionales públicos/esperables: control de volumen del sistema, reproductor/enhancer, ecualizador, foreground media playback.
- Documentar cada fase con decisiones, pruebas y salida.

## Fase 0 — Hallazgos del análisis previo
App analizada: paquete instalado `volumebooster.soundspeaker.louder` v2.9.2.60.

Evidencia colectada vía ADB/dumpsys/strings:
- Servicios propios: `binder.BinderDeathService` y `media.service.MediaControllerService`.
- `MediaControllerService` aparece como `NotificationListenerService`.
- Permisos solicitados: `MODIFY_AUDIO_SETTINGS`, `RECORD_AUDIO`, `SYSTEM_ALERT_WINDOW`, `SYSTEM_OVERLAY_WINDOW`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK`, `RECORD_AUDIO`, etc.
- Queries hacia `com.spotify.music`.
- Strings/UI recursos relacionados con volumen: `tv_booster`, `tv_booster_max`, `tv_booster_percent`, `tv_volume_system`, `tv_volume_value`, `progress_seekbar_volume`, `selector_ic_volume`, `iv_volume_decrease`, `iv_volume_increase`, `super_bass`, `equalizer`, `volume_adjust`.
- Hipótesis principal: la app probablemente combina control de volumen de streams Android con efectos de audio/enhancer y una UI overlay/foreground para operar mientras otra app reproduce música, por ejemplo Spotify.

No se debe presentar como copia; la conclusión funcional es: el usuario puede subir el stream de música/sistema y/o aplicar ganancia en una sesión de audio controlada por la app.

## Fase 1 — Objetivo producto
Nombre tentativo pendiente. Candidate branding: buscar nombre simple, memorable, sin marca registrada aparente.

Propuesta de valor:
- Boost + volumen del sistema multimedia.
- Ganancia/exagerado pero con limiter anti-clip.
- Ecualizador básico.
- Bass boost/virtualizer si es viable.
- Foreground service + quick controls.
- Look propio moderno, no clon visual.
- Monetización razonable: banner/no intrusivo, interstitial solo en cambios de sección, rewarded para presets premium, suscripción opcional.

## Fase 2 — Arquitectura clean-room
Módulos/function:

1. **MainActivity/Home**
   - Slider principal de volumen del stream.
   - Switch de boost.
   - Botones rápidos: +10%, +50%, +100% personalizados.
   - Mini player si se reproduce local audio.

2. **BoostEngine**
   - Obtén `AudioManager`.
   - Lee `getStreamVolume` y `getStreamMaxVolume`.
   - Aplica `setStreamVolume` limitado a máximo legal.
   - Calcula boost UI 0..200% como capa UX, sin falsificar hardware real.
   - Internamente registra estado actual y aplica efectos en sesión activa.

3. **AudioEffectsEngine**
   - `LoudnessEnhancer` por `audioSessionId` en reproducción propia.
   - `Equalizer` para bandas.
   - `Validator`: limitador/limiter vía `DynamicsProcessing` donde sea soportado.

4. **PlayerLocal**
   - `ExoPlayer/Media3` para local audio y pruebas.
   - Sesión propia para aplicar enhancer sin depender de audio de terceros.

5. **Foreground Media Service**
   - `MediaSessionService` o servicio foreground con notificación.
   - Mantener boost activo con pantalla apagada.
   - Botones pausa/stop/back.

6. **Overlay/Floating Mode** (opcional)
   - Solo si beneficio real; evitar abuso por ads.

## Fase 3 — Permisos mínimos MVP
- `MODIFY_AUDIO_SETTINGS` para control de volumen.
- `FOREGROUND_SERVICE` y `FOREGROUND_SERVICE_MEDIA_PLAYBACK` si hay reproducción/servicio.
- `POST_NOTIFICATIONS` Android 13+.
- `READ_MEDIA_AUDIO` si se listan canciones locales.
- `RECORD_AUDIO` solo si realmente se necesita visualizer/capture; evitar pedirlo en MVP si no aporta valor claro.

## Fase 4 — Diseño UX/UI
Look propio: dark modern, neón controlado, cards grandes, slider circular/vertical, fondos degradados, iconografía lineal propia.

Pantallas:
- Splash (sin sobrecarga)
- Onboarding 3 pasos: Boost, EQ, Background
- Home Boost
- EQ
- Settings/Limit warning
- Upgrade/Pro opcional

## Fase 5 — Pruebas
1. Unit tests del mapper 0..100 UI a índice real de stream.
2. Instrumentación: foreground service inicia/para.
3. Manual: volumen real cambia usando botones y slider.
4. Spotify background: validar comportamiento cuando suena otra app y usuario mueve slider.
5. Limiter: subir mucho boost no cruje/clip.
6. Batería/Doze: servicio foreground no drena demasiado.

## Fase 6 — Build/distribución
- Versionado: `1.0.0-cleanroom-debug`.
- APK debug en carpeta compartida `releases/`.
- Cada build con notas, hash SHA-256, fecha y cambios.

## Pendientes inmediatos
- Definir nombre final.
- Crear skeleton Kotlin + Gradle.
- Implementar `MainActivity` + `VolumeControlManager` mínimo.
- Probar build debug.
