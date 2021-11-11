Set-StrictMode -version 2.0

$DebugPreference = "SilentlyContinue"
$ErrorActionPreference = "stop"
$WarningPreference = "stop"

Set-Location (Split-Path $PSCommandPath)

$FileCsv = "{0}\{1}" -f (Get-Location).path, "worldcities.csv"
$filterPaese = "sm"
[double]$fattMolt = 200
$filterPaese = Read-Host -Prompt "Sigla del paese" 
switch ( $filterPaese ) {
  "sm" { $fattMolt = 200; Break}
  "it" { $fattMolt = 10; Break}
  "gb" { $fattMolt = 10; Break}
  "fr" { $fattMolt = 13; Break}
  "ru" { $fattMolt = 0.3; Break}
  default {    $fattMolt    = [convert]::ToDouble(   $(Read-Host -Prompt "Fattore Molt") ) ; break }
}

$tableCites = Import-Csv -Path $FileCsv -Delimiter ";" -Encoding UTF8
$rowMax = $tableCites.Count

# foreach ( $rig in $tableCites ) {
#   $st = $rig.iso2
#   if ( $st -ne $filterPaese ) { 
#     continue 
#   }
#   $rig
# }



class Citta {
  [string] $nome
  [double] $lat
  [double] $lon
  [string] $paese
  [int] $population
}

class punto {
  [double]$px
  [double]$py
  [int]$x
  [int]$y

}
class Vertice {
  [string]$id
  [punto]$punto
  [boolean]$start
  [boolean]$end
}


$colCitta = 2
$colLat = 3
$colLon = 4
$colPaese = 5
$colSigla = 6
$colPopul = 10


$pxMin,$pxMax = 99999999, 0
$pyMin,$pyMax = 99999999, 0


$arrCit = @()
[int]$oldPerc = -1
$totRighe = 1
foreach ( $rig in $tableCites ) {
  [int]$perc =[convert]::ToInt16( ( $totRighe++ / $rowMax ) * 100.0 )
  if ( $oldPerc -ne $perc ) {
    Write-Progress -Activity "Scansione" -Status "$perc% Letto" -PercentComplete $perc
    $oldPerc = $perc
  }
  $sigla = ($rig.iso2).toLower()
  if ( $filterPaese -ne $null -and $sigla -ne $filterPaese ) {
    continue
  }
  
  [Citta]$cit = New-Object -TypeName Citta
  $cit.nome = $rig.city_ascii
  $cit.lat  = [convert]::ToDouble( $rig.lat)
  $cit.lon  = [convert]::ToDouble( $rig.lng)
  $cit.paese = $rig.country
  # $sz = $rig.population  -replace "\.",""
  try {
    $cit.population  = [convert]::ToInt32($($rig.population  -replace "\.",""))
  } catch {
    Write-Output ( "Errore Polul={0}" -f $rig.population)    
    $cit.population  = -1
  }
  
  $pxMin = if ( $cit.lon -lt $pxMin ) {$cit.lon} else {$pxMin}
  $pxMax = if ( $cit.lon -gt $pxMax ) {$cit.lon} else {$pxMax}
  $pyMin = if ( $cit.lat -lt $pyMin ) {$cit.lat} else {$pyMin}
  $pyMax = if ( $cit.lat -gt $pyMax ) {$cit.lat} else {$pyMax}
  
  $arrCit += $cit

}
Write-Progress -Activity "Scansione" -Completed
Write-Output ("Sigla={0}, xMinMax=({1}; {2})" -f $filterPaese, $pxMin,$pxMax ) 
$liVertici = @()

foreach ( $cit in $arrCit ) {
  $ver = New-Object -TypeName Vertice
  $ver.id = $cit.nome
  $ver.start = $false
  $ver.end = $false
  $pu = New-Object -TypeName punto
  $px = ( $cit.lon - $pxMin ) * $fattMolt + 1
  $py = $cit.lat * $fattMolt

  $wx =  $px  / ( $pxMax - $pxMin )
  $wy =  $py  / ( $pyMax - $pyMin )
  $wx = $wx 
  $wy = $wy 
  $pu.px = $px # ( $px - $pxMin ) * 1000
  $pu.py = $py # ( $py - $pyMin ) * 1000
  $pu.x = [Math]::Floor($wx)
  $pu.y = [Math]::Floor($wy)
  $ver.punto = $pu
  $liVertici += $ver
}
Write-Output "Finito"

$myFile =  "{0}\WorldCities_{1}.json" -f (Get-Location).path, $filterPaese
"{ `"liVertici`": " | Out-File $myFile -Encoding utf8 
$liVertici | ConvertTo-Json -depth 100 | Out-File $myFile -Encoding utf8 -Append
"}" | Out-File $myFile -Encoding utf8 -Append
Write-Output ("Scritto `"{0}`" con {1} recs" -f $myFile, $arrCit.Count)