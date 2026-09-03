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

## Ambiente Thunder Room v1

- Recurso: `app/src/main/res/drawable-nodpi/thunder_room_cabinet_v1.png`.
- Generador: ImageGen integrado de Codex.
- Uso: capa independiente detrás del estéreo y de todos los controles Android reales.

Prompt final: crear solamente el interior y el mueble de la referencia, sin estéreo ni controles: sala oscura al atardecer, ciudad por la ventana izquierda, cortinas, cuadro abstracto, lámpara cálida, plantas y consola de nogal oscuro con superficie horizontal, discos de vinilo, libros y cajones. Composición vertical 9:16, centro despejado, iluminación azul de anochecer combinada con ámbar y reflejos cian/magenta. Sin personas, logos, palabras, números ni marcas de agua.
