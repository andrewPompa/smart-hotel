$(document).ready(function () {
    getAll();
    $("#addForm").submit(submitAddForm);
});
function getAll() {
    get("/admin/api/room/all", null, responseSuccess, (e) => {
        console.log('error' + JSON.stringify(e))
    })
}

function submitAddForm() {
    event.preventDefault();
    const data = {
        name: $("#inputName")[0].value,
        size: $("#inputSize")[0].value,
        type: $("#inputStandard")[0].value,
    };
    post("/admin/api/room", JSON.stringify(data), onAddSuccess, onAddFault)
}

function onAddSuccess() {
    getAll();
}

function onAddFault(e) {
    const json = "<pre>" + e.responseText + "</pre>";
    $('#feedback').html(`wystąpił błąd: ${json}`);
}

const responseSuccess = (response) => {
    console.log(response);
    const rows = response.map((room) => {
        return `<tr>
                    <th scope="row">${room.name}</th>
                    <td>${room.size}</td>
                    <td>${room.type}</td>
                    <td>
                        <button class="btn btn-danger" onclick="onDeleteRoom(${room.id})">Usuń</button>
                    </td>
                </tr>`;
    });
    $('#test > tbody:last-child').html("");
    rows.forEach(row => {
        $('#test > tbody:last-child').append(row);
    });
};

function onDeleteRoom(id) {
    console.log('on delete room');
    sendDelete(
        `/admin/api/room/${id}`,
        () => {
            getAll()
        },
        (e) => {
            $('#feedback').html("Nie można usunąć pokoju!<br />" + e.responseText);
        }
    );
}