let client;
$(document).ready(function () {
    getData();
    $("#registerForm").submit(function () {
        event.preventDefault();
        onClickRegisterButton();
    });
});

function getData() {
    get("/client/api/info", null, responseSuccess, faultHandler);
}

const responseSuccess = (data) => {
    client = data;
    $('#registerUsername').val(data.login);
    $('#registerPassword').val(data.password);
    $('#registerFirstName').val(data.firstName);
    $('#registerLastName').val(data.lastName);
    $('#registerCity').val(data.city);
    $('#registerStreet').val(data.street);
    $('#registerFlatNumber').val(data.flatNumber);
};

function onClickRegisterButton() {
    const data = {
        id: client.id,
        firstName: $('#registerFirstName').val(),
        lastName: $('#registerLastName').val(),
        city: $('#registerCity').val(),
        street: $('#registerStreet').val(),
        flatNumber: $('#registerFlatNumber').val(),
    };
    console.log(data);
    post("/client/api/update",
        JSON.stringify(data),
        onSearchSuccess,
        faultHandler);
}

function onSearchSuccess(data) {
    console.log('OK');
    window.location.href = "/client/info";
}

function faultHandler(e) {
    const json = "<pre>" + e.responseText + "</pre>";
    $('#feedback').html(`wystąpił błąd: ${json}`);
}