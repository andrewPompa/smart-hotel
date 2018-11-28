let roomIds = [];
let reservationFrom;
let reservationToId;

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

    const rows = data.map((rowData, index) => {
        let list = "";
        let ids = [];
        console.log(rowData.from);
        console.log(rowData.to);
        rowData.rooms.forEach(room => {
            list += `pokój ${room.name}, il osób: ${room.size}, ${room.type}<br/>`;
            ids.push(room.id);
        });
        console.log(ids);
        return `<tr>
                    <th scope="row">${(index + 1)}</th>
                    <td>${list}</td>
                    <td>${rowData.roomsPrice}</td>
                    <td>
                        <button class="btn btn-primary" data-toggle="modal" data-target="#myModal" onclick="onClickReserveRoomsButton('${list}', ${rowData.roomsPrice}, '${ids}', '${rowData.from}', '${rowData.to}')">
                            Rezerwuj
                        </button>
                    </td>
                </tr>`;
    });
    $('#test > tbody:last-child').html("");
    rows.forEach(row => {
        $('#test > tbody:last-child').append(row);
    });
}

function onClickReserveRoomsButton(rooms, price, ids, from, to) {
    console.log(rooms);
    console.log(price);
    $('#modalRooms').html(rooms);
    $('#modalPrice').html(price);
    console.log(ids);
    roomIds = ids.split(',');
    reservationFromId = from;
    reservationToId = to;

}
function onClickSendReservationButton() {
    const email = $('#userEmail').val();
    const firstName = $('#userFirstName').val();
    const lastName = $('#userLastName').val();
    const data = {email: email, firstName: firstName, lastName: lastName, roomIds: roomIds, from: reservationFromId, to: reservationToId};
    console.log(roomIds);
    console.log(data);
    post('/api/room/reserve', JSON.stringify(data), () => {console.log('goood')}, () => {console.log('not ok')});
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
    if (type.toUpperCase() === "POST") {
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
    $.ajax(ajax);
}

Date.prototype.toDateInputValue = (function () {
    const local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0, 10);
});