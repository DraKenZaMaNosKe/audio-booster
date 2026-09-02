# Plan de monetización — AudioBooster by Pixora IA

Estado: diseño previo; no hay SDK ni identificadores publicitarios integrados todavía.

## Principio de producto

La monetización no puede interrumpir el control de volumen, aparecer sobre otras aplicaciones, cortar la música ni sorprender al usuario durante una interacción con la perilla. El servicio global debe seguir funcionando independientemente de anuncios, red, consentimiento o autenticación.

## Modelo propuesto

### Gratis

- Control normal y boost global básico.
- Banner adaptable únicamente en pantallas internas estáticas, nunca encima de la perilla o controles.
- Intersticial con frecuencia limitada sólo al terminar una navegación voluntaria hacia una sección secundaria; nunca al abrir/cerrar la app, activar el boost, cambiar volumen o mientras el usuario reproduce audio.
- Anuncio recompensado opcional para probar temporalmente perfiles avanzados. La acción y recompensa deben explicarse antes de mostrarlo.

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
