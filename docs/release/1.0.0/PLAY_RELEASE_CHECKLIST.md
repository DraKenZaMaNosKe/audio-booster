# Entrega Play Store — 1.0.0 (1)

- [x] `compileSdk` y `targetSdk` 36.
- [x] Nombre de versión `1.0.0`; código de versión `1`.
- [x] Pruebas unitarias y APK debug compilados con AGP 8.13.2 / Gradle 8.13.
- [x] Instalación y arranque en Huawei VNS-L53, Android 7.
- [x] Rótulo LED medido por ancho y validado sin desbordamiento.
- [x] Capturas de estado inicial y respuesta de la perilla.
- [x] Dos piezas verticales de marketing con personas ficticias y uso real de la app.
- [x] Configurar clave de carga dedicada y conservarla fuera de Git.
- [x] Generar y verificar AAB firmado final.
- [ ] Completar declaración `specialUse` del servicio en primer plano y adjuntar video demostrativo en Play Console.
- [ ] Completar ficha, política de privacidad, seguridad de datos y pruebas cerradas requeridas por la cuenta.

El bundle fue firmado con la clave dedicada `audio_booster_upload_v2.jks`, no con la clave debug. La copia de recuperación está cifrada por su contraseña y se conserva separada del repositorio. No guardar la contraseña junto al archivo.
