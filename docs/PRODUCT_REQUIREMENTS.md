# Requisitos de producto — Orbix Audio Booster

Estado: base técnica funcional en validación (2026-09-01).

## Objetivo

Crear un amplificador de audio para Android con identidad propia de Orbix, capaz de controlar el volumen normal de 0 a 100% y ofrecer de 101 a 200% mediante procesamiento global experimental en dispositivos compatibles. La aplicación debe ser honesta cuando un fabricante no permita ese procesamiento y ofrecer como respaldo un reproductor local con ganancia real.

## Requisitos funcionales

1. Controlar el volumen multimedia normal entre 0 y 100%.
2. Aplicar ganancia global por encima de 100% con `DynamicsProcessing` sobre la sesión global 0, acompañada de limitador y un servicio visible mientras esté activa.
3. Desactivar y liberar completamente el efecto al apagarlo; no dejar ganancia persistente después de detener el servicio o reiniciar el dispositivo.
4. Mostrar título, artista y aplicación de la reproducción externa mediante `NotificationListenerService` y `MediaController`, únicamente con permiso explícito del usuario.
5. No capturar, descargar, clonar ni almacenar el audio de Spotify, YouTube u otras aplicaciones.
6. Incluir reproductor local con ganancia por sesión como alternativa verificable para dispositivos que rechacen el efecto global.
7. Incorporar ecualizador, graves y perfiles sólo después de validar que cada control modifica realmente el efecto; ningún control decorativo podrá presentarse como funcional.
8. Mostrar advertencias de audición y distorsión antes de niveles altos.

## Experiencia e identidad

- Jerarquía inspirada en patrones comunes del mercado: encabezado, pestañas Volumen/Ecualizador, dial central, control del sistema, presets y tarjeta de reproducción.
- Diseño, ilustración, iconografía, textos, marca y código originales de Orbix. No copiar recursos, configuración numérica privada ni identidad visual de EZ Booster.
- Tema oscuro azul noche con acentos cian/violeta y altavoces abstractos originales.
- Control central accesible e interactivo de 0 a 200%, además de presets 0, 30, 60, 100, 125, 150, 175 y 200%.
- El estado debe diferenciar claramente volumen normal, modo global activo, modo no compatible y respaldo local.

## Privacidad y permisos

- El acceso a notificaciones es opcional y sólo se usa para metadatos/controles multimedia.
- No solicitar micrófono para el MVP; añadirlo únicamente si una función real lo exige y con justificación visible.
- Mantener una notificación persistente mientras el procesamiento global esté activo.
- Preparar política de privacidad y formulario de seguridad de datos antes de Play Store.

## Compatibilidad y calidad

- Probar en una matriz real por fabricante y versión de Android; la sesión global 0 está obsoleta y su funcionamiento depende del OEM.
- Medir distorsión, clipping, estabilidad, consumo y liberación del efecto, además de una comparación audible controlada.
- Conservar límite inicial de +6 dB (~200% de amplitud) y limitador; cualquier ampliación exige pruebas acústicas.
- Registrar evidencia de compilación, pruebas unitarias, instalación, `AudioFlinger` y QA audible sin exagerar resultados.

## Fases posteriores

Una vez estable el núcleo de audio: ecualizador completo, perfiles, pulido visual, analítica respetuosa, monetización y acceso con Google. La autenticación y las compras reutilizarán el patrón arquitectónico validado de Pixora IA, pero se implementarán y auditarán como módulos separados antes de publicar.

## Criterio de aceptación del MVP

En al menos un Samsung compatible, Spotify debe continuar siendo dueño de su reproducción mientras Orbix refleja sus metadatos y mantiene una cadena `DynamicsProcessing` activa en sesión 0. El usuario debe percibir una diferencia controlada entre 100 y 125/150/200% sin crash ni clipping inaceptable. En un dispositivo no compatible, la app debe informar la limitación y conservar el reproductor local funcional.
