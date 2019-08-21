const mealAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10) + " " + date.substring(11, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function(row, date, dateIndex) {
                $(row).attr("data-mealExcess", date.excess)
            }
        }),
        updateTable: function() {
            $.get(mealAjaxUrl, updateFilteredTable);
        }
    });
    enableDatetimepicker();
});

function enableDatetimepicker() {
    $("#dateTime").datetimepicker({
        format: "Y-m-d H:i"
    });
    const startDate = $("#startDate");
    const endDate = $("#endDate");
    startDate.datetimepicker({
        format:'Y-m-d',
        onShow:function(ct){
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        },
        timepicker:false
    });
    endDate.datetimepicker({
        format:'Y-m-d',
        onShow:function(ct){
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        },
        timepicker:false
    });
    const startTime = $("#startTime");
    const endTime = $("#endTime");
    startTime.datetimepicker({
        format: "H:i",
        onShow: function(ct) {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        },
        datepicker: false
    });
    endTime.datetimepicker({
        format: "H:i",
        onShow: function(ct) {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        },
        datepicker: false
    })
}