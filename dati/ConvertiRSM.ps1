$myFile = "F:\java\photon2\plotge\dati\WorldCities_sanmarino.json"
$pippo = Get-Content -Path $myFile | Out-String | ConvertFrom-Json

$pxMin,$pxMax = 99999999, 0
$pyMin,$pyMax = 99999999, 0

forEach ( $ve  in $pippo.liVertici ) {
  $punto = $ve.punto
  $px = $punto.px
  $py = $punto.py
  $pxMin = if ( $px -lt $pxMin ) {$px} else {$pxMin}
  $pxMax = if ( $px -gt $pxMax ) {$px} else {$pxMax}
  $pyMin = if ( $py -lt $pyMin ) {$py} else {$pyMin}
  $pyMax = if ( $py -gt $pyMax ) {$py} else {$pyMax}
}
("pxMin={0}, pxMax={1}" -f $pxMin, $pxMax)
("pyMin={0}, pyMax={1}" -f $pyMin, $pyMax)

forEach ( $ve  in $pippo.liVertici ) {
  $punto = $ve.punto
  $px = $punto.px
  $py = $punto.py
  $wx = ( $pxMax - $px ) / ( $pxMax - $pxMin )
  $wy = ( $pyMax - $py ) / ( $pyMax - $pyMin )
  $wx = $wx * 600
  $wy = $wy * 400
  $punto.px = ( $px - $pxMin ) * 1000
  $punto.py = ( $py - $pyMin ) * 1000
  $punto.x = [Math]::Floor($wx)
  $punto.y = [Math]::Floor($wy)
}

$myFile = "F:\java\photon2\plotge\dati\WorldCities_sanmarino_2.json"
$pippo | ConvertTo-Json -depth 100 | Out-File $myFile -Encoding utf8

