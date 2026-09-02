# Roadmap de Audio Booster

## Fase 0 — Base reproducible

- [x] Auditar código y documentación heredados.
- [x] Confirmar que no existía repositorio GitHub del proyecto.
- [x] Obtener primer APK debug desde el código recibido.
- [x] Añadir Gradle Wrapper (CI pendiente).
- [ ] Validar la matriz actualizada AGP/Gradle/SDK (configurada; descarga de Google Maven pendiente).
- [ ] Ejecutar los tests ya añadidos del mapeo volumen/porcentaje.

## Fase 1 — MVP honesto de volumen

- [ ] Refactor a estado observable y arquitectura testeable.
- [ ] Sincronizar UI al volver desde segundo plano o usar botones físicos.
- [ ] Manejar políticas OEM, modo No molestar y errores de `AudioManager`.
- [ ] Eliminar o bloquear controles que no ejecuten una función real.
- [ ] QA Samsung y Huawei con evidencia.

## Fase 2 — Player propio con boost real

- [ ] Integrar AndroidX Media3.
- [ ] Selección de audio con Storage Access Framework, sin permisos amplios innecesarios.
- [ ] Aplicar ganancia a la sesión propia y medir su efecto.
- [ ] Implementar protección anti-clipping real o limitar presets según evidencia.
- [ ] EQ y presets sólo donde el dispositivo reporte soporte.

## Fase 3 — Producto y Play Store

- [ ] Nombre, identidad y package definitivos antes de crear ficha pública.
- [ ] Privacidad, términos, Data Safety y declaración de anuncios.
- [ ] Icono, screenshots, feature graphic y textos de tienda.
- [ ] Analytics y crash reporting mínimos, con consentimiento cuando corresponda.
- [ ] AAB release, firma, pruebas internas/cerradas y checklist de lanzamiento.

## Gate de avance

Una tarea sólo se marca terminada si tiene código, build, prueba y evidencia. “Existe una clase”, “se ve un switch” o “se genera una notificación” no prueba que una función opere.
