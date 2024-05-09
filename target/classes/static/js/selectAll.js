$(document).ready(function() {
    $('.select-group-checkbox').change(function() {
        var checkboxes = $(this).closest('.accordion-item').find('input[type="checkbox"]');
        checkboxes.prop('checked', $(this).prop('checked'));
    });
});