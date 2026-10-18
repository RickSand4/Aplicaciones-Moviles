$ErrorActionPreference = 'Stop'
$workspace = 'C:\Documentos\AppsWebs\PRACTICA_EOHS_01'
$target = 'C:\Documentos\AppsWebs\EXAMEN_EOHS_01'
$stage = Join-Path $workspace 'examen_reestructura'
$backup = Join-Path $workspace 'examen_antes_reestructura'
$baseline = Get-Content -LiteralPath (Join-Path $workspace 'examen_reestructura_baseline.json') -Raw | ConvertFrom-Json -AsHashtable
$changes = Get-Content -LiteralPath (Join-Path $workspace 'examen_reestructura_changes.json') -Raw | ConvertFrom-Json

#Comprobar los archivos antes de integrar la estructura nueva.
foreach ($relative in @($changes.writes) + @($changes.deletes)) {
    $destination = [IO.Path]::GetFullPath((Join-Path $target $relative))
    if (-not $destination.StartsWith($target + '\', [StringComparison]::OrdinalIgnoreCase)) {
        throw "Ruta fuera del proyecto: $relative"
    }
    if ($baseline.ContainsKey($relative)) {
        if (-not (Test-Path -LiteralPath $destination)) { throw "Falta el archivo original: $relative" }
        if ((Get-FileHash -LiteralPath $destination -Algorithm SHA256).Hash -ne $baseline[$relative]) {
            throw "El archivo cambió durante el trabajo: $relative. No se escribió ningún archivo."
        }
    } elseif (Test-Path -LiteralPath $destination) {
        throw "El archivo nuevo ya existe: $relative"
    }
}
foreach ($relative in $changes.writes) {
    if (-not (Test-Path -LiteralPath (Join-Path $stage $relative))) { throw "Falta el resultado: $relative" }
}

#Guardar los originales, incluyendo las clases que se reemplazan.
foreach ($relative in @($changes.writes) + @($changes.deletes)) {
    $original = Join-Path $target $relative
    if (Test-Path -LiteralPath $original) {
        $saved = Join-Path $backup $relative
        New-Item -ItemType Directory -Path (Split-Path $saved) -Force | Out-Null
        Copy-Item -LiteralPath $original -Destination $saved
    }
}

foreach ($relative in $changes.writes) {
    $destination = Join-Path $target $relative
    New-Item -ItemType Directory -Path (Split-Path $destination) -Force | Out-Null
    Copy-Item -LiteralPath (Join-Path $stage $relative) -Destination $destination -Force
}
foreach ($relative in $changes.deletes) {
    Remove-Item -LiteralPath (Join-Path $target $relative)
}
Write-Output "Reestructura aplicada. Originales guardados en $backup"
