$(document).ready(function () {
    getAll();
});
const responseSuccess = (response) => {
    console.log(response);
    const rows = response.map((client, index) => {
        return `<tr>
                    <th scope="row">${(index + 1)}</th>
                    <td>${client.firstName}</td>
                    <td>${client.lastName}</td>
                    <td>${client.debt}</td>
                    <td>
                        <button class="btn btn-danger" onclick="onDeleteUser(${client.id})">Usuń</button>
                    </td>
                    <td>
                    <!--href="/admin/client/${client.id}"-->
                        <button class="btn btn-outline-light" onclick="onClickSeeReservation(${client.id})">Zobacz rezerwacje</button>
                    </td>
                </tr>`;
    });
    $('#test > tbody:last-child').html("");
    rows.forEach(row => {
        $('#test > tbody:last-child').append(row);
    });
};

function getAll() {
    get("/admin/api/client/all", null, responseSuccess, (e) => {
        console.log('error' + JSON.stringify(e))
    })
}

function onDeleteUser(id) {
    console.log('on delete user');
    sendDelete(
        `/admin/api/client/${id}`,
        () => {
            getAll()
        },
        (e) => {
            console.log(e);
            $('#feedback').html("Nie można usunąć użytkownika!<br />" + e.responseText);
        }
    );
}

function onClickSeeReservation(id) {
    get(
        `/admin/api/client/${id}`,
        null,
        (response) => {
            console.log(response);
            if (response.reservations == null) {
                return;
            }
            const rows = response.reservations.map((rowData) => {
                let list = "";
                rowData.rooms.forEach(room => {
                    list += `pokój ${room.name}, il osób: ${room.size}, ${room.type}<br/>`;
                });
                return `<tr>
                    <td>${rowData.fromDay}</td>
                    <td>${rowData.toDay}</td>
                    <td>${list}</td>
                    <td>${rowData.roomPrice}</td>
                </tr>`;
            });
            $('#clientData > tbody:last-child').html("");
            rows.forEach(row => {
                $('#clientData > tbody:last-child').append(row);
            });
        },
        (e) => $('#feedback').html("Nie można pokazać danych użytkownika!<br />" + e.responseText)
    );
}