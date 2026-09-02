# Diseño seleccionado — Thunder Deck

Fecha de decisión: 2026-09-02  
Estado: aprobado por el propietario del producto  
Concepto seleccionado: **05 — Thunder Deck**

## Motivo de selección

Thunder Deck comunica inmediatamente potencia, audio de alto desempeño y control. La disposición es reconocible sin aprendizaje previo: pantalla multimedia, altavoces, perilla principal, transporte, ecualización y protección.

## Traducción a interfaz Android real

- **Pantalla superior:** canción, artista, aplicación de origen, estado de reproducción y espectro animado.
- **Perilla tipo turbina:** control continuo de 0 a 200%; iluminación azul, violeta, naranja y roja según intensidad.
- **Anterior / siguiente:** botones físicos laterales con estados táctiles visibles.
- **Play / pausa:** control central separado y accesible.
- **EQ:** abre el panel de ecualización real cuando esté implementado.
- **SAFE:** muestra estado del limitador y advertencias; nunca simula protección inexistente.
- **Tres palancas:** presets reales y personalizables, inicialmente 100%, 150% y 200%.
- **Torres laterales:** respuesta visual al audio/estado; decorativas hasta que exista señal real de visualizador, sin presentar información falsa.

## Reglas de implementación

1. No usar un mapa táctil invisible sobre la imagen generada.
2. Construir pantalla, perilla, botones y palancas como componentes Android reales, accesibles y adaptativos.
3. Conservar el concepto como referencia artística y material de revisión, no como captura final de la aplicación.
4. Priorizar pantallas estrechas y permitir scroll o reacomodo en dispositivos pequeños.
5. Mantener todas las funciones de audio actuales durante la migración visual.
6. No introducir anuncios, autenticación ni billing dentro del mismo cambio de interfaz.

## Orden de producción

1. Chasis responsive y pantalla multimedia.
2. Perilla térmica y presets integrados.
3. Controles de reproducción externa/local.
4. Panel de ecualizador real.
5. Animación de espectro basada en datos permitidos.
6. Accesibilidad, rendimiento y QA visual en Samsung/Huawei.
