let reservations = [];
let editedReservation = null;
$(document).ready(function () {
    getAll();
    $('#changeForm').submit(onSubmitChangeForm);
});

function getAll() {
    get("/admin/api/reservation/all", null, responseSuccess, (e) => {
        console.log('error' + JSON.stringify(e))
    })
}

function responseSuccess(data) {
    reservations = data;
    const rows = data.map((rowData, index) => {
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
                        <button class="btn btn-primary" onclick="onClickChangeReservationButton(${index})">Zmień dane</button>
                    </td>
                    <td>
                        <button class="btn btn-danger" onclick="onClickDeleteReservationButton(${rowData.id})">Usuń</button>
                    </td>
                </tr>`;
    });
    $('#test > tbody:last-child').html("");
    rows.forEach(row => {
        $('#test > tbody:last-child').append(row);
    });
}

function onClickChangeReservationButton(index) {
    const reservation = reservations[index];
    editedReservation = reservation;
    $('#reservationNumber').html(`Rezerwacja numer: ${reservation.id}`);
    console.log(reservation.fromDay);
    console.log(reservation.toDay);
    $('#inputFromDate').val(reservation.fromDay);
    $('#inputToDate').val(reservation.toDay);
}

function onClickDeleteReservationButton(id) {
    console.log('on delete reservation');
    sendDelete(
        `/admin/api/reservation/${id}`,
        () => {
            getAll()
        },
        (e) => {
            console.log(e);
            $('#feedback').html("Nie można usunąć rezerwacji!<br />" + e.responseText);
        }
    );
}

function onSubmitChangeForm(e) {
    e.preventDefault();
    console.log('on submit');
    if (editedReservation == null) {
        return;
    }
    const fromDay = $('#inputFromDate').val();
    const toDay = $('#inputToDate').val();
    const data = JSON.stringify({fromDay: fromDay, toDay: toDay});
    post(
        `/admin/api/reservation/${editedReservation.id}`,
        data,
        () => {getAll()},
        (e) => { $('#feedback').html("Nie można zmodyfikować rezerwacji!<br />" + e.responseText);});
}