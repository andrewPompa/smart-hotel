$(document).ready(function () {
    let numOfPeoplesRange = $("#inputNumOfPeoples");
    $("#labelNumOfPeoples").html("Ilość osób: " + numOfPeoplesRange[0].value);
    $('#inputFromDate').val(new Date().toDateInputValue());
    $('#inputToDate').val(new Date().toDateInputValue());

    numOfPeoplesRange.on('input', function () {
        $("#labelNumOfPeoples").html("Ilość osób: " + numOfPeoplesRange[0].value);
    });


    $("#searchForm").submit(function () {
        event.preventDefault();
        const data = {
            from: $("#inputFromDate")[0].value,
            to: $("#inputToDate")[0].value,
            standard: $("#inputStandard")[0].value,
            numOfPeoples: numOfPeoplesRange[0].value
        };
        get("/api/room/search", data, onSearchSuccess, onSearchFault)
    });
});

function onSearchSuccess(data) {
    var json = "<h4>Ajax Response</h4><pre>" + JSON.stringify(data, null, 4) + "</pre>";
    $('#feedback').html(json);

}

function onSearchFault(e) {
    var json = "<h4>Ajax Response</h4><pre>" + e.responseText + "</pre>";
    $('#feedback').html(json);
}

function get(url, data, successHandler, errorHandler) {
    sendAjax("GET", url, data, successHandler, errorHandler)
}

function post(url, data, successHandler, errorHandler) {
    sendAjax("POST", url, data, successHandler, errorHandler)
}

function sendAjax(type, url, data, successHandler, errorHandler) {
    const ajax = {};
    ajax.type = type;
    ajax.url = url;
    ajax.dataType = "json";
    if (type.toUpperCase() == "POST") {
        ajax.contentType = "application/json";
    }
    if (data != null) {
        ajax.data = data;
    }
    if (successHandler != null) {
        ajax.success = successHandler;
    }
    if (errorHandler != null) {
        ajax.error = errorHandler;
    }
    console.log(ajax);
    $.ajax(ajax);
}

Date.prototype.toDateInputValue = (function() {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});