# Audio Booster para Android

Proyecto clean-room de Orbix para controlar el volumen multimedia y estudiar una amplificación segura y comprobable. El nombre comercial y el package definitivo siguen pendientes.

## Estado real (2026-09-01)

- El control de `STREAM_MUSIC` entre 0 y el máximo permitido por Android funciona a nivel de código.
- El proyecto compila un APK debug.
- El boost por encima del máximo para audio de terceros **no está implementado**.
- `LoudnessEnhancer` existe como experimento para una sesión de reproducción propia, pero todavía no está conectado a un player.
- El limiter, EQ y servicio persistente todavía no constituyen un motor de audio funcional.
- No está listo para Play Store ni para afirmar que amplifica Spotify u otras apps.

## Documentación

- [Proceso maestro](00_MASTER_PROCESS.md)
- [Auditoría técnica inicial](docs/PROJECT_AUDIT_2026-09-01.md)
- [Roadmap](docs/ROADMAP.md)
- [Reglas de colaboración](docs/COLLABORATION.md)
- [Prompt maestro reutilizable para Kimi](docs/PROMPT_MAESTRO_APP_ANDROID.md)
- [Registro de pruebas](04_TEST_LOG.md)

La fuente canónica es GitHub. La carpeta de Drive `pixoraIA_admin/claude_compartido/audio_booster` es un espejo de coordinación.
