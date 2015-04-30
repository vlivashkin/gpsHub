<?php

$green = "#5cb85c";
$yellow = "#f0ad4e";
$red = "#d9534f";
$white = "#fff";
$grey = "#ccc";

if (isset($_GET['status']) && isset($_GET['busy']) && isset($_GET['accuracy'])) {
	$status = $_GET['status'];
	$busy = $status == 'green' || $status == 'yellow' ? $_GET['busy'] : 'white';
	$accuracy = $_GET['accuracy'];
} else {
	echo "No enough parameters";
	exit();
}

header('Content-type: image/svg+xml');

echo <<<EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<svg xmlns="http://www.w3.org/2000/svg" xml:space="preserve" width="35px" height="35px" version="1.1" style="shape-rendering:geometricPrecision; fill-rule:evenodd; clip-rule:evenodd" viewBox="0 0 4233 4233">
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
        <path class="fil0 str2" d="M3745 2991c0,-64 -14,-125 -41,-181 -2,-14 -5,-28 -9,-43l-283 -701 -2 -6c-99,-295 -240,-567 -597,-567l-1365 0c-366,0 -496,279 -597,565l-296 703c-42,69 -67,147 -67,230l0 218c0,34 4,67 12,99 -7,19 -12,40 -12,62l0 648c0,96 78,173 173,173l357 0c95,0 172,-77 172,-173l0 -324 1874 0 0 324c0,96 77,173 172,173l337 0c95,0 172,-77 172,-173l0 -648c0,-22 -4,-43 -11,-62 7,-32 11,-65 11,-99l0 -218 0 0z"/>
        <path class="fil3 str1" d="M1230 3295l-378 0c-67,0 -123,-55 -123,-123 0,-68 56,-123 123,-123l378 0c68,0 123,55 123,123 0,68 -55,123 -123,123z"/>
        <path class="fil3 str1" d="M2587 3365l-912 0c-43,0 -75,-127 -75,-170 0,-43 35,-78 78,-78l877 0c43,0 78,35 78,78 0,43 -3,170 -46,170z"/>
        <path class="fil3 str1" d="M3354 3295l-378 0c-68,0 -123,-55 -123,-123 0,-68 55,-123 123,-123l378 0c68,0 123,55 123,123 0,68 -55,123 -123,123z"/>
        <path class="fil1 str1" d="M1051 2129c113,-318 203,-415 387,-415l1365 0c182,0 283,108 386,413l206 419c-67,-25 -141,-39 -219,-39l-2139 0c-70,0 -137,11 -199,32l213 -410z"/>
EOF;

if ($status == "green" || $status == "yellow") {
    echo <<<EOF
        <ellipse class="fil2 str2" cx="3744" cy="1151" rx="430" ry="447"/>
EOF;
}

echo <<<EOF
    </g>
</svg>
EOF;

