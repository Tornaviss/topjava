// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
        }
    );
    $(".enabled").change(function() {
        changeEnabled($(this).parents().eq(1).attr("id"),  !!$(this).is(":checked"));
    })
});

function changeEnabled(id, enabled) {
    $.ajax({
            type: "PATCH",
            url: context.ajaxUrl + id + "&" + enabled
        }).done(function() {
            successNoty("Done! User with id " + id + " enabled value is " + enabled + " now");
            let patchedRow = $("tr[id=" + id + "]");
            enabled ? patchedRow.css("opacity", "1") : patchedRow.css("opacity", "0.5");
    })
}