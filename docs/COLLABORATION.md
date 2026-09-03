# Colaboración entre Codex, Kimi y OpenClaw

## Fuente de verdad

1. GitHub es la fuente canónica de código y documentación.
2. Drive es espejo de entregables, handoffs y artefactos grandes.
3. Cada agente debe iniciar con `git status --short --branch`, `git log -5 --oneline` y lectura de este archivo.
4. Nadie debe sobrescribir cambios ajenos ni trabajar sobre cambios no entendidos.

## Sincronización continua autorizada

Cada incremento verificable debe compilar, registrar evidencia, crear un commit independiente en `main`, hacer push inmediato a `origin/main` y actualizar el espejo de Drive. El espejo excluye `.git`, cachés, builds y configuración específica de la computadora.

Cuando cambie el punto de reanudación, `contexto_audiobooster.md` debe actualizarse y verificarse tanto en GitHub como en Drive.

## Flujo

- Para incrementos pequeños y verificados puede trabajarse directamente en `main`. Los objetivos grandes o riesgosos usan `feature/...`, `fix/...` o `docs/...` y se fusionan a `main` antes de sincronizar el avance.
- Un commit debe contener un cambio coherente y su documentación/pruebas asociadas.
- Antes de entregar: build, tests, lint y resumen de archivos cambiados.
- Los pushes de avances verificados a `origin/main` están autorizados por el propietario. Releases, despliegues externos y publicación en Play Store requieren autorización explícita adicional.
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
