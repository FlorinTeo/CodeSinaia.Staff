/**
 * Variables for the Guest Login UI elements
 */
const edtGuestName = document.getElementById("guestName");
const btnGuestLogin = document.getElementById("guestLogin");
const txtGuestOutput = document.getElementById("guestOutput");

/**
 * Hook code listeners to actions and events in the Guest Login flow
 */
btnGuestLogin.addEventListener("click", onClickLogin);

/**
 * Static resources needed in the Guest Login code
 */
const urlGuestAPI = window.location.origin + "/InstantReaction_Solved/IRGuest";
const urlGuestMain = window.location.origin + "/InstantReaction_Solved/Guest/Guest-main.jsp";
 
/**
 * Callback for clicking on the "Login" button
 */
function onClickLogin(e) {
    e.preventDefault();
    var username = edtGuestName.value;

    if(username == null || username == "") {
        alert("Error: Need a name!");
    } else {
        var request = new  XMLHttpRequest();
        request.open("GET", `${urlGuestAPI}?cmd=login&name=${username}`, true);
        request.timeout = 2000;
        request.onload = onLoginResponse;
        request.send();
    }
}

/**
 * Callback for receiving the response from the REST API Guest Login call
 */
function onLoginResponse() {
    var jsonResponse = JSON.parse(this.response);
    // if successful or guest already logged in redirect to the host main page
    // otherwise display the response on the host login page.
    if (jsonResponse.Success) {
        window.location.href = `${urlGuestMain}?name=${edtGuestName.value}`;
    } else {
        txtGuestOutput.innerHTML = jsonResponse.Message;
    }
}