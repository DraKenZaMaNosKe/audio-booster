# 04 — Log de pruebas

## 2026-09-01 — Fase análisis

- Samsung conectado por ADB: sí, comando `adb devices -l` mostró `SM_A155M`.
- Paquete identificado: `volumebooster.soundspeaker.louder`.
- APK extraído a workspace local para análisis offline.
- Documentación creada en Drive compartida.

Pendiente:
- Refinar análisis bytecode con herramienta de decompilado si se instala `jadx`.
- Verificar ejecución en vivo y compare `dumpsys audio` con app activa/inactiva.

## 2026-09-01 — Auditoría Codex

- Git local al iniciar: no existía carpeta `.git`.
- Búsqueda GitHub en la cuenta configurada: sin repositorio coincidente por nombre o código.
- Dispositivos ADB detectados: Huawei VNS-L53 y Samsung SM-A155M.
- Toolchain encontrada: JDK incluido con Android Studio; Gradle 8.13 disponible en caché, pero sin wrapper en el proyecto.
- `assembleDebug`: alcanzó `packageDebug` y generó `app/build/outputs/apk/debug/app-debug.apk`.
- Compilación Kotlin: completada.
- `lintDebug`: inició análisis pero se detuvo después de varios minutos sin terminar; debe repetirse aisladamente.
- Advertencia: AGP 8.5.2 no declara compatibilidad probada con `compileSdk 35`.
- Prueba funcional en dispositivo: pendiente; un APK generado no demuestra boost real.

## 2026-09-01 — Base reproducible

- Gradle Wrapper 8.9: generado correctamente.
- Matriz configurada: AGP 8.7.3, Kotlin 2.0.21, Java 17, compile/target SDK 35.
- Tests unitarios `VolumeMathTest`: añadidos.
- Validación con el wrapper actualizado: bloqueada durante la descarga de `bundletool 1.17.1` desde Google Maven; se canceló después de varios minutos sin progreso.
- Estado honesto: la configuración nueva y sus tests deben volver a ejecutarse cuando Google Maven responda; no se consideran aprobados todavía.
