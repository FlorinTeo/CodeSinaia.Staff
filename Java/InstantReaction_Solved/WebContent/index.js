/**
 * Variables for the Control UI elements
 */
const txtControlOutput = document.getElementById("controlOutput");

/**
 * Hook code listeners to actions and events in the Control page
 */
document.addEventListener("DOMContentLoaded", onPageLoad);

/**
 * Static resources needed in the Host Login code
 */
const urlControlAPI = window.location.origin + "/InstantReaction/IRControl";

/**
 * Callback for the initial loading of the Control page.
 */
function onPageLoad() {
    var request = new  XMLHttpRequest();
    request.open("GET", `${urlControlAPI}?cmd=status`, true);
    request.timeout = 2000;
    request.onload = onStatusResponse;
    request.send();
}

/**
 * Callback for receiving the response from the REST API Control status call
 */
function onStatusResponse() {
    // if successful or user not logged in go to the main page, otherwise alert error.
    if (this.response.includes("IRControl_TODO")) {
        txtControlOutput.innerHTML = this.response;
    } else {
        alert(this.response);
    }
}