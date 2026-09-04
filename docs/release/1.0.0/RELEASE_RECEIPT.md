# Recibo de lanzamiento — Audio Booster 1.0.0 (1)

- Archivo: `AudioBooster_v1.0.0_1.aab`
- Paquete: `com.pixora.volumemax`
- `versionName`: `1.0.0`
- `versionCode`: `1`
- `minSdk`: 24
- `targetSdk` / `compileSdk`: 36
- Tamaño: 17,301,022 bytes
- SHA-256 del AAB: `C2A8EC6B2A90CB5E685A015E8803B53E655EB7BC71BE7382CD807D2C0EBC45B4`
- Certificado: `CN=Pixora IA, OU=Audio Booster, O=Pixora IA, C=MX`
- SHA-256 del certificado: `8D:28:58:AA:28:CC:62:03:1D:80:21:6C:9F:4C:E0:63:50:F4:6C:21:FB:46:48:62:EB:2B:7B:0B:CE:06:B3:4B`

## Verificaciones

- Gradle `testDebugUnitTest`, `assembleDebug` y `bundleRelease`: correcto.
- `jarsigner`: `jar verified`, salida 0.
- Bundletool 1.18.3 oficial: `validate`, salida 0.
- Manifiesto leído desde el AAB: paquete, versión y SDK coinciden con esta ficha.
- Clave de carga: dedicada, no incluida en Git y respaldada cifrada por separado.

Pendiente fuera del artefacto: carga manual en Play Console, declaración del servicio `specialUse`, ficha, privacidad, seguridad de datos y prueba cerrada.
