function signInInit() {
    $('#signin-form').submit(function (event) {
        event.preventDefault();
        signIn();
        return false;
    });
}

function signIn() {
    var login = $("#signin-login").val();
    var password = $("#signin-password").val();

    $("#signin-error").hide();

    if (login == "") {
        responseMsg("login");
    } else if (password == "") {
        responseMsg("password");
    } else {
        $("#signin-btn").prop('disabled', true);
        responseMsg("pleasewait");
        sendRequest();
    }
}

function sendRequest() {
    var login = $("#signin-login").val();
    var password = $("#signin-password").val();

    var shaObj = new jsSHA(password + login, "TEXT");
    var hash = shaObj.getHash("SHA-256", "HEX");

    $.ajax({
        url: 'actions/signin.php',
        type: 'POST',
        data: {
            login: login,
            hash: hash
        },
        success: function (data) {
            requestResult(data);
        },
        error: function () {
            responseMsg("connectionerror");
            $("#signin-btn").prop('disabled', false);
        }
    });
}

function requestResult(data) {
    if (data === "SUCCESS") {
        responseMsg("pleasewait");
        window.location.href = "index.php";
    } else if (data === "FAILURE") {
        responseMsg("badpass");
        $("#signin-btn").prop('disabled', false);
    } else {
        responseMsg("connectionerror");
        $("#signin-btn").prop('disabled', false);
    }
}

function responseMsg(type) {
    var $err = $("#signin-error");
    $err.hide();
    $err.removeClass("bs-callout-warning");
    $err.removeClass("bs-callout-danger");
    $err.removeClass("bs-callout-info");

    switch (type) {
        case "login" :
            $err.addClass("bs-callout-warning");
            $err.find("p").text("Введите login.");
            break;
        case "password" :
            $err.addClass("bs-callout-warning");
            $err.find("p").text("Введите пароль.");
            break;
        case "badpass" :
            $err.addClass("bs-callout-danger");
            $err.find("p").text("Неверное имя пользователя или пароль.");
            break;
        case "pleasewait" :
            $err.addClass("bs-callout-info");
            $err.find("p").text("Пожалуйста, подождите...");
            break;
        case "connectionerror":
            $err.addClass("bs-callout-danger");
            $err.find("p").text("Произошла ошибка во время связи с сервером. Попробуйте обновить страницу");
            break;
    }
    $err.show();
}

function signOut() {
    $.ajax({
        url: 'actions/signout.php',
        type: 'GET',
        success: function () {
            window.location.replace("signin.php");
        }
    });
}