<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <title>gpsHub: Sign In</title>

    <script src="js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/user.min.js" type="text/javascript"></script>

    <link rel="StyleSheet" type="text/css" href="css/bootstrap.min.css">
</head>
<body>
<label for="id">id:</label>
<input id="id" class="form-control" value="1">
<label for="lat">lat:</label>
<input id="lat" class="form-control" value="37.61778" onchange="send()">
<label for="lng">lng:</label>
<input id="lng" class="form-control" value="145.75167" onchange="send()">
<script language="JavaScript">
    function send() {
        $.ajax({
            url: 'actions/Drivers.php',
            type: 'POST',
            data: {
                id: $("#id").val(),
                lat: $("#lat").val(),
                lng: $("#lng").val()
            },
            success: function (data) {
                console.log(data);
            }
        })
    }
</script>
</body>
</html>