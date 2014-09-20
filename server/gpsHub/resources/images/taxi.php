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
<svg xmlns="http://www.w3.org/2000/svg" xml:space="preserve" width="27px" height="37px" version="1.1"
style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
viewBox="0 0 3307 4365">
 <defs>
  <style type="text/css">
   <![CDATA[
    .str0 {stroke:black;stroke-width:20}
    .str1 {stroke:black;stroke-width:40}
    .str2 {stroke:black;stroke-width:70}
    .fil0 {fill:${$status}}
    .fil1 {fill:${$busy}}
    .fil2 {fill:${$accuracy}}
    .fil3 {fill:white}
   ]]>
  </style>
 </defs>
 <g>
EOF;

if ($status == "green" || $status == "yellow") {
    echo <<<EOF
        <path class="fil2 str2" d="M3064 1211c-15,-56 -45,-116 -68,-169 -270,-648 -859,-881 -1336,-881 -637,0 -1339,427 -1433,1309l0 180c0,7 3,74 7,108 52,420 383,866 631,1286 266,450 542,892 816,1335 169,-289 337,-581 502,-863 45,-82 97,-164 142,-243 30,-53 87,-105 113,-154 266,-487 695,-978 695,-1462l0 -199c0,-52 -65,-236 -69,-247zm-1392 903c-187,0 -392,-93 -494,-352 -15,-41 -13,-124 -13,-131l0 -117c0,-329 280,-479 523,-479 300,0 532,240 532,540 0,299 -248,539 -548,539z"/>
EOF;
}

echo <<<EOF
  <path class="fil3 str0" d="M779 3439l-378 0c-68,0 -123,-56 -123,-123 0,-68 55,-123 123,-123l378 0c67,0 123,55 123,123 0,67 -56,123 -123,123z"/>
  <path class="fil3 str0" d="M2143 3506l-913 0c-43,0 -74,-127 -74,-170 0,-43 35,-78 78,-78l877 0c43,0 78,35 78,78 0,43 -3,170 -46,170z"/>
  <path class="fil3 str0" d="M2903 3439l-378 0c-68,0 -123,-56 -123,-123 0,-68 55,-123 123,-123l378 0c67,0 123,55 123,123 0,67 -56,123 -123,123z"/>
  <path class="fil0 str2" d="M2903 3439l-378 0c-68,0 -123,-56 -123,-123 0,-68 55,-123 123,-123l378 0c67,0 123,55 123,123 0,67 -56,123 -123,123zm-760 67l-913 0c-43,0 -74,-127 -74,-170 0,-43 35,-78 78,-78l877 0c43,0 78,35 78,78 0,43 -3,170 -46,170zm-1364 -67l-378 0c-68,0 -123,-56 -123,-123 0,-68 55,-123 123,-123l378 0c67,0 123,55 123,123 0,67 -56,123 -123,123zm-178 -1164c113,-318 204,-415 387,-415l1365 0c182,0 283,108 386,414l206 418c-67,-25 -141,-39 -219,-39l-2139 0c-70,0 -137,11 -199,32l213 -410zm2684 863c0,-64 -14,-126 -41,-182 -2,-14 -5,-28 -9,-42l-283 -702 -2 -6c-99,-294 -240,-567 -597,-567l-1365 0c-366,0 -496,279 -597,565l-296 703c-42,69 -67,147 -67,231l0 218c0,33 4,66 12,98 -8,19 -12,40 -12,62l0 649c0,95 77,172 173,172l357 0c95,0 172,-77 172,-172l0 -325 1873 0 0 325c0,95 78,172 173,172l337 0c95,0 172,-77 172,-172l0 -649c0,-22 -4,-43 -11,-62 7,-32 11,-65 11,-98l0 -218 0 0z"/>
EOF;

if ($status == "green" || $status == "yellow" || $busy == "white") {
    echo <<<EOF
        <path class="fil1 str0" d="M601 2275c113,-318 204,-415 387,-415l1365 0c182,0 283,108 386,414l206 418c-67,-25 -141,-39 -219,-39l-2139 0c-70,0 -137,11 -199,32l213 -410z"/>
EOF;
}

echo <<<EOF
 </g>
</svg>
EOF;

