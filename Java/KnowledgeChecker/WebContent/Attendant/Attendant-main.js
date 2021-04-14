const attendantName = document.getElementById("attendantName");
const attendantLogout = document.getElementById("attendantLogout");

const attendantAPI = window.location.origin + "/KnowledgeChecker/Attendant";
const attendantLoginRef = window.location.origin + "/KnowledgeChecker/Attendant/index.jsp";

const txtQ = document.getElementById("txtQ");
const defTxtQ = txtQ.value;

const divAnswerLabel = document.getElementById("divAnswerLabel");
const spnAnswer = document.getElementById("spnAnswer");

const divTrueFalse = document.getElementById("divTrueFalse");
const btnTrue = document.getElementById("btnTrue");
const btnFalse = document.getElementById("btnFalse");

const divYesNo = document.getElementById("divYesNo");
const btnYes = document.getElementById("btnYes");
const btnNo = document.getElementById("btnNo");

const divNumeric = document.getElementById("divNumeric");
const rngNumeric = document.getElementById("rngNumeric");

const divFree = document.getElementById("divFree");
const txtFree = document.getElementById("txtFree");
const btnFree = document.getElementById("btnFree");

document.addEventListener("DOMContentLoaded", onPageLoad);
attendantLogout.addEventListener("click", onLogoutClick);
btnTrue.addEventListener("click", onChoiceClick);
btnFalse.addEventListener("click", onChoiceClick);
btnYes.addEventListener("click", onChoiceClick);
btnNo.addEventListener("click", onChoiceClick);
rngNumeric.addEventListener("change", onChoiceClick);
btnFree.addEventListener("click", onChoiceClick);

var crtQuestion = null;

function onPageLoad() {
    var request = new  XMLHttpRequest();
    onStatusRequest();
    setInterval(onStatusRequest, 4000);
}

function onStatusRequest() {
    var request = new  XMLHttpRequest();
    request.open("GET", `${attendantAPI}?cmd=status`, true);
    request.timeout = 2000;
    request.onload = onStatusResponse;
    request.send();
}

function onStatusResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        attendantName.innerText = jsonStatus.Name;
        updateQuestion(jsonStatus);
    }
}

function onLogoutClick(e) {
    e.preventDefault();
    var name = attendantName.value;
    var request = new  XMLHttpRequest();
    request.open("GET", `${attendantAPI}?cmd=logout`, true);
    request.timeout = 2000;
    request.onload = onLogoutResponse;
    request.send();
}

function onLogoutResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (!jsonStatus.Success) {
        alert(jsonStatus.Message);
    }
    
    window.location.href = attendantLoginRef;
}

function updateQuestion(jsonStatus) {
    if (jsonStatus.hasOwnProperty("Question")) {
        this.crtQuestion = jsonStatus.Question;
        txtQ.value = this.crtQuestion.QuestionText;
        divAnswerLabel.style.display="block";
        if (this.crtQuestion.QuestionType == "TrueFalse") {
            divTrueFalse.style.display="block";
        } else if (this.crtQuestion.QuestionType == "YesNo") {
            divYesNo.style.display="block";
        } else if (this.crtQuestion.QuestionType == "Numeric") {
            divNumeric.style.display="block";
        } else if (this.crtQuestion.QuestionType == "Free") {
            divFree.style.display="block";
        }
    } else {
        this.crtQuestion = null;
        txtQ.value = defTxtQ;
        divAnswerLabel.style.display = "none";
        divTrueFalse.style.display = "none";
        divYesNo.style.display = "none";
        divNumeric.style.display = "none";
        divFree.style.display = "none";
        rngNumeric.value = "3";
        spnAnswer.innerText = "";
    }
}

function onChoiceClick(e) {
    e.preventDefault();
    var ctlChoice = e.target || e.srcElement;
    if (ctlChoice != btnFree) {
        spnAnswer.innerText = ctlChoice.value;
        onPostAnswer(ctlChoice.value);
    } else {
        spnAnswer.innerText = "Sent!";
        onPostAnswer(txtFree.value);
    }
}

function onPostAnswer(answerText) {
    var question = { "QuestionID" : this.crtQuestion.QuestionID, "AnswerText" : answerText };
    
    var request = new  XMLHttpRequest();
    request.open("POST", `${attendantAPI}?cmd=answer`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onAnswerResponse;
    request.send(JSON.stringify(question));
}

function onAnswerResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
    } else {
        alert(jsonStatus.Message);
        updateQuestion(jsonStatus);
    }
}