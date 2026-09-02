# 04 — Log de pruebas

## 2026-09-01 — Fase análisis

- Samsung conectado por ADB: sí, comando `adb devices -l` mostró `SM_A155M`.
- Paquete identificado: `volumebooster.soundspeaker.louder`.
- APK extraído a workspace local para análisis offline.
- Documentación creada en Drive compartida.

Pendiente:
- Refinar análisis bytecode con herramienta de decompilado si se instala `jadx`.
- Verificar ejecución en vivo y compare `dumpsys audio` con app activa/inactiva.

## 2026-09-01 — Auditoría Codex

- Git local al iniciar: no existía carpeta `.git`.
- Búsqueda GitHub en la cuenta configurada: sin repositorio coincidente por nombre o código.
- Dispositivos ADB detectados: Huawei VNS-L53 y Samsung SM-A155M.
- Toolchain encontrada: JDK incluido con Android Studio; Gradle 8.13 disponible en caché, pero sin wrapper en el proyecto.
- `assembleDebug`: alcanzó `packageDebug` y generó `app/build/outputs/apk/debug/app-debug.apk`.
- Compilación Kotlin: completada.
- `lintDebug`: inició análisis pero se detuvo después de varios minutos sin terminar; debe repetirse aisladamente.
- Advertencia: AGP 8.5.2 no declara compatibilidad probada con `compileSdk 35`.
- Prueba funcional en dispositivo: pendiente; un APK generado no demuestra boost real.

## 2026-09-01 — Base reproducible

- Gradle Wrapper 8.9: generado correctamente.
- Matriz configurada: AGP 8.7.3, Kotlin 2.0.21, Java 17, compile/target SDK 35.
- Tests unitarios `VolumeMathTest`: añadidos.
- Validación con el wrapper actualizado: bloqueada durante la descarga de `bundletool 1.17.1` desde Google Maven; se canceló después de varios minutos sin progreso.
- Estado honesto: la configuración nueva y sus tests deben volver a ejecutarse cuando Google Maven responda; no se consideran aprobados todavía.

## 2026-09-01 — MVP de reproducción local con ganancia real

- Se eliminó la UI de boost global ficticio, el limiter sin implementación y el foreground service que sólo mostraba una notificación.
- Se integró Media3 ExoPlayer 1.9.2 y selección de audio mediante Storage Access Framework.
- `SessionGainController` conecta `LoudnessEnhancer` exclusivamente a la sesión del reproductor propio, limitado a 0, +3 y +6 dB.
- Permisos eliminados: notificaciones y foreground service. Permanece únicamente `MODIFY_AUDIO_SETTINGS` para el control del volumen multimedia.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL**, 40 tareas, tests aprobados y APK generado.
- APK instalado en Samsung SM-A155M mediante ADB: `Success`.
- Arranque frío de `com.pixora.volumemax/.MainActivity`: `Status: ok`, 1.528 s, sin excepción `AndroidRuntime` en el registro inspeccionado.
- `lintDebug`: llegó a `lintAnalyzeDebug` pero no concluyó tras varios minutos sin salida; se canceló y continúa pendiente.
- Advertencia controlada: AGP 8.5.2 sólo declara pruebas hasta SDK 34 aunque el proyecto compila con SDK 35. No se suprimió la advertencia.
- Pendiente de QA manual: seleccionar una pista, comparar 0/+3/+6 dB, comprobar pausa/reanudación y medir clipping en Samsung y Huawei.

## 2026-09-01 — Modo global experimental y metadatos

- Análisis clean-room de EZ Booster confirmó que Spotify conserva la reproducción; la app refleja metadatos con `NotificationListenerService`/`MediaController` y crea un `DynamicsProcessing` sobre la sesión global 0.
- Se implementó una cadena propia, sin copiar configuración o recursos: prioridad máxima, entrada hasta +6 dB y limitador a -0.5 dB.
- APK instalada en Samsung SM-A155M. Logs: `Global DynamicsProcessing enabled; channels=2` y `Global input gain applied: 6.0dB`.
- `dumpsys media.audio_flinger` mostró un efecto `DynamicsProcessing` habilitado en sesión 0, con cliente perteneciente a `com.pixora.volumemax`, control y prioridad 2147483647.
- Se forzó el cierre de EZ Booster durante la prueba para evitar atribuirle nuestra cadena.
- Observador multimedia implementado con ruta compatible antes/después de API 33. El acceso especial sigue sin concederse a nuestra app y debe habilitarlo explícitamente el usuario.
- Dial Orbix y arte de fondo originales integrados.
- Validación final de esta iteración: `testDebugUnitTest assembleDebug` terminó en **BUILD SUCCESSFUL** (40 tareas); APK reinstalada y actividad principal reanudada sin excepción `AndroidRuntime` observada.
- Pendiente: comparación audible Spotify 100/125/150/200%, validación de metadatos tras autorizar acceso y medición de clipping. La presencia de la cadena no sustituye estas pruebas.
- Revisión Orbix Guardian: build/tests e instalación aprobados; `lintDebug` volvió a quedar detenido en `lintAnalyzeDebug` y se canceló tras ~90 s. Se resolvieron antes del commit sus hallazgos de ciclo de vida (`START_NOT_STICKY`), advertencia global, permiso de notificaciones bajo demanda y visualización de la app de origen.

## 2026-09-01 — Identidad y pulido visual

- Validación del usuario en Samsung: el incremento de volumen funciona y la calidad percibida fue calificada como muy buena. Esta evidencia es auditiva subjetiva; las mediciones de clipping/distorsión siguen pendientes.
- Nombre visible actualizado a `AudioBooster` con firma `BY PIXORA IA`.
- Splash nativo e icono vectorial original integrados.
- Menú desplegable personalizado para navegación rápida entre secciones.
- Perilla mejorada con progresión térmica azul/violeta/naranja/rojo y pulso luminoso al confirmar.
- Botones y etiquetas unificados con formas, colores y áreas táctiles Material personalizadas.
- Se corrigió el scroll automático inicial que ocultaba la cabecera y el contenido bajo la barra de estado.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL**; APK instalada y abierta en Samsung sin excepción `AndroidRuntime` observada.
