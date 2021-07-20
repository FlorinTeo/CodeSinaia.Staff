/**
 * Variables for the Guest main page parameters
 */
const username = (new URLSearchParams(window.location.search)).get("name");

/**
 * Variables for the Guest Main UI elements
 */
const txtTitleName = document.getElementById("titleName");
const btnGuestLogout = document.getElementById("guestLogout");

/**
 * Hook code listeners to actions and events in the Guest main flow
 */
document.addEventListener("DOMContentLoaded", onPageLoad);
btnGuestLogout.addEventListener("click", onClickLogout);

/**
 * Static resources needed in the Guest Main code
 */
const urlGuestAPI = window.location.origin + "/InstantReaction_Solved/IRGuest";
const urlGuestLogin = window.location.origin + "/InstantReaction_Solved/Guest/index.jsp";

/**
 * Callback for the initial loading of the Guest main page.
 */
function onPageLoad() {
    txtTitleName.innerText = username;
}

/**
 * Callback for clicking on the Guest Logout button
 */
function onClickLogout(e) {
    e.preventDefault();
    var request = new  XMLHttpRequest();
    request.open("GET", `${urlGuestAPI}?cmd=logout&name=${username}`, true);
    request.timeout = 2000;
    request.onload = onLogoutResponse;
    request.send();
}

/**
 * Callback for receiving the response from the REST API Guest Logout call
 */
function onLogoutResponse() {
    var jsonResponse = JSON.parse(this.response);
    // if successful or user not logged in go to the main page, otherwise alert error.
    if (jsonResponse.Success) {
        window.location.href = urlGuestLogin;
    } else {
        alert(jsonResponse.Message);
    }
}