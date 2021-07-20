/**
 * Variables for the Guest main page parameters
 */
const username = (new URLSearchParams(window.location.search)).get("name");

/**
 * Variables for the Guest Main UI elements
 */
const txtTitleName = document.getElementById("titleName");
const btnGuestLogout = document.getElementById("guestLogout");
const txtQuestion = document.getElementById("txtQuestion");
const divAnswers = [
    document.getElementById("divAnswerLabel"),
    document.getElementById("divChoice"),
    document.getElementById("divRange"),
    document.getElementById("divFreeForm"),
];
const spnAnswer = document.getElementById("spnAnswer");
const btnChoiceA = document.getElementById("btnChoiceA");
const btnChoiceB = document.getElementById("btnChoiceB");
const rngRange = document.getElementById("rngRange");
const txtFreeFormAnswer = document.getElementById("txtFreeFormAnswer");
const btnFreeFormSubmit = document.getElementById("btnFreeFormSubmit");

/**
 * Hook code listeners to actions and events in the Guest main flow
 */
document.addEventListener("DOMContentLoaded", onPageLoad);
btnGuestLogout.addEventListener("click", onClickLogout);
btnChoiceA.addEventListener("click", onClickChoice);
btnChoiceB.addEventListener("click", onClickChoice);
rngRange.addEventListener("change", onChangeRange);
btnFreeFormSubmit.addEventListener("click", onClickSubmit);

/**
 * Static resources needed in the Guest Main code
 */
const urlGuestAPI = window.location.origin + "/InstantReaction_Solved/IRGuest";
const urlGuestLogin = window.location.origin + "/InstantReaction_Solved/Guest/index.jsp";

/**
 * Global variables
 */
 var currentQuestion;

/**
 * Callback for the initial loading of the Guest main page.
 */
function onPageLoad() {
    txtTitleName.innerText = username;
    setInterval(onStatusRequest, 4000);
    onStatusRequest();
}

/**
 * Timer callback sending an IRGuest?cmd=status request to the server.
 */
function onStatusRequest() {
    var request = new  XMLHttpRequest();
    request.open("GET", `${urlGuestAPI}?cmd=status&name=${username}`, true);
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
            setupQuestion(jsonStatus.Question);
            currentQuestion = jsonStatus.Question;
        } else {
            resetQuestion();
            currentQuestion = null;
        }
    } else {
        alert(jsonStatus.Message);
    }
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
 * Callback for receiving the response from the REST API Guest Logout call.
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

/**
 * Callback for clicking on the FreeForm Submit button.
 */
function onClickChoice(e) {
    e.preventDefault();
    // identify the choice and post the answer request
    postAnswerRequest(e.currentTarget.value);
}

/**
 * Callback for changing the range slider.
 */
function onChangeRange(e) {
    e.preventDefault();
    postAnswerRequest(rngRange.value);
}

/**
 * Callback for clicking on the FreeForm Submit button.
 */
function onClickSubmit(e) {
    e.preventDefault();
    postAnswerRequest(txtFreeFormAnswer.value);
}

function postAnswerRequest(answer) {
    // construct the JSON object for the answer to be sent to the server
    var answer = { "QuestionID" : currentQuestion.QuestionID, "AnswerText" : answer };
    
    // prepare the web POST request
    var request = new  XMLHttpRequest();
    request.open("POST", `${urlGuestAPI}?name=${username}&cmd=answer`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onAnswerResponse;
    request.send(JSON.stringify(answer));
}

/**
 * Callback for receiving the response from the REST API "cmd=answer" call
 */
function onAnswerResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        // trim the answer to max 10 chars to fit in the answer label.
        if (jsonStatus.Message.length > 10) {
            jsonStatus.Message = jsonStatus.Message.substring(0, 9) + '...';
        }
        spnAnswer.innerHTML = jsonStatus.Message;
    } else {
        alert(jsonStatus.Message);
    }
}

/**
 * Setup the answering controls based on the question being asked.
 * The question content get displayed then the correct answering controls
 * get displayed based on the type of question.
 */
function setupQuestion(question) {
    txtQuestion.value = question.QuestionText;
    divAnswers[0].style.display = 'block';
    if (question.QuestionType == 'Choice') {
        divAnswers[1].style.display = 'block';
    } else if (question.QuestionType == 'Range'){
        divAnswers[2].style.display = 'block';
    } else {
        divAnswers[3].style.display = 'block';
    }
}

/**
 * Reset the answering controls since the question has been cleared.
 * The question content gets filled in with default message,
 * all answering controls get hidden.
 */
function resetQuestion() {
    txtQuestion.value = 'No outstanding question!';
    spnAnswer.innerHTML = '';
    for (let divAnswer of divAnswers) {
        divAnswer.style.display = 'none';
    }
}