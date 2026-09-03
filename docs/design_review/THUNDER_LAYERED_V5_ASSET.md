# Thunder Layered Shell v5

## Objetivo

Separar la interfaz en capas reales para que solamente se animen los componentes interactivos:

1. `thunder_room_cabinet_v1.png`: habitación y mueble fijos.
2. `thunder_run_stereo_shell_v5.png`: chasis transparente, pantalla, torres, marcos y palancas fijos.
3. `BoostDialView`: LEDs progresivos y halo táctil.
4. `thunder_volume_rotor_v1.png`: rotor que gira con el dedo.
5. Controles Android: anterior, siguiente, reproducción, acceso multimedia, SAFE y presets.

## Fuente y proceso

- Generador: ImageGen integrado de Codex.
- Fuente verde conservada: `docs/design_review/source_assets/thunder_shell_v5_chroma_green.png`.
- Recurso final: `app/src/main/res/drawable-nodpi/thunder_run_stereo_shell_v5.png`.
- Eliminación cromática: verde convertido a alpha real; la cavidad central se conserva negra y opaca.

## Prompt final

Editar la carcasa v4 conservando geometría, escala, perspectiva frontal, pantalla, torres, bocinas, rejillas, tornillos, metal y palancas. Sustituir únicamente el exterior y el piso reflectante por verde croma uniforme `#00FF00`; eliminar rotor y LEDs encendidos; dejar un canal metálico oscuro con alojamientos vacíos alrededor de una cavidad central; dejar los receptáculos laterales sin flechas. Mantener dimensiones y alineación. Sin texto, logos, porcentajes, controles nuevos, habitación, muebles, sombras exteriores, tablero cuadriculado ni marca de agua.

## Interacción de perilla

- `ACTION_DOWN`: captura el puntero sin cambiar bruscamente el valor y bloquea la intercepción del `ScrollView`.
- `ACTION_MOVE`: aplica el delta angular relativo; fondo y chasis permanecen inmóviles.
- `ACTION_UP`/`ACTION_CANCEL`: libera el puntero y devuelve el gesto al contenedor.
- Mientras está capturada: halo cian y rotor al 105.5% para indicar foco.

## Bocinas vivas — siguiente incremento

- Separar conos de graves, conos medios/agudos y rejillas/marcos.
- Woofers: escala/desplazamiento corto según energía de bajas frecuencias.
- Tweeters: movimiento menor y más rápido según bandas medias/altas.
- Reproductor local: FFT real ligada a su sesión de Media3.
- Spotify/externo: intentar visualización de sesión global sólo si Android/OEM la autoriza; usar fallback honesto de actividad, nunca presentar una animación estimada como medición real.
