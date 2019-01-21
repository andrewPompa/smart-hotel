$(document).ready(function () {
    $("#registerForm").submit(function () {
        event.preventDefault();
        onClickRegisterButton();
    });
});
function onClickRegisterButton() {
    const data = {
        login: $('#registerUsername').val(),
        password: $('#registerPassword').val(),
        firstName: $('#registerFirstName').val(),
        lastName: $('#registerLastName').val(),
        city: $('#registerCity').val(),
        street: $('#registerStreet').val(),
        flatNumber: $('#registerFlatNumber').val(),
    };
    console.log(data);
    post("/register/client-form",
        JSON.stringify(data),
        onSearchSuccess,
        onSearchFault);
}
function onSearchSuccess(data) {
    console.log('OK');
    window.location.href = "/login/client";
}

function onSearchFault(e) {
    const json = "<pre>" + e.responseText + "</pre>";
    $('#feedback').html(`wystąpił błąd: ${json}`);
}