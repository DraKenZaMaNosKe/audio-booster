param(
    [string]$KeyStorePath = "$env:USERPROFILE\.android\audio_booster_upload.jks",
    [string]$Alias = "audio_booster_upload"
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$keyTool = Join-Path $env:JAVA_HOME "bin\keytool.exe"
$gradle = Join-Path $projectRoot "gradlew.bat"
$outputDir = Join-Path $projectRoot "releases"
$finalAab = Join-Path $outputDir "AudioBooster_v1.0.0_1.aab"
$builtAab = Join-Path $projectRoot "app\build\outputs\bundle\release\app-release.aab"

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

function Read-MaskedPassword([string]$Prompt) {
    $form = New-Object System.Windows.Forms.Form
    $form.Text = "Firma segura de Audio Booster"
    $form.StartPosition = "CenterScreen"
    $form.Size = New-Object System.Drawing.Size(520, 190)
    $form.TopMost = $true

    $label = New-Object System.Windows.Forms.Label
    $label.Text = $Prompt
    $label.AutoSize = $true
    $label.Location = New-Object System.Drawing.Point(24, 22)
    $form.Controls.Add($label)

    $box = New-Object System.Windows.Forms.TextBox
    $box.Location = New-Object System.Drawing.Point(24, 55)
    $box.Size = New-Object System.Drawing.Size(455, 26)
    $box.UseSystemPasswordChar = $true
    $form.Controls.Add($box)

    $ok = New-Object System.Windows.Forms.Button
    $ok.Text = "Continuar"
    $ok.Location = New-Object System.Drawing.Point(294, 100)
    $ok.DialogResult = [System.Windows.Forms.DialogResult]::OK
    $form.Controls.Add($ok)

    $cancel = New-Object System.Windows.Forms.Button
    $cancel.Text = "Cancelar"
    $cancel.Location = New-Object System.Drawing.Point(394, 100)
    $cancel.DialogResult = [System.Windows.Forms.DialogResult]::Cancel
    $form.Controls.Add($cancel)

    $form.AcceptButton = $ok
    $form.CancelButton = $cancel
    $form.Add_Shown({ $box.Focus() })
    $result = $form.ShowDialog()
    if ($result -ne [System.Windows.Forms.DialogResult]::OK) {
        throw "Proceso cancelado por el usuario."
    }
    return $box.Text
}

if (-not (Test-Path -LiteralPath $keyTool)) {
    throw "No se encontró keytool. Abre el proyecto con el JDK de Android Studio configurado."
}

if (Test-Path -LiteralPath $KeyStorePath) {
    throw "La clave ya existe en $KeyStorePath. No se sobrescribió."
}

$password = Read-MaskedPassword "Crea una contraseña NUEVA de al menos 12 caracteres."
$confirmation = Read-MaskedPassword "Repite exactamente la contraseña nueva."
$passwordText = $password
$confirmationText = $confirmation

try {
    if ($passwordText -cne $confirmationText) {
        throw "Las contraseñas no coinciden. No se creó ninguna clave."
    }
    if ($passwordText.Length -lt 12) {
        throw "Usa al menos 12 caracteres. No se creó ninguna clave."
    }

    $keyDirectory = Split-Path -Parent $KeyStorePath
    New-Item -ItemType Directory -Force -Path $keyDirectory | Out-Null
    $env:AUDIOBOOSTER_UPLOAD_SECRET = $passwordText

    & $keyTool -genkeypair -v `
        -keystore $KeyStorePath `
        -storepass:env AUDIOBOOSTER_UPLOAD_SECRET `
        -keypass:env AUDIOBOOSTER_UPLOAD_SECRET `
        -alias $Alias `
        -keyalg RSA `
        -keysize 4096 `
        -validity 10000 `
        -dname "CN=Pixora IA, OU=Audio Booster, O=Pixora IA, C=MX"
    if ($LASTEXITCODE -ne 0) { throw "keytool no pudo crear la clave." }

    $env:AUDIOBOOSTER_KEYSTORE_FILE = $KeyStorePath
    $env:AUDIOBOOSTER_KEYSTORE_PASSWORD = $passwordText
    $env:AUDIOBOOSTER_KEY_ALIAS = $Alias
    $env:AUDIOBOOSTER_KEY_PASSWORD = $passwordText

    Push-Location $projectRoot
    try {
        & $gradle testDebugUnitTest bundleRelease --no-daemon --console=plain
        if ($LASTEXITCODE -ne 0) { throw "La compilación de lanzamiento falló." }
    } finally {
        Pop-Location
    }

    New-Item -ItemType Directory -Force -Path $outputDir | Out-Null
    Copy-Item -LiteralPath $builtAab -Destination $finalAab -Force
    & $keyTool -printcert -jarfile $finalAab
    if ($LASTEXITCODE -ne 0) { throw "El AAB se creó, pero su firma no pudo verificarse." }

    Write-Host ""
    Write-Host "AAB firmado y verificado:" -ForegroundColor Green
    Write-Host $finalAab -ForegroundColor Cyan
    Write-Host "Clave privada:" -ForegroundColor Yellow
    Write-Host $KeyStorePath
    Write-Host "Guarda una copia privada de la clave y su contraseña. No las subas a Git."
    [System.Windows.Forms.MessageBox]::Show(
        "AAB firmado y verificado.`n`n$finalAab`n`nRegresa a Codex y escribe: listo",
        "Audio Booster listo",
        [System.Windows.Forms.MessageBoxButtons]::OK,
        [System.Windows.Forms.MessageBoxIcon]::Information
    ) | Out-Null
} finally {
    Remove-Item Env:AUDIOBOOSTER_UPLOAD_SECRET -ErrorAction SilentlyContinue
    Remove-Item Env:AUDIOBOOSTER_KEYSTORE_FILE -ErrorAction SilentlyContinue
    Remove-Item Env:AUDIOBOOSTER_KEYSTORE_PASSWORD -ErrorAction SilentlyContinue
    Remove-Item Env:AUDIOBOOSTER_KEY_ALIAS -ErrorAction SilentlyContinue
    Remove-Item Env:AUDIOBOOSTER_KEY_PASSWORD -ErrorAction SilentlyContinue
    $passwordText = $null
    $confirmationText = $null
}
