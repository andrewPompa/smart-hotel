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
    post('/api/room/reserve', JSON.stringify(data), onReservationSuccess, onSearchFault);
}

function onSearchFault(e) {
    const json = "<h4>Ajax Response</h4><pre>" + e.responseText + "</pre>";
    $('#feedback').html(`wystąpił błąd: ${json}`);
    $('#closeModalButton').click();
}

const onReservationSuccess = (data) => {
    console.log(data);
    if (data.newClient === true) {
        $('#feedback').html(`Pomyśnie zarezerwowano.<br />Nowy użytkownik ${data.code}`);
    } else {
        $('#feedback').html(`Pomyśnie zarezerwowano.<br />Użytkownik już istnieje`);
    }
    $('#closeModalButton').click();
};