let filterForm = $("#filterForm");
$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime",
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        });

    $("#clearFilter").click(function() {
        $("#filterForm input").val("");
        updateTable()
    });
    $("#applyFilter").click(function() {
        filter();
    });
    function filter() {
        $.ajax({
            type: "GET",
            url: context.ajaxUrl + "filter",
            data: filterForm.serialize()
        }).done(function (data) {
            context.datatableApi.clear().rows.add(data).draw();
            successNoty("Filter applied");
        });
    }
});

