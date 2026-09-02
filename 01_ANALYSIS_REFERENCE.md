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

## Lectura funcional preliminar

**No** se asumió el código como una lección copiable; se extrajo una hipótesis de arquitectura:

1. App crea overlay/foreground UI.
2. Lee estado de reproducción/notificaciones, posiblemente para mostrar canción actual y no depender de player propio.
3. Usa `AudioManager` para tocar `STREAM_MUSIC` y/o `STREAM_SYSTEM`.
4. Aplica efectos de audio en sesión activa: probablemente `AudioEffect`/Equalizer/LoudnessEnhancer/bass/virtualizer.
5. La sensación de “boost” surge de:
   - volumen stream al máximo;
   - ganancia adicional/equalizer en frecuencias perceptibles;
   - overlay/foreground que persuasivamente muestra porcentaje arriba de 100%.

## Lo que debemos replicar legal y clean-room

- Control real y medible del volumen del sistema (`STREAM_MUSIC`).
- Foreground service confiable.
- UI de boost propia.
- Player local opcional para boost claramente explícito dentro de nuestra app.
- Limitador/compresor para evitar clipping.

## Lo que no copiamos

- Nombres de clases.
- Assets ni layouts.
- Textos exactos.
- Estructuras internas del APK.

## Riesgos/alertas

- Prometer “más del máximo” puede ser confuso: debemos decir “hasta límite seguro del dispositivo + amplificación percibida/local”.
- `RECORD_AUDIO` puede alarmar usuarios y Play policy; sólo usarlo si realmente aporta una función clara.
- Ads agresivos convierten poco y queman retención.
