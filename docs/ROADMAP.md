# Roadmap de Audio Booster

## Fase 0 — Base reproducible

- [x] Auditar código y documentación heredados.
- [x] Confirmar que no existía repositorio GitHub del proyecto.
- [x] Obtener primer APK debug desde el código recibido.
- [x] Añadir Gradle Wrapper (CI pendiente).
- [ ] Actualizar AGP 8.5.2 a una versión compatible oficialmente con SDK 35; AGP 8.7.3 quedó pendiente por descarga lenta de `bundletool 1.17.1`.
- [x] Ejecutar los tests del mapeo volumen/porcentaje.

## Fase 1 — MVP honesto de volumen

- [ ] Refactor a estado observable y arquitectura testeable.
- [ ] Sincronizar UI al volver desde segundo plano o usar botones físicos.
- [ ] Manejar políticas OEM, modo No molestar y errores de `AudioManager`.
- [x] Eliminar controles de boost, limiter y servicio que no ejecutaban una función real.
- [ ] QA Samsung y Huawei con evidencia.

## Fase 2 — Player propio con boost real

- [x] Integrar AndroidX Media3 en un reproductor local básico.
- [x] Selección de audio con Storage Access Framework, sin permisos amplios innecesarios.
- [x] Aplicar ganancia nominal de 0, +3 y +6 dB a la sesión propia.
- [ ] Medir salida, clipping y diferencias entre dispositivos con una señal de prueba controlada.
- [ ] Implementar protección anti-clipping real o limitar presets según evidencia.
- [ ] EQ y presets sólo donde el dispositivo reporte soporte.

## Fase 2.5 — Modo global experimental y experiencia Orbix

- [x] Reproducir el mecanismo global compatible observado en clean-room: `DynamicsProcessing`, prioridad alta y sesión 0.
- [x] Añadir limitador propio, servicio foreground `specialUse` y liberación explícita del efecto.
- [x] Verificar en Samsung que `AudioFlinger` registra la cadena global activa y controlada por esta app.
- [x] Añadir observador opcional de metadatos multimedia sin capturar audio.
- [x] Integrar fondo original y dial Orbix interactivo de 0 a 200%.
- [ ] Autorizar manualmente el acceso multimedia y verificar Spotify en vivo.
- [ ] Completar pestaña Ecualizador con controles reales, arte/controles del reproductor y pulido adaptable.
- [x] Separar fondo, carcasa, rotor y LEDs para impedir que el gesto mueva toda la composición.
- [x] Fijar completamente el escenario principal y retirar de vista la bandeja local inferior.
- [ ] Separar conos/rejillas de bocinas y conectar animación por bandas a medición real donde Android lo permita.
- [x] Mostrar en la pantalla del estéreo canción, artista/fuente, volumen, boost y estado SAFE, conservando el ecualizador.
- [ ] QA auditivo y medición de clipping en Samsung; comprobar fallback en Huawei.
- [x] Seleccionar dirección artística final: Thunder Deck.
- [x] Reconstruir Thunder Deck con componentes Android reales y adaptativos, preservando el motor de audio.
- [ ] Refinar el chasis Thunder Deck en pantallas pequeñas/grandes y completar accesibilidad táctil/lector de pantalla.

## Fase 3 — Producto y Play Store

- [ ] Nombre, identidad y package definitivos antes de crear ficha pública.
- [ ] Privacidad, términos, Data Safety y declaración de anuncios.
- [ ] Icono, screenshots, feature graphic y textos de tienda.
- [ ] Analytics y crash reporting mínimos, con consentimiento cuando corresponda.
- [ ] AAB release, firma, pruebas internas/cerradas y checklist de lanzamiento.
- [x] Diseñar estrategia de monetización separada del núcleo de audio (`MONETIZATION_PLAN.md`).
- [ ] Integrar UMP/consentimiento con IDs de prueba y kill switch.
- [ ] Integrar anuncios adaptables en superficies estáticas; no interrumpir controles ni reproducción.
- [x] Reservar visualmente `adBannerSlot` sin SDK, solicitudes ni IDs reales.
- [ ] Añadir Premium con Play Billing y verificación confiable de derechos.
- [ ] Integrar inicio de sesión con Google después de cerrar la interfaz y antes del lanzamiento, separado de anuncios y del motor de audio.

## Gate de avance

Una tarea sólo se marca terminada si tiene código, build, prueba y evidencia. “Existe una clase”, “se ve un switch” o “se genera una notificación” no prueba que una función opere.
