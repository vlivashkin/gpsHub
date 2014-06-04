function signInInit() {
    $('input').on('keyup', function(e) {
        if (e.which == 13) {
            e.preventDefault();
            signIn();
        }
    });
}

function signIn() {
    var $err = $("#signin-error");
    var email = $("#signin-email").val();
    var password = $("#signin-password").val();

    if (email == "") {
        $err.removeClass("bs-callout-danger");
        $err.addClass("bs-callout-warning");
        $err.find("p").text("Введите email.");
        $err.show();
    } else if (password == "") {
        $err.removeClass("bs-callout-danger");
        $err.addClass("bs-callout-warning");
        $err.find("p").text("Введите пароль.");
        $err.show();
    } else {
        $.ajax({
            url: 'classes/SignIn.php',
            type: 'GET',
            data: {
                signin: true,
                email: email,
                password: password
            },
            success: function(data) {
                if (data === "yes") {
                    $("#signin-error").hide();
                    window.location.href = "index.php";
                } else if (data === "no") {
                    $err.removeClass("bs-callout-warning");
                    $err.addClass("bs-callout-danger");
                    $err.find("p").text("Неверное имя пользователя или пароль.");
                    $("#signin-error").show();
                }
            }
        });
    }
}

function signOut() {
    $.ajax({
        url: 'classes/SignOut.php',
        type: 'GET',
        success: function(data) {
            location.reload();
        }
    });
}