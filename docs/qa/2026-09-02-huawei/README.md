# QA Huawei VNS-L53 — 2026-09-02

Dispositivo: Huawei VNS-L53, serial ADB `G2R4C17516000149`.

## Resultados

- Instalación mediante ADB: aprobada.
- Arranque y layout 1080×1920: aprobados; sin crash `AndroidRuntime` observado.
- Scroll y controles visibles: aprobados.
- Volumen multimedia normal: controlado hasta 100%.
- Boost global: no compatible. El dispositivo está por debajo de API 28 y la app rechazó el modo de forma segura, liberó el procesador, regresó a 0% y mostró el fallback local.
- Servicio global residual: ninguno después del rechazo.
- Acceso a notificaciones: no autorizado en este Huawei.
- Controles externos sin permiso/sesión: no ejecutan una acción falsa; muestran el mensaje explicativo y no causan crash.
- Reproductor local: disponible como siguiente prueba manual con archivo de audio.

## Capturas

1. `01-home.png`: inicio adaptado al Huawei.
2. `02-global-fallback.png`: rechazo seguro del efecto global.
3. `03-controls.png`: controles externos, EQ pendiente y estado SAFE.
4. `04-media-fallback.png`: respuesta al intentar controlar sin autorización/sesión.

Estas capturas corresponden a la interfaz transitoria anterior a la reconstrucción completa Thunder Deck; son evidencia de compatibilidad, no aprobación visual final.
