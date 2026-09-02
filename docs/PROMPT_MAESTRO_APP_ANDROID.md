# Prompt maestro para pedir a Kimi una app Android completa

Copiar este prompt y sustituir los campos entre corchetes.

---

Quiero que desarrolles **[NOMBRE/IDEA DE APP]** como proyecto Android mantenible y publicable en Google Play. No me entregues sólo pantallas o clases sueltas: cada función visible debe estar conectada, ejecutarse de verdad y tener evidencia de prueba.

## 1. Antes de escribir código

1. Revisa todos los archivos existentes, `AGENTS.md`, README, Git, Drive y documentación maestra aplicable.
2. Ejecuta y reporta `git status --short --branch`, remotos y últimos cinco commits.
3. Explica qué ya funciona, qué es mock/prototipo y qué limitaciones impone Android o Google Play.
4. No copies código, assets, textos, nombres internos ni diseño de apps de terceros. Si se estudió una referencia, trabaja clean-room y documenta sólo comportamientos públicos.
5. Propón arquitectura, permisos mínimos, riesgos técnicos, riesgos de política y criterios verificables de aceptación.

## 2. Proyecto reproducible obligatorio

- Incluye `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar` y `gradle-wrapper.properties`.
- Define versiones compatibles entre JDK, Gradle, Android Gradle Plugin, Kotlin, `compileSdk`, `targetSdk` y dependencias.
- No silencies advertencias de incompatibilidad para aparentar éxito; corrige la matriz de versiones.
- Incluye `.gitignore`, `local.properties.example` y comandos exactos para build/test/lint.
- Nunca guardes secretos, tokens, keystores, passwords ni rutas privadas en Git.
- El proyecto debe compilar desde una terminal limpia, no sólo desde el estado interno de Android Studio.

## 3. Implementación real

- Todo botón, slider, switch, servicio, notificación y permiso debe tener propósito y lógica conectada.
- No muestres porcentajes, dB, estados “activo”, limiter, EQ o persistencia si el procesamiento correspondiente no existe.
- Distingue capacidades del sistema, capacidades de una sesión propia y funciones experimentales/OEM.
- Maneja lifecycle, errores, rotación, segundo plano, process death y cambios externos de estado.
- Usa permisos mínimos y APIs públicas. Justifica cada permiso contra una función visible.
- Incluye accesibilidad, textos localizables, estados vacíos/carga/error y seguridad del usuario.

## 4. Calidad obligatoria

- Unit tests para lógica pura y conversiones.
- Tests instrumentados para flujos Android críticos.
- `assembleDebug`, tests y lint ejecutados con resultado registrado.
- QA en los dispositivos indicados: **[MODELOS/API]**.
- Entrega evidencia: versión, commit, SHA-256 del APK/AAB, capturas/logs y matriz de pruebas.
- No marques una tarea completa sólo porque “compila”; demuestra el comportamiento solicitado.

## 5. Google Play desde el diseño

- Revisa políticas actuales aplicables a permisos, foreground services, anuncios, suscripciones, salud/seguridad y afirmaciones del producto.
- Prepara política de privacidad, Data Safety, clasificación, audiencia, Ads declaration y texto de ficha coherentes con el binario real.
- Usa upload key dedicada y secretos fuera de repositorio/Drive compartido.
- Planea internal testing, closed testing, monitoreo de crashes/ANR y rollout gradual.
- No publiques ni crees cambios externos irreversibles sin mi autorización explícita.

## 6. GitHub, Drive y colaboración

- GitHub es fuente canónica; Drive es espejo de handoffs y artefactos.
- Crea ramas por objetivo y commits pequeños y coherentes.
- Conserva cambios de otros agentes; no hagas reset destructivo.
- Actualiza README, roadmap, decisiones y log de pruebas junto con el código.
- Al terminar deja un handoff con estado previo, cambios, validación, pendientes, riesgos, rama y commit para que Codex/OpenClaw continúen sin repetir trabajo.

## 7. Definición de terminado

Una función está terminada sólo si está implementada, conectada a la UI, compila, tiene prueba apropiada, fue validada en el entorno objetivo y está documentada. Declara con honestidad todo lo que no pudiste probar.

Comienza con una auditoría y un plan por fases. Luego implementa únicamente la primera fase aprobada, valida y entrega el handoff.

---

## Nota específica aprendida de Audio Booster

Para apps de audio, exige separar: (a) volumen del sistema hasta el máximo OEM, (b) procesamiento de una sesión reproducida por la propia app y (c) cualquier efecto global experimental. No aceptar como “boost real” una notificación persistente, un slider con dB estimados o una clase de efectos que nunca se conecta a una sesión de audio.
