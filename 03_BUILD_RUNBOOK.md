# 03 — Build Runbook

## Requisitos sugeridos

- Android Studio Ladybug/Koala estable
- JDK 17
- Android SDK + platform 35/36
- Gradle 8.x
- ADB para pruebas

## Comandos esperados

Desde raíz del proyecto:

```powershell
.\gradlew.bat clean assembleDebug
.\gradlew.bat installDebug
.\gradlew.bat testDebugUnitTest
```

Sin Gradle wrapper inicial: abrir con Android Studio y permitir que genere/actualice wrapper.

## Salida esperada

APK debug objetivo:

```text
app\build\outputs\apk\debug\app-debug.apk
```

Copia de prueba a Drive:

```powershell
Copy-Item .\app\build\outputs\apk\debug\app-debug.apk "G:\Mi unidad\pixoraIA_admin\claude_compartido\audio_booster\releases\boost-debug-yyyymmdd-hhmm.apk"
```

## Notas de compatibilidad

- Android 8.0+: foreground service.
- Android 13+: `POST_NOTIFICATIONS`.
- Android 10+ media: preferir Media3/ExoPlayer.

## Firma futura

- Generar upload key propia.
- No usar la keystore de debug para Play.
- Guardar passwords fuera del repo/Drive compartido.

## Pruebas mínimas manual antes de entregar

1. Instalación limpia.
2. Abrir app y otorgar notificación.
3. Subir/bajar volumen real desde UI.
4. Spotify reproduciendo en segundo plano + slider de volumen.
5. Player local con archivo MP3 y boost activo.
6. Limitador: subir demasiado no clippea.
7. Matar app y reabrir: recordar último estado.
