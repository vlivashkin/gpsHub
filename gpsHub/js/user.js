function signInInit() {
    $('input').on('keyup', function(e) {
        if (e.which == 13) {
            e.preventDefault();
            signIn();
        }
    });

    $('#signin-form').submit(function (event) {
        event.preventDefault();
        signIn();
        return false;
    });
}

function signIn() {
    var $btn = $("#signin-btn");
    var email = $("#signin-email").val();
    var password = $("#signin-password").val();

    $("#signin-error").hide();
    $btn.prop('disabled', true);

    if (email == "") {
        signInMessage("email");
        $btn.prop('disabled', false);
    } else if (password == "") {
        signInMessage("password");
        $btn.prop('disabled', false);
    } else {
        $.ajax({
            url: 'actions/SignIn.php',
            type: 'GET',
            data: {
                signin: true,
                email: email,
                password: password
            },
            success: function(data) {
                if (data === "yes") {
                    signInMessage("goodpass");
                    window.location.href = "index.php";
                } else if (data === "no") {
                    signInMessage("badpass");
                    $btn.prop('disabled', false);
                }
            }
        });
    }


}

function signInMessage(type) {
    var $err = $("#signin-error");
    $err.hide();
    $err.removeClass("bs-callout-warning");
    $err.removeClass("bs-callout-danger");
    $err.removeClass("bs-callout-info");

    switch (type) {
        case "email" :
            $err.addClass("bs-callout-warning");
            $err.find("p").text("Введите email.");
            break;
        case "password" :
            $err.addClass("bs-callout-warning");
            $err.find("p").text("Введите пароль.");
            break;
        case "badpass" :
            $err.addClass("bs-callout-danger");
            $err.find("p").text("Неверное имя пользователя или пароль.");
            break;
        case "goodpass" :
            $err.addClass("bs-callout-info");
            $err.find("p").text("Пожалуйста, подождите...");
            break;
    }

    $err.show();
}

function signOut() {
    $.ajax({
        url: 'actions/SignOut.php',
        type: 'GET',
        success: function(data) {
            location.reload();
        }
    });
}