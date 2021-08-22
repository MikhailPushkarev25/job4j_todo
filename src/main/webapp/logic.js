

$('document').ready(function () {
    doSend();
    setInterval(() => {
        console.log('After timeout')
    }, 10000)
});

function validate() {
    let addBtn = document.getElementById("myInput").value;
    if (addBtn === '' || addBtn === null) {
        alert('Вы не указали задачу!');
        return false;
    }
    return true;
}

function doSend() {
    if (validate()) {
        console.log("button");
        let describe = document.getElementById("myInput").value;
        console.log(describe);
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/todo/add',
            data: {
                id: 0,
                des: describe
            }
        }).done(function () {
            console.log("success");
            $.ajax({
                type: 'GET',
                crossdomain: true,
                url: 'http://localhost:8080/todo/add',
            }).done(function (data) {
                let checkboxes = $('#showTasks');
                checkboxes.prop('checked', true);

                let idNumber = data[data.length - 1].id;
                let massage = data[data.length - 1].des;
                console.log(massage);
                let date = data[data.length - 1].created;
                let state = data[data.length - 1].done;
                let status;
                state ? status = '  в работе' : status = 'выполнено';
                let el = `<label><input type="checkbox" id="${idNumber}"><span>${status}</span></label>`;
                $("#table").find('tbody')
                    .append($('<tr>')
                        .append($('<td>')
                            .text(massage)
                        ).append($('<td>')
                            .text(date)
                        ).append($('<td>')
                            .append($(el)))
                    );
                if (!state) {
                    $(`#${idNumber}`).prop('checked', true);
                }
                $('input:checkbox').click(function () {
                    if (this.id === 'showTasks') {
                        return;
                    }
                    let id = $(this).get()[0].id;
                    let status = $(this).parent().find('span').html();
                    if ($(this).is(':checked')) {
                        status = " выполнено";
                        $(this).parent().css('color', 'green');
                        $(this).parent().find('span').empty().html(`${status}`);
                        updateStatus(id, false);
                    } else {
                        let status = "  в работе";
                        $(this).parent().css('color', 'black');
                        $(this).parent().find('span').empty().html(`${status}`);
                        updateStatus(id, true);
                    }
                });
                checkboxes.click(function () {
                    if (!checkboxes.is(':checked')) {
                        $('input:checkbox:checked').each(function () {
                            $(this).parent().parent().parent().hide();
                        });
                    }
                    if (checkboxes.is(':checked')) {
                        $('input:checkbox:checked').each(function () {
                            $(this).parent().parent().parent().show();
                        });
                    }
                });
            })
        }).fail(function () {
            alert("Error")
        });
    }
}

$(document).ready(function () {
    updatePage();
});

function updatePage() {
    $.ajax({
        type: 'GET',
        crossdomain: true,
        url: 'http://localhost:8080/todo/add',
    }).done(function (data) {
        let checkboxes = $('#showTasks');
        checkboxes.prop('checked', true);
        for (let i = 0; i < data.length; data[i++]) {
            let idNumber = data[i].id;
            let massage = data[i].des;
            console.log(massage);
            let date = data[i].created;
            let state = data[i].done;
            let status;
            state ? status = '  в работе' : status = 'выполнено';
            let el = `<label><input type="checkbox" id="${idNumber}"><span>${status}</span></label>`;
            $("#table").find('tbody')
                .append($('<tr>')
                    .append($('<td>')
                        .text(massage)
                    ).append($('<td>')
                        .text(date)
                    ).append($('<td>')
                        .append($(el)))
                );
            if (!state) {
                $(`#${idNumber}`).prop('checked', true);
            }
        }
        $('input:checkbox').click(function () {
            if (this.id === 'showTasks') {
                return;
            }
            let id = $(this).get()[0].id;
            let status = $(this).parent().find('span').html();
            if ($(this).is(':checked')) {
                status = " выполнено";
                $(this).parent().css('color', 'green');
                $(this).parent().find('span').empty().html(`${status}`);
                updateStatus(id, false);
            } else {
                let status = "  в работе";
                $(this).parent().css('color', 'black');
                $(this).parent().find('span').empty().html(`${status}`);
                updateStatus(id, true);
            }
        });
        checkboxes.click(function () {
            if (!checkboxes.is(':checked')) {
                $('input:checkbox:checked').each(function () {
                    $(this).parent().parent().parent().hide();
                });
            }
            if (checkboxes.is(':checked')) {
                $('input:checkbox:checked').each(function () {
                    $(this).parent().parent().parent().show();
                });
            }
        });
    }).fail(function () {
        alert("Error")
    });
}

function updateStatus(id, state) {
    $.ajax({
        type: 'POST',
        crossdomain: true,
        url: 'http://localhost:8080/todo/add',
        data: {
            id: id + "",
            des: "text",
            done: state + ""
        }
    }).done(function () {
        console.log("updateStatus OK");
    }).fail(function (err) {
        alert(err);
    })
}
