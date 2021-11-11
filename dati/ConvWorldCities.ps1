Set-StrictMode -version 2.0

$DebugPreference = "SilentlyContinue"
$ErrorActionPreference = "stop"
$WarningPreference = "stop"

Set-Location (Split-Path $PSCommandPath)

$FileXslx = "{0}\{1}" -f (Get-Location).path, "worldcities.xlsx"
$filterPaese = "it"
$ExcelApp = $excel = New-Object -ComObject excel.application  
$WorkB = $ExcelApp.Workbooks.Open($FileXslx)
$Sheet = $WorkB.worksheets.item(1)
$rowMax = ( $Sheet.UsedRange.rows).count

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
For ( $rig = 2 ; $rig -le $rowMax ; $rig++ ) {
  [int]$perc =[convert]::ToInt16( ( $rig / $rowMax ) * 100.0 )
  if ( $oldPerc -ne $perc ) {
    Write-Progress -Activity "Scansione" -Status "$perc% Letto" -PercentComplete $perc
    $oldPerc = $perc
  }
  $sigla = ($Sheet.cells.item($rig, $colSigla).text).toLower()
  if ( $filterPaese -ne $null -and $sigla -ne $filterPaese ) {
    continue
  }
  
  [Citta]$cit = New-Object -TypeName Citta
  $cit.nome = $Sheet.cells.item($rig, $colCitta).text
  $cit.lat  = [convert]::ToDouble(   $Sheet.cells.item($rig, $colLat).text)
  $cit.lon  = [convert]::ToDouble(   $Sheet.cells.item($rig, $colLon).text)
  $cit.paese = $Sheet.cells.item($rig, $colPaese).text
  $sz = $Sheet.cells.item($rig, $colPopul).text -replace "\.",""
  $cit.population  = [convert]::ToInt32($sz)
  
  $pxMin = if ( $cit.lon -lt $pxMin ) {$cit.lon} else {$pxMin}
  $pxMax = if ( $cit.lon -gt $pxMax ) {$cit.lon} else {$pxMax}
  $pyMin = if ( $cit.lat -lt $pyMin ) {$cit.lat} else {$pyMin}
  $pyMax = if ( $cit.lat -gt $pyMax ) {$cit.lat} else {$pyMax}
  
  $arrCit += $cit

}
Write-Progress -Completed
$liVertici = @()
foreach ( $cit in $arrCit ) {
  $ver = New-Object -TypeName Vertice
  $ver.id = $cit.nome
  $ver.start = $false
  $ver.end = $false
  $pu = New-Object -TypeName punto
  $px = $cit.lon
  $py = $cit.lat

  $wx = ( $pxMax - $px ) / ( $pxMax - $pxMin )
  $wy = ( $pyMax - $py ) / ( $pyMax - $pyMin )
  $wx = $wx * 600
  $wy = $wy * 400
  $pu.px = ( $px - $pxMin ) * 1000
  $pu.py = ( $py - $pyMin ) * 1000
  $pu.x = [Math]::Floor($wx)
  $pu.y = [Math]::Floor($wy)
  $ver.punto = $pu
  $liVertici += $ver
}
Write-Host "Finito"
print $liVertici.count
$myFile =  "{0}\WorldCities_{1}.json" -f (Get-Location).path, $filterPaese
$liVertici | ConvertTo-Json -depth 100 | Out-File $myFile -Encoding utf8
