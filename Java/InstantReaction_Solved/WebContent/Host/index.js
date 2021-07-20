/**
 * Variables for the Host Login UI elements
 */
const edtHostName = document.getElementById("hostName");
const edtHostPassword = document.getElementById("hostPassword");
const btnHostLogin = document.getElementById("hostLogin");
const txtHostOutput = document.getElementById("hostOutput");

/**
 * Hook code listeners to actions and events in the Host Login flow
 */
btnHostLogin.addEventListener("click", onClickLogin);

/**
 * Static resources needed in the Host Login code
 */
const urlHostAPI = window.location.origin + "/InstantReaction/IRHost";
const urlHostMain = window.location.origin + "/InstantReaction/Host/Host-main.jsp";

/**
 * Callback for clicking on the "Login" button
 */
function onClickLogin(e) {
    e.preventDefault();
    var username = edtHostName.value;
    var password = edtHostPassword.value;

    if(username == null || username == "") {
        alert("Error: Need a name!");
    } else {
        var request = new  XMLHttpRequest();
        request.open("GET", `${urlHostAPI}?cmd=login&name=${username}&password=${password}`, true);
        request.timeout = 2000;
        request.onload = onLoginResponse;
        request.send();
    }
}

/**
 * Callback for receiving the response from the REST API Host Login call
 */
function onLoginResponse() {
    var jsonResponse = JSON.parse(this.response);
    // if successful or guest already logged in redirect to the guest main page
    // otherwise display the response on the guest login page.
    if (jsonResponse.Success) {
        window.location.href = `${urlHostMain}?name=${edtHostName.value}`;
    } else {
        txtHostOutput.innerHTML = jsonResponse.Message;
    }
}