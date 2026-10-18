$ErrorActionPreference = 'Stop'
$workspace = 'C:\Documentos\AppsWebs\PRACTICA_EOHS_01'
$stage = Join-Path $workspace 'examen_work'
$target = 'C:\Documentos\AppsWebs\EXAMEN_EOHS_01'
$baseline = Get-Content -LiteralPath (Join-Path $workspace 'examen_baseline.json') -Raw | ConvertFrom-Json -AsHashtable
$changes = Get-Content -LiteralPath (Join-Path $workspace 'examen_changes.json') -Raw | ConvertFrom-Json
$backup = Join-Path $workspace 'examen_backup'
foreach ($relative in $changes) {
    $destination = [IO.Path]::GetFullPath((Join-Path $target $relative))
    if (-not $destination.StartsWith($target + '\', [StringComparison]::OrdinalIgnoreCase)) { throw 'Ruta fuera del proyecto del examen.' }
    if ($baseline.ContainsKey($relative)) {
        if (-not (Test-Path -LiteralPath $destination)) { throw "Falta el original: $relative" }
        if ((Get-FileHash -LiteralPath $destination -Algorithm SHA256).Hash -ne $baseline[$relative]) { throw "Hay cambios recientes del usuario en $relative. No se ha escrito ningún archivo." }
    } elseif (Test-Path -LiteralPath $destination) {
        throw "Ya existe un archivo nuevo: $relative. No se ha escrito ningún archivo."
    }
    if (-not (Test-Path -LiteralPath (Join-Path $stage $relative))) { throw "Falta el resultado: $relative" }
}
foreach ($relative in $changes) {
    $destination = Join-Path $target $relative
    if (Test-Path -LiteralPath $destination) {
        $saved = Join-Path $backup $relative
        New-Item -ItemType Directory -Path (Split-Path $saved) -Force | Out-Null
        Copy-Item -LiteralPath $destination -Destination $saved
    }
    New-Item -ItemType Directory -Path (Split-Path $destination) -Force | Out-Null
    Copy-Item -LiteralPath (Join-Path $stage $relative) -Destination $destination -Force
}
Write-Output ("Aplicados {0} archivos a {1}" -f $changes.Count, $target)
