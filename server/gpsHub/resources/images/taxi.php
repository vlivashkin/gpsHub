<?php

$green = "#5cb85c";
$yellow = "#f0ad4e";
$red = "#d9534f";
$white = "#ffffff";

if (isset($_GET['status']) && isset($_GET['busy']) && isset($_GET['accuracy'])) {
	$status = $_GET['status'];
	$busy = $_GET['busy'];
	$accuracy = $_GET['accuracy'];
} else {
	echo "No enough parameters";
	exit();
}

header('Content-type: image/svg+xml');

echo <<<EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<svg xmlns="http://www.w3.org/2000/svg" xml:space="preserve" width="30px" height="40px" version="1.1"
style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
viewBox="0 0 3307 4365">
 <defs>
  <style type="text/css">
   <![CDATA[
    .str0 {stroke:black;stroke-width:20}
    .fil0 {fill:${$status}}
    .fil1 {fill:${$busy}}
    .fil2 {fill:${$accuracy}}
   ]]>
  </style>
 </defs>
 <g>
  <path class="fil0 str0" d="M2903 3439l-378 0c-68,0 -123,-56 -123,-123 0,-68 55,-123 123,-123l378 0c67,0 123,55 123,123 0,67 -56,123 -123,123zm-760 67l-913 0c-43,0 -74,-127 -74,-170 0,-43 35,-78 78,-78l877 0c43,0 78,35 78,78 0,43 -3,170 -46,170zm-1364 -67l-378 0c-68,0 -123,-56 -123,-123 0,-68 55,-123 123,-123l378 0c67,0 123,55 123,123 0,67 -56,123 -123,123zm-178 -1164c113,-318 204,-415 387,-415l1365 0c182,0 283,108 386,414l206 418c-67,-25 -141,-39 -219,-39l-2139 0c-70,0 -137,11 -199,32l213 -410zm2684 863c0,-64 -14,-126 -41,-182 -2,-14 -5,-28 -9,-42l-283 -702 -2 -6c-99,-294 -240,-567 -597,-567l-1365 0c-366,0 -496,279 -597,565l-296 703c-42,69 -67,147 -67,231l0 218c0,33 4,66 12,98 -8,19 -12,40 -12,62l0 649c0,95 77,172 173,172l357 0c95,0 172,-77 172,-172l0 -325 1873 0 0 325c0,95 78,172 173,172l337 0c95,0 172,-77 172,-172l0 -649c0,-22 -4,-43 -11,-62 7,-32 11,-65 11,-98l0 -218 0 0z"/>
EOF;

if ($status == "green" || $status == "yellow" || $busy == "white") {
    echo <<<EOF
  <path class="fil1 str0" d="M601 2275c113,-318 204,-415 387,-415l1365 0c182,0 283,108 386,414l206 418c-67,-25 -141,-39 -219,-39l-2139 0c-70,0 -137,11 -199,32l213 -410z"/>
EOF;
}

if ($status == "green" || $status == "yellow") {
    echo <<<EOF
  <path class="fil2 str0" d="M2104 367c-4,-18 -14,-37 -21,-54 -86,-206 -274,-280 -425,-280 -203,0 -426,136 -456,416l0 57c0,3 1,24 2,35 17,134 122,275 201,409 85,143 172,284 260,424 53,-91 107,-184 159,-274 14,-26 31,-52 45,-77 10,-17 28,-34 36,-49 85,-155 221,-311 221,-465l0 -63c0,-17 -20,-75 -22,-79zm-442 287c-60,0 -125,-29 -157,-112 -5,-13 -5,-39 -5,-42l0 -36c0,-105 89,-153 167,-153 95,0 169,76 169,172 0,95 -79,171 -174,171z"/>
EOF;
}

echo <<<EOF
 </g>
</svg>
EOF;
