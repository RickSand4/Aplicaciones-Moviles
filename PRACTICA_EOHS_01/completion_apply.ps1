$ErrorActionPreference = 'Stop'
$workspace = 'C:\Documentos\AppsWebs\PRACTICA_EOHS_01'
$stage = Join-Path $workspace 'completion_work'
$target = 'C:\Documentos\AppsWebs\Practica_Whatsapp'
$baseline = Get-Content -LiteralPath (Join-Path $workspace 'completion_baseline.json') -Raw | ConvertFrom-Json -AsHashtable
$paths = Get-Content -LiteralPath (Join-Path $workspace 'completion_changes.json') -Raw | ConvertFrom-Json
$backup = Join-Path $workspace 'completion_backup'
# Verificar todos los destinos antes de escribir; conservar cambios del usuario.
foreach ($relative in $paths) {
    $destination = [IO.Path]::GetFullPath((Join-Path $target $relative))
    if (-not $destination.StartsWith($target + '\', [StringComparison]::OrdinalIgnoreCase)) { throw 'Destino fuera del proyecto.' }
    $key = $relative.Replace('/','\')
    if ($baseline.ContainsKey($key)) {
        if (-not (Test-Path -LiteralPath $destination)) { throw "Falta el archivo original: $relative" }
        if ((Get-FileHash -LiteralPath $destination -Algorithm SHA256).Hash -ne $baseline[$key]) { throw "El usuario modificó: $relative. No se aplicó ningún archivo." }
    } elseif (Test-Path -LiteralPath $destination) {
        throw "Ya existe un archivo nuevo: $relative. No se aplicó ningún archivo."
    }
    if (-not (Test-Path -LiteralPath (Join-Path $stage $relative))) { throw "Falta el resultado: $relative" }
}
foreach ($relative in $paths) {
    $destination = Join-Path $target $relative
    if (Test-Path -LiteralPath $destination) {
        $copy = Join-Path $backup $relative
        New-Item -ItemType Directory -Path (Split-Path $copy) -Force | Out-Null
        Copy-Item -LiteralPath $destination -Destination $copy
    }
    New-Item -ItemType Directory -Path (Split-Path $destination) -Force | Out-Null
    Copy-Item -LiteralPath (Join-Path $stage $relative) -Destination $destination -Force
}
Write-Output ("Aplicados {0} archivos a {1}" -f $paths.Count,$target)

