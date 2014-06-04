
function signInInit() {
    $("#signin-email").click(function() {
        signIn();
    });
    $("#signin-password").click(function() {
        signIn();
    });
}

function signIn() {
    $.ajax({
        url: 'classes/SignIn.php',
        type: 'GET',
        data: {
            signin: true,
            email: $("#signin-email").val(),
            password: $("#signin-password").val()
        },
        success: function(data) {
            window.location.href = "index.php";
        }
    });
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