# Audio Booster para Android

Proyecto clean-room de Orbix para controlar el volumen multimedia y estudiar una amplificación segura y comprobable. “Orbix Audio Booster” es el nombre de trabajo; el package definitivo sigue pendiente.

## Estado real (2026-09-01)

- El control de `STREAM_MUSIC` entre 0 y el máximo permitido por Android funciona a nivel de código.
- El proyecto compila un APK debug.
- El volumen multimedia del sistema puede controlarse hasta el máximo permitido por el fabricante.
- El reproductor local Media3 permite seleccionar audio mediante el selector del sistema y aplicar ganancia real de 0, +3 o +6 dB a su propia sesión con `LoudnessEnhancer`.
- La app dispone de un modo global experimental de 101 a 200% mediante `DynamicsProcessing` en sesión 0, con limitador y servicio visible. Se verificó la cadena activa en un Samsung, pero su compatibilidad depende del fabricante.
- Puede reflejar metadatos de Spotify u otro reproductor mediante acceso opcional a notificaciones; no captura ni copia audio.
- Incluye un dial Orbix original, presets y un fondo visual propio. El ecualizador completo continúa pendiente.
- El MVP compila y arranca en dispositivo, pero todavía no está listo para Play Store ni cuenta con QA auditivo/mediciones acústicas suficientes.

## Documentación

- [Proceso maestro](00_MASTER_PROCESS.md)
- [Auditoría técnica inicial](docs/PROJECT_AUDIT_2026-09-01.md)
- [Roadmap](docs/ROADMAP.md)
- [Reglas de colaboración](docs/COLLABORATION.md)
- [Prompt maestro reutilizable para Kimi](docs/PROMPT_MAESTRO_APP_ANDROID.md)
- [Registro de pruebas](04_TEST_LOG.md)
- [Requisitos de producto](docs/PRODUCT_REQUIREMENTS.md)
- [Plan de monetización](docs/MONETIZATION_PLAN.md)

La fuente canónica es GitHub. La carpeta de Drive `pixoraIA_admin/claude_compartido/audio_booster` es un espejo de coordinación.
