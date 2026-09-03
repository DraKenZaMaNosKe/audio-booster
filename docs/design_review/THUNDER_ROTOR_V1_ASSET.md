# Thunder rotor v1

## Propósito

Sprite independiente de la perilla de volumen. Gira alrededor de su centro mediante una `ImageView`; el aro LED, la cavidad y el chasis permanecen fijos en la carcasa v4.

- Carcasa sin rotor: `drawable-nodpi/thunder_run_stereo_shell_v4.png`.
- Sprite transparente: `drawable-nodpi/thunder_volume_rotor_v1.png`.
- Sprite: 1254×1254, alpha real en el exterior.
- Generación: herramienta integrada ImageGen, 2026-09-02.

## Prompts finales

Carcasa:

> Retirar sólo la turbina/perilla central de la carcasa. Sustituirla por una cavidad circular negra con ranuras concéntricas y eje central. Conservar exactamente el aro LED, tornillos, botones, bocinas, pantalla, palancas, dimensiones y luz. Sin texto ni controles nuevos.

Rotor:

> Crear un rotor frontal aislado de metal grafito cepillado, doce aspas simétricas, mecanizado fino, tornillos y centro perfectamente alineado. Lienzo cuadrado, transparencia real fuera del rotor, sin aro LED, textos, logos, sombra exterior ni escenario.

## Animación

`BoostDialView` traduce el arco táctil 0–200% y envía previsualización continua. `MainActivity` rota `knobSprite` de 0° a 540° durante el gesto y anima 420 ms al aplicar presets. El porcentaje es texto separado; ya no se dibuja un arco opaco sobre la perilla.
