/**
 * Variables for the Host main page parameters
 */
const username = (new URLSearchParams(window.location.search)).get("name");

/**
 * Variables for the Host main UI elements
 */
const txtTitleName = document.getElementById("titleName");
const btnHostLogout = document.getElementById("hostLogout");
const txtQ = document.getElementById("txtQ");
const ckbChoice = document.getElementById("ckbChoice");
const ckbRange = document.getElementById("ckbRange");
const ckbFree = document.getElementById("ckbFree");
const ckbChoices = [ckbChoice, ckbRange, ckbFree];
const btnAsk = document.getElementById("btnAsk");
const btnClear = document.getElementById("btnClear");
const tblServerStatus = document.getElementById("statusTable").getElementsByTagName('tbody')[0];

/**
 * Hook code listeners to actions and events in the Host main flow
 */
document.addEventListener("DOMContentLoaded", onPageLoad);
btnHostLogout.addEventListener("click", onClickLogout);
ckbChoice.addEventListener("click", onCkbClick);
ckbRange.addEventListener("click", onCkbClick);
ckbFree.addEventListener("click", onCkbClick);
btnAsk.addEventListener("click", onBtnAskClick);
btnClear.addEventListener("click", onBtnClearClick);

/**
 * Static resources needed in the Host main code
 */
const urlHostAPI = window.location.origin + "/InstantReaction_Solved/IRHost";
const urlHostLogin = window.location.origin + "/InstantReaction_Solved/Host/index.jsp";

/**
 * Global variables
 */
 var question = null;

/**
 * Callback for the initial loading of the Host main page.
 */
function onPageLoad() {
    txtTitleName.innerText = username;
    var request = new  XMLHttpRequest();
    request.open("GET", `${urlHostAPI}?cmd=status&name=${username}`, true);
    request.timeout = 2000;
    request.onload = onStatusResponse;
    request.send();
}

/**
 * Callback for receiving the response from the REST API "cmd=status" call
 */
function onStatusResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        if (jsonStatus.hasOwnProperty("Question")) {
            question = jsonStatus.Question;
            lockQuestion();
        } else if (question != null){
            unlockQuestion();
            question = null;
        }
        
        // fill in server status table by emptying it and repopulating with current state
        tblServerStatus.innerHTML = '';
        for (i = 0; i < jsonStatus.Members.length; i++) {
            // fill in only the guest information
            var newRow = tblServerStatus.insertRow();
            newRow.className = (jsonStatus.Members[i].Role == 'Host') ? 'status-host-tr' : 'status-guest-tr';
            // fill in the guest name cell
            newRow.insertCell().style= 'width:35%';
            newRow.cells[0].innerHTML = jsonStatus.Members[i].Name;
            // fill in the guest answer, if any
            newRow.insertCell().style = 'width:65%';
            newRow.cells[1].innerHTML = (jsonStatus.Members[i].Role == 'Host') ? '-' : 'TODO';
        }
    } else {
        alert(jsonStatus.Message);
    }
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

/**
 * Callback for clicking on any of the [Choice][Range][Free] checkboxes
 */
function onCkbClick(e) {
    var ckb = e.target || e.srcElement;
    for (i = 0; i < ckbChoices.length; i++) {
        if (ckb != ckbChoices[i]) {
            ckbChoices[i].checked = false;
        }
    }
}

/**
 * Callback for clicking on the [Ask] button
 */
function onBtnAskClick(e) {
    e.preventDefault();
    
    // find out which of the [Choice][Range][Free] checkboxes are checked
    var ckb = null;
    for (i = 0; i < ckbChoices.length; i++) {
        if (ckbChoices[i].checked) {
            ckb = ckbChoices[i];
            break;
        }
    }
    
    if (txtQ.value.length < 1 || ckb == null) {
        alert("Question definition is incomplete!");
        return;
    }
    
    // construct the JSON object to be sent to the server
    var question = { "QuestionType" : ckb.name, "QuestionText" : txtQ.value };
    
    // prepare the web POST request
    var request = new  XMLHttpRequest();
    request.open("POST", `${urlHostAPI}?name=${username}&cmd=ask`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onStatusResponse;
    request.send(JSON.stringify(question));
}

/**
 * Locks the text and checkbox fields giving the question text and type
 */
function lockQuestion() {
    txtQ.disabled = true;
    txtQ.value = question.QuestionText;
    for (i = 0; i < ckbChoices.length; i++) {
        ckbChoices[i].checked = (question.QuestionType == ckbChoices[i].name);
        ckbChoices[i].disabled = true;
    }
}

/**
 * Callback for clicking on the [Clear] button
 */
function onBtnClearClick(e) {
    e.preventDefault();
    
    var request = new  XMLHttpRequest();
    request.open("POST", `${urlHostAPI}?name=${username}&cmd=clear`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onStatusResponse;
    request.send();
}

/**
 * Resets and unlocks the text and checkboxes fields giving the question text and type
 */
function unlockQuestion() {
    txtQ.value="";
    txtQ.disabled=false;
    for (i = 0; i < ckbChoices.length; i++) {
        ckbChoices[i].checked = false;
        ckbChoices[i].disabled = false;
    }
}