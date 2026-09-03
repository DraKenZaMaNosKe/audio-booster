# Thunder Run shell v3

## Propósito

Carcasa raster original utilizada como superficie principal del estéreo. Sustituye las torres y paneles planos dibujados por código. Los controles Android reales permanecen superpuestos y conservan accesibilidad/lógica.

Archivo: `app/src/main/res/drawable-nodpi/thunder_run_stereo_shell_v3.png`  
Generación: herramienta integrada ImageGen, 2026-09-02.  
Referencias: concepto 05 aprobado y captura de la implementación anterior. Las referencias se usaron sólo para composición/estilo; no se copiaron marcas ni recursos.

## Prompt final

> Crear una superficie de control Android vertical 9:16, frontal y simétrica, con dos torres de bocinas y consola central. Incluir pantalla rectangular hundida, gran perilla turbina con aro LED cian-violeta-rojo, controles circulares, tres palancas físicas, ventilas, tornillos, uniones y paneles de metal grafito. Estilo anime-realista de producto, metal cepillado, malla negra y profundidad de conos. Dejar interiores limpios para superponer controles reales. Sin habitación, muebles, textos, logos, marcas, watermark, teléfono ni botones UI impresos.

## Regla de integración

La imagen no define interacción. `activity_main.xml`, `BoostDialView` y `MainActivity` mantienen las zonas táctiles, estado, advertencias y motor de audio. Si cambia la proporción del asset se deben recalibrar y probar los límites táctiles con `uiautomator`.
