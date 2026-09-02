# 01 — Análisis de referencia funcional (clean-room)

**App analizada:** `volumebooster.soundspeaker.louder` v2.9.2.60 instalada en Samsung.

## Lo observado por ADB/dumpsys/package + strings

- Paquete: `volumebooster.soundspeaker.louder`
- Versión: `2.9.2.60`
- Servicios/export visible:
  - `volumebooster.soundspeaker.louder.binder.BinderDeathService`
  - `volumebooster.soundspeaker.louder.media.service.MediaControllerService`
  - `MediaControllerService` registrado como `NotificationListenerService`
- Permisos solicitados:
  - `android.permission.MODIFY_AUDIO_SETTINGS`
  - `android.permission.RECORD_AUDIO`
  - `android.permission.SYSTEM_ALERT_WINDOW`
  - `android.permission.SYSTEM_OVERLAY_WINDOW`
  - `android.permission.FOREGROUND_SERVICE`
  - `android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK`
  - `android.permission.POST_NOTIFICATIONS`
  - `android.permission.INTERNET`
  - ads/billing/AdServices
- Consulta/apps de interés: `com.spotify.music`

## Strings/recursos detectados en APK

Se encontraron nombres de recursos/UI relacionados a volumen/boost/equalizador:

- `tv_booster`
- `tv_booster_max`
- `tv_booster_min`
- `tv_booster_percent`
- `tv_booster_volume_max`
- `rb_volume_adjust`
- `rcv_volume_adjust`
- `rv_volume_adjust`
- `sb_volume`
- `progress_seekbar_volume`
- `progress_seekbar_float_volume`
- `selector_ic_volume`
- `selector_ic_button_volume`
- `iv_volume`
- `iv_volume_button`
- `iv_volume_decrease`
- `iv_volume_increase`
- `iv_volume_system`
- `tv_volume_system`
- `tv_volume_value`
- `tv_vol_booster`
- `tv_volume_title`
- `asb_system_volume`
- `super_bass`
- `tv_super_bass`
- `selector_eq_*`
- `tv_equalizer`

## Lectura funcional confirmada el 2026-09-01

El análisis estático y la inspección en ejecución confirmaron esta arquitectura, sin reutilizar implementación ajena:

1. App crea overlay/foreground UI.
2. Su `MediaControllerService` es un `NotificationListenerService`. Desde ahí observa la sesión de Spotify y muestra título, artista, portada y estado; no clona ni descarga temporalmente la canción.
3. Spotify continúa siendo el propietario de la reproducción y de su audio session ID.
4. El APK crea `DynamicsProcessing` con prioridad máxima y **audio session `0`**, es decir, intenta insertarlo sobre la mezcla global de salida.
5. La configuración observada incluye dos canales, diez bandas, pre-EQ, post-EQ, compresor multibanda y limiter. También existen rutas para `Equalizer`, `BassBoost`, `Virtualizer` y `Visualizer`.
6. La ganancia se modifica en tiempo real dentro de `DynamicsProcessing`; por ello puede afectar Spotify en dispositivos cuyo motor OEM todavía admita efectos insertados en la sesión global `0`.
7. La sensación de “boost” surge de:
   - volumen stream al máximo;
   - ganancia/compresión/ecualización global admitida por el motor OEM;
   - overlay/foreground que persuasivamente muestra porcentaje arriba de 100%.

## Lo que debemos replicar legal y clean-room

- Control real y medible del volumen del sistema (`STREAM_MUSIC`).
- Foreground service confiable.
- UI de boost propia.
- Player local opcional para boost claramente explícito dentro de nuestra app.
- Limitador/compresor para evitar clipping.
- Modo global experimental mediante APIs públicas `DynamicsProcessing` + sesión `0`, únicamente cuando una prueba de capacidad confirme que el dispositivo lo soporta.
- Lectura opcional de metadatos mediante Notification Listener con onboarding y explicación de privacidad.

## Lo que no copiamos

- Nombres de clases.
- Assets ni layouts.
- Textos exactos.
- Estructuras internas del APK.

## Riesgos/alertas

- Prometer “más del máximo” puede ser confuso: debemos decir “hasta límite seguro del dispositivo + amplificación percibida/local”.
- `RECORD_AUDIO` puede alarmar usuarios y Play policy; sólo usarlo si realmente aporta una función clara.
- Android documenta como obsoleto adjuntar efectos insert a la mezcla global mediante sesión `0`; puede fallar o no producir efecto en otros fabricantes/versiones.
- La app debe detectar soporte y ofrecer fallback al reproductor propio, sin prometer compatibilidad universal.
- Ads agresivos convierten poco y queman retención.
