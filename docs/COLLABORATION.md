# Colaboración entre Codex, Kimi y OpenClaw

## Fuente de verdad

1. GitHub es la fuente canónica de código y documentación.
2. Drive es espejo de entregables, handoffs y artefactos grandes.
3. Cada agente debe iniciar con `git status --short --branch`, `git log -5 --oneline` y lectura de este archivo.
4. Nadie debe sobrescribir cambios ajenos ni trabajar sobre cambios no entendidos.

## Flujo

- Crear una rama por objetivo: `feature/...`, `fix/...`, `docs/...`.
- Un commit debe contener un cambio coherente y su documentación/pruebas asociadas.
- Antes de entregar: build, tests, lint y resumen de archivos cambiados.
- No hacer push, release, deploy o publicación en Play sin autorización explícita, excepto el push inicial solicitado para crear este repositorio.
- No guardar tokens, contraseñas, keystores ni archivos `local.properties`.

## Formato de handoff

- Objetivo solicitado y estado anterior verificado.
- Cambios exactos realizados.
- Comandos y resultados de validación.
- Qué no se probó.
- Riesgos y siguiente paso recomendado.
- Commit y rama.

## Regla de realidad funcional

Cada control visible debe estar conectado a una implementación verificable. Si una función es mock, experimental o sólo visual, debe decirlo en código, UI y documentación.
