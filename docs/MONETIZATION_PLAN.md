# Plan de monetización — AudioBooster by Pixora IA

Estado: diseño previo; no hay SDK ni identificadores publicitarios integrados todavía.

La interfaz Thunder Run reserva `adBannerSlot`, un contenedor de 56dp situado después de los controles locales. En esta fase sólo muestra `AD`: no solicita red, no carga publicidad y no contiene IDs de producción.

## Principio de producto

La monetización no puede interrumpir el control de volumen, aparecer sobre otras aplicaciones, cortar la música ni sorprender al usuario durante una interacción con la perilla. El servicio global debe seguir funcionando independientemente de anuncios, red, consentimiento o autenticación.

## Modelo propuesto

### Gratis

- Control normal y boost global básico.
- Banner adaptable anclado como primer formato, dentro de `adBannerSlot`, nunca encima de la perilla o controles.
- Intersticial con frecuencia limitada sólo al terminar una navegación voluntaria hacia una sección secundaria; nunca al abrir/cerrar la app, activar el boost, cambiar volumen o mientras el usuario reproduce audio.
- Anuncio recompensado opcional para probar temporalmente perfiles avanzados. La acción y recompensa deben explicarse antes de mostrarlo.

## Formatos evaluados para AudioBooster

1. **Banner adaptable — recomendado para el MVP.** Google lo plantea como la evolución de los banners estándar y ajusta el tamaño al dispositivo. Ya tiene ubicación reservada debajo de los controles.
2. **Recompensado — recomendado después de Premium.** Siempre voluntario; puede habilitar temporalmente un tema visual o perfil de EQ. No debe recompensar niveles de volumen peligrosos.
3. **Inicio de aplicación — opcional y tardío.** Sólo sobre splash/carga y después de que el usuario haya abierto la app varias veces. No se mostrará en el primer uso ni cada vez que vuelva brevemente desde Spotify.
4. **Intersticial — baja prioridad.** Únicamente en una pausa natural y con límite estricto. AudioBooster casi no tiene transiciones naturales, por lo que no debe ser el formato principal.
5. **Nativo avanzado — no recomendado en la pantalla principal.** Es útil en feeds o contenido desplazable y requiere identificación publicitaria visible; el estéreo no ofrece hoy una superficie adecuada.

Fuentes oficiales consultadas el 2026-09-02: documentación de formatos de AdMob, banners adaptables, anuncios recompensados y guía de App Open de Google.

- https://support.google.com/admob/answer/6128738
- https://support.google.com/admob/answer/9993556
- https://support.google.com/admob/answer/7372450
- https://developers.google.com/admob/android/app-open

### Premium

- Sin anuncios.
- Ecualizador completo, presets guardados, perfiles por dispositivo y automatizaciones futuras.
- Compra/suscripción mediante una interfaz de billing independiente del proveedor, siguiendo el patrón validado de Pixora IA.

## Arquitectura prevista

- `AdGateway`: interfaz neutral para inicializar, precargar, mostrar y liberar anuncios.
- `ConsentManager`: UMP antes de solicitar anuncios y acceso permanente a opciones de privacidad.
- `EntitlementRepository`: fuente única para Premium; nunca confiar sólo en una bandera del cliente.
- `PlacementPolicy`: frecuencia, enfriamiento y exclusiones durante reproducción/ajuste de audio.
- IDs de prueba en debug; IDs reales sólo mediante configuración release no incluida en Git.
- Kill switch para desactivar anuncios sin romper la aplicación.

## Secuencia de implementación

1. Cerrar audio, UI, accesibilidad y estabilidad OEM.
2. Crear ficha de AdMob y mensajes de privacidad; integrar UMP y pruebas sin anuncios reales.
3. Integrar banner adaptable en una ubicación revisada visualmente.
4. Añadir rewarded opt-in y después, sólo si aporta valor, intersticiales en pausas lógicas.
5. Implementar Play Billing/Premium y validación de compras del lado confiable.
6. Completar política de privacidad, Data Safety, ads declaration y pruebas cerradas.

## Reglas no negociables

- Nunca mostrar intersticial al iniciar, salir, pulsar atrás, activar/desactivar boost o mover controles.
- Nunca mostrar publicidad fuera de la app ni desde el servicio foreground.
- No solicitar anuncios antes de que `ConsentManager` confirme que pueden pedirse.
- No colocar anuncios donde generen clics accidentales cerca de presets o controles de reproducción.
- Conservar telemetría mínima y documentar cada dato recopilado.
