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

## 2026-09-02 — Thunder Deck seleccionado y controles reales

- El propietario seleccionó oficialmente el concepto 05 `Thunder Deck`.
- Se añadió `ExternalMediaControls`, basado en `MediaSessionManager`, para ejecutar anterior, play/pausa y siguiente sobre la sesión multimedia activa autorizada.
- `SAFE` refleja si el limitador global está activo. `EQ` permanece claramente marcado como próxima fase y sólo informa que aún no está conectado; no simula procesamiento.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL** (40 tareas).
- APK instalada y abierta en Samsung SM-A155M sin excepción `AndroidRuntime` observada.
- Acceso a notificaciones confirmado para `com.pixora.volumemax/.MediaObserverService`.
- No había una sesión musical activa durante la inspección de `dumpsys media_session`; los comandos externos requieren QA con Spotify reproduciendo una canción.
- Huawei VNS-L53: instalación, arranque, render 1080×1920 y scroll aprobados sin crash observado.
- Huawei rechazó correctamente el modo global por API inferior a 28; la app liberó el efecto, detuvo el servicio y mostró el fallback al reproductor local.
- Sin acceso multimedia ni sesión activa, los controles externos mostraron una explicación y no simularon una acción.
- Evidencia visual guardada en `docs/qa/2026-09-02-huawei/`.

## 2026-09-02 — Chasis Thunder Deck funcional

- Se reemplazó el formulario transitorio por el chasis responsive Thunder Deck construido con componentes Android reales.
- La pantalla superior muestra el estado global y los metadatos externos; los controles anterior, play/pausa y siguiente conservan su conexión a `ExternalMediaControls`.
- La perilla conserva el control táctil 0–200% y el gradiente frío→caliente. Dos altavoces dibujados por código reflejan visualmente la potencia elegida; no afirman capturar el audio.
- Las palancas visibles 100%, 150% y 200% están conectadas a presets reales. SAFE refleja el limitador y EQ continúa deshabilitado informativamente.
- El volumen del sistema y el reproductor local permanecen accesibles en la parte inferior mediante desplazamiento.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL** (40 tareas) con APK debug generado.
- Huawei VNS-L53: instalación `Success`, arranque de `MainActivity`, scroll y selección 200% aprobados; sin `FATAL EXCEPTION` observada. Al no soportar la ruta global, mantiene el estado apagado/fallback de forma segura.
- Samsung SM-A155M: APK instalada `Success`, pero el dispositivo estaba con pantalla bloqueada y batería al 6%; no se forzó una prueba interactiva adicional.
- Evidencia de esta iteración: `docs/qa/2026-09-02-thunder-deck/`.

## 2026-09-02 — Thunder Run visual, texto mínimo

- La comparación directa con el concepto 05 mostró exceso de textos, bocinas pequeñas y una composición todavía similar a formulario.
- Se reconstruyó la jerarquía como estéreo: torres laterales altas con tweeter, dos woofers y ventilas; display superior con barras de color; perilla central metálica; transporte, SAFE, acceso multimedia, EQ y tres palancas compactas.
- Se retiraron de la vista principal los párrafos explicativos, títulos repetidos y botones con frases. El texto visible queda limitado a marca, estado/porcentaje, metadatos musicales y valores indispensables.
- El visualizador de barras y la iluminación de bocinas responden al nivel seleccionado, pero son respuesta visual, no medición de la señal de audio.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL** (40 tareas). APK instalada en Huawei y pantalla principal inspeccionada sin crash observado.
- Evidencia: `docs/qa/2026-09-02-thunder-run-v2/`.

## 2026-09-02 — Thunder Shell v3 con piezas raster

- Se tomó una captura nueva del APK y se comparó con el concepto 05. El faltante principal era material: los paneles seguían siendo geometría plana sin texturas, tornillos, malla ni profundidad.
- Se generó una carcasa original anime-realista 9:16 mediante ImageGen: dos torres, pantalla hundida, metal grafito, tornillería, conos con malla, perilla LED y tres palancas.
- El asset quedó versionado en `drawable-nodpi/thunder_run_stereo_shell_v3.png`; prompt/procedencia en `docs/design_review/THUNDER_SHELL_V3_ASSET.md`.
- La carcasa es visual. Perilla, anterior/siguiente, acceso multimedia, play/pausa, SAFE y palancas 100/150/200 continúan siendo controles Android superpuestos.
- `uiautomator` confirmó límites táctiles. La palanca roja 200% abrió correctamente la advertencia auditiva.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL** (40 tareas). Instalación Huawei: `Success`; sin excepción fatal observada.
- Comparación y evidencia: `docs/qa/2026-09-02-thunder-shell-v3/`.

## 2026-09-02 — QA Samsung y reconciliación de estado

- Samsung SM-A155M (`RF8X903KZ3K`), 1080×2340, densidad 450: instalación `Success`, actividad principal reanudada y Thunder Shell v3 renderizado completo.
- La pantalla alta muestra el estéreo, bandeja local y ganancias sin recortar controles; los límites de `uiautomator` confirmaron perilla, transporte, multimedia y palancas.
- Se detectó un estado visual obsoleto de 136% después de reinstalar: la preferencia persistía aunque `GlobalBoostService` ya no existía. Se corrigió reconciliando la preferencia con `GlobalBoostService.isRunning` dentro del mismo proceso; la bandera sólo queda activa después de aplicar correctamente el efecto y vuelve a falso al apagar o fallar.
- Regresión verificada: después de reinstalar/forzar cierre, la UI inició en `OFF` y no había `GlobalBoostService` activo.
- Activación real 150%: UI `150%`, `DynamicsProcessing enabled; channels=2`, ganancia `3.521825 dB`, servicio foreground activo y SAFE activo.
- 100% liberó previamente `DynamicsProcessing`; 200% también se verificó con +6 dB durante la exploración inicial.
- El acceso de notificaciones está concedido a `MediaObserverService`. Spotify figura como último receptor multimedia, pero no existía una sesión activa para probar metadatos/transporte en este pase.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL** (40 tareas). Sin `FATAL EXCEPTION` observada.
- Evidencia: `docs/qa/2026-09-02-samsung-thunder-shell-v3/`.

## 2026-09-02 — Rotor animado y controles metálicos

- Se corrigió la silueta fantasma de la perilla separando el asset en carcasa v4 sin rotor y sprite PNG transparente.
- `knobSprite` rota realmente según la previsualización táctil 0–200% y anima al seleccionar un preset; el aro LED y el chasis no giran.
- Se eliminó el arco gráfico superpuesto de `BoostDialView`; mantiene entrada táctil, porcentaje y accesibilidad.
- SAFE se redujo y centró dentro de su disco. El acceso multimedia ahora es un botón metálico cian integrado en el control circular izquierdo del estéreo.
- Botones de archivo, reproducción local y ganancias sustituyeron el tinte morado Material por selectores metálicos propios con respuesta presionada.
- Samsung: capturas 0/100% y grabación de la rotación guardadas. El rotor cambió de ángulo de forma centrada, sin crash observado.
- `testDebugUnitTest assembleDebug`: **BUILD SUCCESSFUL** (40 tareas).
- Evidencia: `docs/qa/2026-09-02-rotor-and-controls/`.
