/**
 * Variables for the Host main page parameters
 */
const username = (new URLSearchParams(window.location.search)).get("name");

/**
 * Variables for the Host main UI elements
 */
const txtTitleName = document.getElementById("titleName");
const btnHostLogout = document.getElementById("hostLogout");

/**
 * Hook code listeners to actions and events in the Host main flow
 */
document.addEventListener("DOMContentLoaded", onPageLoad);
btnHostLogout.addEventListener("click", onClickLogout);

/**
 * Static resources needed in the Host main code
 */
const urlHostAPI = window.location.origin + "/InstantReaction/IRHost";
const urlHostLogin = window.location.origin + "/InstantReaction/Host/index.jsp";

/**
 * Callback for the initial loading of the Host main page.
 */
function onPageLoad() {
    txtTitleName.innerText = username;
}

/**
 * Callback for clicking on the Host Logout button
 */
function onClickLogout(e) {
    e.preventDefault();
    var request = new  XMLHttpRequest();
    request.open("GET", `${urlHostAPI}?cmd=logout&name=${username}`, true);
    request.timeout = 2000;
    request.onload = onLogoutResponse;
    request.send();
}

/**
 * Callback for receiving the response from the REST API Host Logout call
 */
function onLogoutResponse() {
    var jsonResponse = JSON.parse(this.response);
    // if successful or user not logged in go to the main page, otherwise alert error.
    if (jsonResponse.Success) {
        window.location.href = urlHostLogin;
    } else {
        alert(json.Message);
    }
}