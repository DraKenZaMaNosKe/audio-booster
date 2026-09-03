# Contexto AudioBooster

Última actualización: 2026-09-02  
Proyecto: **AudioBooster by Pixora IA**  
Repositorio canónico: <https://github.com/DraKenZaMaNosKe/audio-booster>  
Rama de trabajo: `main`  
Ruta original Windows: `D:\Orbix\audio_booster`  
Espejo Drive: `G:\Mi unidad\pixoraIA_admin\claude_compartido\audio_booster`

## Cómo retomar desde otra computadora

1. Clonar el repositorio y entrar en la rama `main`.
2. Leer este archivo, `README.md`, `docs/PRODUCT_REQUIREMENTS.md`, `docs/ROADMAP.md` y `04_TEST_LOG.md`.
3. Ejecutar `git status --short --branch` y confirmar que está limpio/alineado con `origin/main` antes de editar.
4. Configurar Android SDK/JDK local sin versionar `local.properties`.
5. Ejecutar `gradlew.bat testDebugUnitTest assembleDebug` en Windows.
6. Continuar desde **Siguiente paso exacto** al final de este documento.

## Objetivo del producto

Aplicación Android para controlar el volumen multimedia normal y ofrecer amplificación real por encima del 100% cuando el dispositivo admita efectos globales. Debe funcionar como producto Play Store, con identidad Pixora IA, controles honestos, advertencias auditivas, fallback local y documentación verificable.

## Estado funcional confirmado

- Volumen multimedia normal 0–100% mediante `AudioManager`.
- Boost global experimental 101–200% mediante `DynamicsProcessing` sobre sesión global 0, prioridad máxima, entrada hasta +6 dB y limitador propio.
- Foreground service `specialUse` visible mientras el efecto global está activo.
- Liberación del efecto al apagar/detener servicio; servicio `START_NOT_STICKY`.
- Persistencia visual básica del porcentaje global entre recreaciones normales de la actividad.
- Reproductor local Media3/ExoPlayer con selección SAF y `LoudnessEnhancer` de 0, +3 y +6 dB sobre su propia sesión.
- Observador opcional de metadatos mediante `NotificationListenerService` y `MediaController`; no captura, copia ni descarga audio externo.
- Controles externos reales mediante `MediaSessionManager`: anterior, play/pausa y siguiente. Priorizan la sesión `STATE_PLAYING` y después una sesión pausada disponible.
- `SAFE` refleja si el modo global/limitador está activo.
- `EQ` está marcado como `Próxima fase` y no finge procesamiento todavía.
- Advertencia antes de superar 100%.

## Evidencia Samsung

Dispositivo probado: Samsung SM-A155M, serial usado `RF8X903KZ3K`.

QA Thunder Shell v3 (2026-09-02): render 1080×2340 aprobado. Arranque fresco reconciliado en `OFF`; preset 150% confirmó servicio foreground, `DynamicsProcessing` global y +3.521825 dB. Capturas en `docs/qa/2026-09-02-samsung-thunder-shell-v3/`. Falta repetir controles/metadatos con Spotify reproduciendo una sesión activa.

- Instalación, arranque y build aprobados.
- El usuario confirmó de forma auditiva que el incremento funciona y que la calidad percibida es muy buena.
- Logs confirmaron `Global DynamicsProcessing enabled; channels=2` y ganancia global +6 dB.
- `dumpsys media.audio_flinger` mostró `DynamicsProcessing` habilitado en sesión 0, controlado por `com.pixora.volumemax`, prioridad `2147483647`.
- EZ Booster se cerró durante la prueba para no atribuirle nuestra cadena.
- El acceso de notificaciones llegó a estar autorizado para `MediaObserverService`.
- Pendiente: medición formal de clipping/distorsión y QA completo de botones con Spotify activo.

## Evidencia Huawei

Dispositivo: Huawei VNS-L53, serial ADB `G2R4C17516000149`.

- APK instalada y actividad abierta sin crash observado.
- Layout, barras del sistema y scroll verificados a 1080×1920.
- El teléfono usa una API anterior a 28: rechazó correctamente el boost global, liberó el efecto, no dejó servicio residual y mostró el fallback local.
- Sin acceso de notificaciones/sesión multimedia, los botones externos mostraron una explicación y no simularon una acción.
- Capturas: `docs/qa/2026-09-02-huawei/`.

## Identidad visual y decisión aprobada

- Nombre visible: **AudioBooster**.
- Firma: **BY PIXORA IA**.
- Splash nativo e icono adaptativo implementados.
- Se generaron cinco conceptos clean-room en `docs/design_review/stereo_concepts/`.
- Galería: `docs/design_review/stereo_concepts.html`.
- Diseño seleccionado por el propietario: **05 — Thunder Deck**.
- Especificación: `docs/design_review/SELECTED_DESIGN.md`.

Thunder Deck ya es una interfaz Android real, no una imagen con zonas táctiles invisibles:

- Pantalla: estado global y metadatos de reproducción externa disponibles.
- Perilla turbina: volumen/boost 0–200%, azul → violeta → naranja → rojo.
- Botones físicos: anterior, play/pausa y siguiente.
- Palancas reales: presets inicialmente 100%, 150% y 200%.
- `SAFE`: estado verdadero del limitador.
- `EQ`: panel real cuando el procesamiento esté conectado.

Revisión visual 2026-09-02: después de Thunder Run v2 se confirmó que faltaban imágenes/materiales realistas. **Thunder Shell v3** integra una carcasa raster original con torres, malla, conos, metal, tornillos, display, perilla LED y palancas. Los controles Android reales están alineados encima; la imagen no contiene zonas funcionales por sí sola.

Revisión rotor/controles: Thunder Shell v4 separa la turbina en `thunder_volume_rotor_v1.png`. El sprite gira 0–540° con el gesto/preset; la cavidad y el aro LED permanecen fijos. SAFE cabe en su disco, el acceso multimedia forma parte del panel físico y los controles inferiores usan metal propio en lugar del morado predeterminado.

Ambientación visual: `thunder_room_cabinet_v1.png` aporta la sala nocturna y la consola de nogal con vinilos como capa separada. El estéreo y sus controles reales permanecen encima, escalados juntos al 90%, para que el aparato esté apoyado en el mueble sin convertir la interfaz en una sola imagen sin interacción.

Pantalla informativa: el display del estéreo conserva el ecualizador y ahora presenta título, artista/fuente, volumen multimedia real, boost seleccionado y SAFE/LIMIT. Sin sesión activa muestra un estado breve. La interfaz reserva `adBannerSlot` debajo de los controles; todavía no existe SDK AdMob, consentimiento ni IDs reales. Monetización y Google Sign-In se implementarán después de cerrar el look and feel.

Capas v5: `thunder_run_stereo_shell_v5.png` es una carcasa con exterior transparente, canal LED apagado y cavidad sin rotor. El fondo, chasis, LEDs y rotor son capas separadas. La perilla captura el puntero, usa delta angular relativo y bloquea el desplazamiento del contenedor hasta `UP/CANCEL`; el foco se muestra mediante halo cian y escala sutil. Las flechas blancas fueron sustituidas por controles metálicos cian. El siguiente detalle vivo propuesto es separar conos y rejillas para animar graves/medios/agudos con medición real cuando sea posible.

QA Samsung de capas v5: el límite de la perilla permaneció en `[329,808][750,1229]` antes y después de un gesto 0→200. Capturas y video están en `docs/qa/2026-09-03-layered-shell/`. El prompt, fuente verde y procedimiento están en `docs/design_review/THUNDER_LAYERED_V5_ASSET.md`.

Escenario fijo: `mainStage` reemplaza al contenedor desplazable. Arrastrar cualquier parte del fondo o chasis no mueve el estéreo. La bandeja local, `+`, play local, ganancias y marcador AD están ocultos de la pantalla principal; su código se conserva fuera de la experiencia visual para fases secundarias/monetización.

La interfaz actual ya usa el chasis Thunder Deck con perilla, fondo y controles funcionales. Permanecen pendientes el refinamiento adaptativo final, la accesibilidad completa y un visualizador basado en medición de audio autorizada.

## Arquitectura y archivos importantes

- `app/src/main/java/com/pixora/volumemax/MainActivity.kt`: UI y coordinación.
- `GlobalAudioBoostController.kt`: `DynamicsProcessing`, ganancia y limitador global.
- `GlobalBoostService.kt`: ciclo de vida foreground del boost.
- `SessionGainController.kt`: ganancia del reproductor local.
- `ExternalMediaControls.kt`: transporte real de sesiones externas.
- `MediaObserverService.kt`: metadatos de notificaciones multimedia.
- `BoostDialView.kt`: perilla interactiva con progresión térmica.
- `app/src/main/res/layout/activity_main.xml`: chasis Thunder Deck funcional y desplazable.
- `app/src/main/res/drawable-nodpi/thunder_run_stereo_shell_v3.png`: carcasa visual original del estéreo.
- `app/src/main/res/drawable-nodpi/thunder_run_stereo_shell_v4.png`: carcasa sin rotor para animación independiente.
- `app/src/main/res/drawable-nodpi/thunder_volume_rotor_v1.png`: sprite transparente y rotatorio de la perilla.
- `docs/design_review/THUNDER_SHELL_V3_ASSET.md`: prompt, procedencia y reglas de integración del asset.
- `docs/design_review/THUNDER_ROTOR_V1_ASSET.md`: prompts y contrato de animación del rotor.
- `docs/PRODUCT_REQUIREMENTS.md`: requisitos de producto.
- `docs/MONETIZATION_PLAN.md`: estrategia de anuncios/Premium aún no implementada.
- `docs/PROMPT_MAESTRO_APP_ANDROID.md`: aprendizajes para futuros prompts a Kimi/OpenClaw.

## Toolchain actual

- Gradle Wrapper 8.9.
- AGP 8.5.2 temporal.
- Kotlin 2.0.0.
- Java 17.
- compileSdk/targetSdk 35; minSdk 24.
- Media3 ExoPlayer 1.9.2.

Deuda conocida: AGP 8.5.2 sólo declara compatibilidad probada hasta SDK 34. Se intentó AGP 8.7.3, pero `bundletool 1.17.1` descargaba extremadamente lento. No se ocultó la advertencia.

## Pruebas y limitaciones conocidas

- `testDebugUnitTest assembleDebug`: aprobado repetidamente, 40 tareas.
- `lintDebug`: llega a `lintAnalyzeDebug` y queda detenido durante minutos; fue cancelado y sigue pendiente.
- El uso de efectos insert globales sobre sesión 0 está obsoleto y depende del OEM. Mantener siempre fallback local y matriz real de dispositivos.
- El estado guardado se reconcilia con el efecto activo de `GlobalBoostService` al crear la actividad; `isRunning` sólo se confirma tras aplicar el efecto y se limpia al apagar o fallar. Falta cubrir casos OEM extremos donde el sistema reconstruya componentes en secuencias inusuales.
- Falta accesibilidad completa de la perilla mediante teclado/acciones incrementales.
- Falta EQ real, medición de audio controlada, iconografía vectorial definitiva y refinamiento final del acabado Thunder Run.
- No afirmar compatibilidad universal ni mediciones acústicas que aún no existen.

## Monetización planeada, no implementada

- No hay SDK, IDs ni clases AdMob integrados todavía.
- UMP/consentimiento antes de solicitar anuncios.
- No mostrar anuncios al abrir/cerrar, mover volumen, activar boost o durante reproducción.
- Banner adaptable sólo en superficies secundarias estáticas.
- Rewarded opt-in para probar funciones avanzadas.
- Premium sin anuncios con EQ/presets/automatizaciones.
- Billing y derechos verificados mediante arquitectura separada, siguiendo las prácticas de Pixora IA.
- Mantener anuncios, autenticación y billing separados del cambio visual/audio.

## Historial consolidado

- `133037d` — baseline Android auditado.
- `580ae64` — handoff multiagente.
- `9ff7210` — boost global experimental y reflejo multimedia.
- `cde0727` — identidad Pixora, splash, adaptive icon y experiencia visual.
- `e252f88` — validación auditiva Samsung registrada.
- `25c9b03` — cinco conceptos estéreo y galería de selección.

- `bdc4739` — controles externos reales, selección Thunder Deck, evidencia Huawei y este handoff.

## Reglas de trabajo

1. GitHub es la fuente canónica; Drive es espejo del proyecto, excluyendo `.git`, cachés, builds y configuración local.
2. Cada incremento que compile y pase su verificación debe cerrarse con commit en `main`, push a GitHub y sincronización del espejo completo de Drive para revisión desde otra computadora.
3. No publicar una fase rota como terminada. Los avances parciales o experimentales deben identificarse en `04_TEST_LOG.md` y en este contexto.
4. No copiar assets, marca, textos ni código de EZ Booster. El análisis fue clean-room.
5. No presentar controles decorativos como funciones reales.
6. Mantener cambios de audio/UI separados de monetización/login cuando sea posible.
7. Ejecutar build/tests, instalar en dispositivo y guardar evidencia antes de declarar una fase terminada.
8. Invocar la revisión Orbix Guardian después de cambios relevantes y atender bloqueadores antes del commit.
9. No modificar `D:\Orbix\Pixora-IA`; sólo consultarlo como referencia cuando corresponda.

## Siguiente paso exacto

Thunder Shell v4 ya usa rotor independiente, controles metálicos y botón multimedia integrado. Evidencia Samsung en `docs/qa/2026-09-02-rotor-and-controls/`.

El siguiente incremento debe ser **QA funcional Samsung con Spotify activo y adaptación proporcional**:

1. Validar anterior, play/pausa, siguiente y metadatos con Spotify reproduciendo.
2. Convertir la calibración rígida 360dp/9:16 en constraints proporcionales sin perder alineación con el asset.
3. Medir clipping con una señal controlada en 100/150/200%; la activación técnica ya está confirmada.
4. Corregir accesibilidad de la perilla y botones.
5. Diseñar el EQ real por separado; no habilitarlo hasta comprobar procesamiento.

Antes de comenzar desde otra computadora, actualizar `main` y confirmar que `git status --short --branch` está limpio.
