const username = (new URLSearchParams(window.location.search)).get("name");

const attendantName = document.getElementById("attendantName");
const attendantLogout = document.getElementById("attendantLogout");

const attendantAPI = window.location.origin + "/KnowledgeChecker/Attendant";
const attendantLoginRef = window.location.origin + "/KnowledgeChecker/Attendant/index.jsp";

const txtQ = document.getElementById("txtQ");
const defTxtQ = txtQ.value;

const divAnswerLabel = document.getElementById("divAnswerLabel");
const spnAnswer = document.getElementById("spnAnswer");

const divButtons = document.getElementById("divButtons");

const divRange = document.getElementById("divRange");
const rngRange = document.getElementById("rngRange");

const divFree = document.getElementById("divFree");
const txtFree = document.getElementById("txtFree");
const btnFree = document.getElementById("btnFree");

document.addEventListener("DOMContentLoaded", onPageLoad);
attendantLogout.addEventListener("click", onLogoutClick);
rngRange.addEventListener("change", onChoiceClick);
btnFree.addEventListener("click", onChoiceClick);

var crtQuestion = null;

function onPageLoad() {
    var request = new  XMLHttpRequest();
    attendantName.innerText = username;
    onStatusRequest();
    setInterval(onStatusRequest, 2000);
}

function onStatusRequest() {
    var request = new  XMLHttpRequest();
    request.open("GET", `${attendantAPI}?name=${username}&cmd=status`, true);
    request.timeout = 2000;
    request.onload = onStatusResponse;
    request.send();
}

function onStatusResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        
        // fetch the current question, if any
        var question = jsonStatus.hasOwnProperty("Question") ? jsonStatus.Question : null;

        // check if we need to either fill in or reset the current question
        if ( question != null && (crtQuestion == null || crtQuestion.QuestionID != question.QuestionID)) {
            // Question is new or updated
            crtQuestion = question;
            fillQuestion();
        } else if (question == null && crtQuestion != null) {
            // Question has been removed 
            resetQuestion();
            crtQuestion = null;
        }
    }
}

function onLogoutClick(e) {
    e.preventDefault();
    var name = attendantName.value;
    var request = new  XMLHttpRequest();
    request.open("GET", `${attendantAPI}?name=${username}&cmd=logout`, true);
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

function onChoiceClick(e) {
    e.preventDefault();
    var ctlChoice = e.target || e.srcElement;
    
    if (ctlChoice == rngRange) {
        // answer with range value
        spnAnswer.innerText = ctlChoice.value;
        onPostAnswer(ctlChoice.value);
    } else if (ctlChoice == btnFree) {
        // answer with free-form text
        spnAnswer.innerText = "Sent!";
        onPostAnswer(txtFree.value);
    } else {
        // answer with specific choice
        spnAnswer.innerText = ctlChoice.innerText;
        onPostAnswer(ctlChoice.innerText);
    }
}

function onPostAnswer(answerText) {
    var question = { "QuestionID" : crtQuestion.QuestionID, "AnswerText" : answerText };
    
    var request = new  XMLHttpRequest();
    request.open("POST", `${attendantAPI}?name=${username}&cmd=answer`, true);
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

function fillQuestion() {
    txtQ.value = crtQuestion.QuestionText.replace(/\|/g,"");
    divAnswerLabel.style.display="block";
    if (crtQuestion.QuestionType == "Choice") {
        formatButtons();
    } else if (crtQuestion.QuestionType == "Range") {
        formatRange();
    } else if (crtQuestion.QuestionType == "Free") {
        divFree.style.display="block";
    }
}
    
function resetQuestion() {
    txtQ.value = defTxtQ;
    txtFree.value = "";
    divAnswerLabel.style.display = "none";
    divButtons.style.display = "none";
    divRange.style.display = "none";
    divFree.style.display = "none";
    rngRange.value = "5";
    spnAnswer.innerText = "";
}

function formatButtons() {
    var labels = (crtQuestion.QuestionArguments.length > 0)
                ? crtQuestion.QuestionArguments : ["Yes", "No"];
                
    // remove existent buttons, if any
    divButtons.querySelectorAll('*').forEach(n => n.remove());
    
    // create new buttons for each of the labels
    for (i = 0; i < labels.length; i++) {
        var btn = document.createElement('button');
        btn.id = `btn${i}`;
        btn.className = "main-button";
        btn.innerText = labels[i];
        
        if (i > 0) {
            divButtons.innerHTML += "&nbsp;&nbsp;";
        }
        
        divButtons.appendChild(btn);
    }
    
    // add listeners for all created buttons
    divButtons.querySelectorAll('*').forEach(n => n.addEventListener("click", onChoiceClick));
    
    divButtons.style.display="block";
}

function formatRange() {
    // check if question has arguments to label the range
    if (crtQuestion.QuestionArguments.length >= 2) {
        // There are custom range labels
        divRange.children[0].innerHTML = crtQuestion.QuestionArguments[0];
        divRange.children[2].innerHTML = crtQuestion.QuestionArguments[1];
    } else {
        // Default range labels, taken from the input range element
        divRange.children[0].innerHTML = divRange.children[1].min;
        divRange.children[2].innerHTML = divRange.children[1].max;
    }
    
    divRange.style.display="block";
}