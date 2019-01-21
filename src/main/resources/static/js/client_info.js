$(document).ready(function () {
    getAll();
});

function onClickPayButton() {
    get('/client/pay',
        null,
        (data) => {
            setDebt(data.debt);
        },
        faultHandler
    );
}

function faultHandler(e) {
    $('#feedback').html(`wystąpił błąd: <pre> ${e.responseText}</pre>`);
}

const responseSuccess = (data) => {
    const rows = data.reservations.map((rowData, index) => {
        let list = "";
        rowData.rooms.forEach(room => {
            list += `pokój ${room.name}, il osób: ${room.size}, ${room.type}<br/>`;
        });
        return `<tr>
                    <th scope="row">${(index + 1)}</th>
                    <th scope="row">${rowData.fromDay}</th>
                    <th scope="row">${rowData.toDay}</th>
                    <td>${list}</td>
                    <td>${rowData.roomPrice}</td>
                    <td>${rowData.client.firstName} ${rowData.client.lastName}</td>
                    <td>
                        <button class="btn btn-danger" onclick="onClickDeleteReservationButton(${rowData.id})">Usuń</button>
                    </td>
                </tr>`;
    });
    $('#userInfo').html(`${data.firstName} ${data.lastName}`);
    setDebt(data.debt);
    $('#test > tbody:last-child').html("");
    rows.forEach(row => {
        $('#test > tbody:last-child').append(row);
    });
};

function setDebt(debt) {
    if (debt > 0) {
        $('#toPay').html(`Do zapłaty: ${debt}`);
        $('#toPayButton').removeAttr('disabled');
    } else {
        console.log('disabled should be');
        $('#toPay').html(`Do zapłaty: 0.0`);
        $('#toPayButton').attr("disabled", "disabled");
    }
}

function getAll() {
    get("/client/api/info", null, responseSuccess, faultHandler);
}

function onClickDeleteReservationButton(id) {
    console.log('on delete reservation');
    sendDelete(
        `/client/api/reservation/${id}`,
        () => {
            getAll()
        },
        (e) => {
            console.log(e);
            $('#feedback').html("Nie można usunąć rezerwacji!<br />" + e.responseText);
        }
    );
}