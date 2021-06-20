const username = (new URLSearchParams(window.location.search)).get("name");

const speakerName = document.getElementById("speakerName");
const speakerLogout = document.getElementById("speakerLogout");
const speakerAPI = window.location.origin + "/KnowledgeChecker/Speaker";
const speakerLoginRef = window.location.origin + "/KnowledgeChecker/Speaker/index.jsp";
const txtQ = document.getElementById("txtQ");

const ckbChoice = document.getElementById("ckbChoice");
const ckbRange = document.getElementById("ckbRange");
const ckbFree = document.getElementById("ckbFree");
const ckbChoices = [ckbChoice, ckbRange, ckbFree];

const btnAsk = document.getElementById("btnAsk");
const btnClear = document.getElementById("btnClear");

const statusTable = document.getElementById("statusTable");
const statusHeader = document.getElementById("statusHeader");

document.addEventListener("DOMContentLoaded", onPageLoad);
speakerLogout.addEventListener("click", onLogoutClick);

ckbChoice.addEventListener("click", onCkbClick);
ckbRange.addEventListener("click", onCkbClick);
ckbFree.addEventListener("click", onCkbClick);

btnAsk.addEventListener("click", onBtnAskClick);
btnClear.addEventListener("click", onBtnClearClick);

var crtQuestion = null;

function onPageLoad() {
    //statusTable.style.opacity = 0;
    onStatusRequest();
    setInterval(onStatusRequest, 1000);
}

function onStatusRequest() {
    var request = new  XMLHttpRequest();
    speakerName.innerText = username;
    request.open("GET", `${speakerAPI}?name=${username}&cmd=status`, true);
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
        
        fillStatusTable(jsonStatus);
    }
}

function onLogoutClick(e) {
    e.preventDefault();
    var name = speakerName.value;
    var request = new  XMLHttpRequest();
    request.open("GET", `${speakerAPI}?name=${username}&cmd=logout`, true);
    request.timeout = 2000;
    request.onload = onLogoutResponse;
    request.send();
}

function onLogoutResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (!jsonStatus.Success) {
        alert(jsonStatus.Message);
    }
    window.location.href = speakerLoginRef;
}

function onCkbClick(e) {
    var ckb = e.target || e.srcElement;
    for (i = 0; i < ckbChoices.length; i++) {
        if (ckb != ckbChoices[i]) {
            ckbChoices[i].checked = false;
        }
    }
}

function onBtnAskClick(e) {
    e.preventDefault();
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
    
    var question = { "QuestionText" : txtQ.value, "QuestionType" : ckb.name };
    
    var request = new  XMLHttpRequest();
    request.open("POST", `${speakerAPI}?name=${username}&cmd=ask`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onAskResponse;
    request.send(JSON.stringify(question));
}

function onAskResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        crtQuestion = jsonStatus.Question;
        fillQuestion();
    } else {
        alert(jsonStatus.Message);
    }
}

function onBtnClearClick(e) {
    e.preventDefault();
    
    var request = new  XMLHttpRequest();
    request.open("POST", `${speakerAPI}?name=${username}&cmd=clear`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onClearResponse;
    request.send();
}

function onClearResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        resetQuestion();
    } else {
        alert(jsonStatus.Message);
    }
}

function fillQuestion() {
    txtQ.disabled = true;
    txtQ.value = crtQuestion.QuestionText;
    for (i = 0; i < ckbChoices.length; i++) {
        ckbChoices[i].checked = (crtQuestion.QuestionType == ckbChoices[i].name);
        ckbChoices[i].disabled = true;
    }
}

function resetQuestion() {
    txtQ.value="";
    txtQ.disabled=false;
    for (i = 0; i < ckbChoices.length; i++) {
        ckbChoices[i].checked = false;
        ckbChoices[i].disabled = false;
    }
}

function fillStatusTable(jsonStatus) {
    var statusTableHTML = `<table id="statusTable" class="status-table" >`;
    statusTableHTML += statusHeader.innerHTML;
    
    // add the newly fetched ones 
    for (i = 0; i < jsonStatus.Members.length; i++) {
        rowStyle = "status-tr"; 
        if (jsonStatus.Members[i].Role == "Speaker") {
            rowStyle = "status-gray-tr";
        }
        statusTableHTML += `<tr class="${rowStyle}">`;
        statusTableHTML += `<td style="width: 35%">${jsonStatus.Members[i].Name}</td>`;
        statusTableHTML += (jsonStatus.Members[i].hasOwnProperty("Answer"))
                            ? `<td style="width: 50%">${jsonStatus.Members[i].Answer.AnswerText}</td>`
                            : `<td style="width: 50%">-</td>`;
        statusTableHTML += `<td style="width: 15%">${jsonStatus.Members[i].State}</td>`;
        statusTableHTML += `</tr>`;
    }
    
    statusTable.innerHTML = statusTableHTML;
}