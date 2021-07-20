/**
 * Variables for the Control UI elements
 */
const txtControlOutput = document.getElementById("controlOutput");
const tblServerStatus = document.getElementById("statusTable").getElementsByTagName('tbody')[0];

/**
 * Hook code listeners to actions and events in the Control page
 */
document.addEventListener("DOMContentLoaded", onPageLoad);

/**
 * Static resources needed in the Host Login code
 */
const urlControlAPI = window.location.origin + "/InstantReaction_Solved/IRControl";

/**
 * Callback for the initial loading of the Control page.
 */
function onPageLoad() {
    setInterval(onStatusRequest, 4000);
    onStatusRequest();
}

/**
 * Timer callback sending an IRControl?cmd=status request to the server.
 */
function onStatusRequest() {
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
    // if successful print out the server status message and list, otherwise alert error.
    var jsonResponse = JSON.parse(this.response);
    if (jsonResponse.Success) {
        // print out the summary message
        txtControlOutput.innerHTML = jsonResponse.Message;
        // empty the table and repopulate it with the current server status
        tblServerStatus.innerHTML = '';
        for (i = 0; i < jsonResponse.Members.length; i++) {
            var newRow = tblServerStatus.insertRow();
            // chose the row styling based on the role of this member
            newRow.className = (jsonResponse.Members[i].Role == 'Host') ? 'status-host-tr': 'status-guest-tr';
            newRow.insertCell().innerHTML = jsonResponse.Members[i].Role;
            newRow.insertCell().innerHTML = jsonResponse.Members[i].IPAddress;
            newRow.insertCell().innerHTML = jsonResponse.Members[i].Name;
        }
    } else {
        alert(jsonResponse.Message);
    }
}