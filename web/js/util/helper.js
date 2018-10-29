/* Various helper functions */
define(function() {

    /*
     * Helper method, converting FormData JSON.
     *
     */
    var formDataToJson = function(formData) {
        // Converting to JSON
        let obj = {};
        for(const keyvalue of formData.entries()) {
            obj[keyvalue[0]] = keyvalue[1];
        }

        return JSON.stringify(obj);
    };


    // Hiding provided element, adding display none if no parameter provided
    var hideElement = function(el, value = "none") {
        el.style.display = "none";
    };

    // Showing provided form, adding display block if no parameter provided
    var showElement = function(el, value = "block") {
        el.style.display = value;
    };

    return {
        formDataToJson: function(data) {
            return formDataToJson(data);
        },

        hideElement: function(el, value = "none") {
            hideElement(el,value);
        },

        showElement: function(el, value = "block") {
            showElement(el,value);
        }
    };

});