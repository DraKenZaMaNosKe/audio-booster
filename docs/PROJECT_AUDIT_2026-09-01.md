# Auditoría técnica inicial — 2026-09-01

## Veredicto

La base es un prototipo Android válido, no una app de audio booster terminada. Genera un APK debug y puede modificar el volumen multimedia dentro del rango legal del dispositivo. La interfaz muestra estados de boost que todavía no corresponden a procesamiento real de audio.

## Evidencia revisada

- Proyecto Kotlin/XML con `minSdk 24`, `targetSdk 35` y package provisional `com.pixora.volumemax`.
- `VolumeController` usa `AudioManager` para leer y escribir `STREAM_MUSIC`.
- `LocalAudioBoost` encapsula `LoudnessEnhancer`, pero ninguna pantalla crea o conecta un `MediaPlayer`.
- `BoostForegroundService` sólo publica una notificación; no posee ni aplica un motor de boost.
- `switchLimiter` no tiene listener ni implementación DSP.
- No existen EQ, player local, persistencia, pruebas unitarias o instrumentadas.
- No había repositorio Git ni Gradle Wrapper al iniciar la auditoría.

## Riesgos críticos

1. **Promesa técnica:** una app Android normal no puede garantizar ganancia global sobre la salida de Spotify u otras apps. El volumen del sistema llega hasta el máximo del fabricante. Los efectos fiables deben pertenecer a una sesión controlada por nuestra app o depender de capacidades específicas del dispositivo.
2. **Producto engañoso:** mostrar `+3 dB` o `+6 dB` sin aplicar ganancia medible debe eliminarse o etiquetarse como no disponible.
3. **Foreground service:** declarar `mediaPlayback` sin reproducir medios ni ejecutar una función perceptible crea riesgo técnico y de revisión en Play.
4. **Seguridad auditiva:** no basta un diálogo. Se requieren límites conservadores, estado inicial seguro, texto transparente y pruebas con bocina/audífonos.
5. **Toolchain:** AGP 8.5.2 advierte que fue probado sólo hasta `compileSdk 34`, mientras el proyecto compila con 35.
6. **Reproducibilidad:** el entorno local tenía Android Studio, pero Java y Gradle no estaban en `PATH`; el repositorio debe incluir wrapper y comandos autocontenidos.

## Qué sí podemos convertir en producto real

- Control claro y cómodo del volumen multimedia real.
- Reproductor local basado en Media3 con sesión propia.
- Ganancia mediante `LoudnessEnhancer` o DSP propio únicamente sobre esa sesión.
- EQ y protección anti-clipping comprobables en el player propio.
- Presets y accesos rápidos que no prometan modificar audio ajeno.
- Compatibilidad experimental OEM sólo si se detecta y prueba en ejecución.

## No prometer

- Saltarse el límite físico del teléfono.
- Amplificar cualquier aplicación de terceros en todos los dispositivos.
- “Limiter activo” si no existe procesamiento de señal detrás del control.
- Persistencia del boost cuando sólo persiste una notificación.

## Criterios antes de Play Store

- Pruebas automatizadas y lint sin errores.
- QA real en Samsung SM-A155M y Huawei VNS-L53.
- Matriz con bocina, audífonos, Bluetooth, pantalla apagada y reproducción externa/local.
- Política de privacidad y Data Safety coherentes con permisos y SDKs reales.
- Ficha de Play transparente y sin afirmaciones no demostradas.
- AAB firmado con upload key dedicada, secretos fuera de Git y Drive compartido.
- Closed testing, revisión de crashes/ANR y evidencia de funcionamiento.
