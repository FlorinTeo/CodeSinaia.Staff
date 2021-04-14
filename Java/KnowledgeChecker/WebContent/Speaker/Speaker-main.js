const speakerName = document.getElementById("speakerName");
const speakerLogout = document.getElementById("speakerLogout");
const speakerAPI = window.location.origin + "/KnowledgeChecker/Speaker";
const speakerLoginRef = window.location.origin + "/KnowledgeChecker/Speaker/index.jsp";
const txtQ = document.getElementById("txtQ");

const ckbYN = document.getElementById("ckbYN");
const ckbTF = document.getElementById("ckbTF");
const ckbNum = document.getElementById("ckbNum");
const ckbFree = document.getElementById("ckbFree");
const ckbChoices = [ckbYN, ckbTF, ckbNum, ckbFree];

const btnAsk = document.getElementById("btnAsk");
const btnClear = document.getElementById("btnClear");

const statusTable = document.getElementById("statusTable");
const statusHeader = document.getElementById("statusHeader");

document.addEventListener("DOMContentLoaded", onPageLoad);
speakerLogout.addEventListener("click", onLogoutClick);
ckbYN.addEventListener("click", onCkbClick);
ckbTF.addEventListener("click", onCkbClick);
ckbNum.addEventListener("click", onCkbClick);
ckbFree.addEventListener("click", onCkbClick);
btnClear.addEventListener("click", onBtnClearClick);
btnAsk.addEventListener("click", onBtnAskClick);

function onPageLoad() {
    //statusTable.style.opacity = 0;
    onStatusRequest();
    setInterval(onStatusRequest, 4000);
}

function onStatusRequest() {
    var request = new  XMLHttpRequest();
    request.open("GET", `${speakerAPI}?cmd=status`, true);
    request.timeout = 2000;
    request.onload = onStatusResponse;
    request.send();
}

function onStatusResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        speakerName.innerText = jsonStatus.Name;
        fillStatusTable(jsonStatus);
    }
}

function onLogoutClick(e) {
    e.preventDefault();
    var name = speakerName.value;
    var request = new  XMLHttpRequest();
    request.open("GET", `${speakerAPI}?cmd=logout`, true);
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

function onBtnClearClick(e) {
    e.preventDefault();
    
    var request = new  XMLHttpRequest();
    request.open("POST", `${speakerAPI}?cmd=clear`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onClearResponse;
    request.send();
}

function onClearResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        txtQ.value="";
        txtQ.disabled=false;
        for (i = 0; i < ckbChoices.length; i++) {
            ckbChoices[i].checked = false;
            ckbChoices[i].disabled = false;
        }
        //statusTable.style.opacity = 0;
    } else {
        alert(jsonStatus.Message);
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
    request.open("POST", `${speakerAPI}?cmd=ask`, true);
    request.timeout = 2000;
    request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    request.onload = onAskResponse;
    request.send(JSON.stringify(question));
}

function onAskResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        txtQ.disabled = true;
        for (i = 0; i < ckbChoices.length; i++) {
            ckbChoices[i].disabled = true;
        }
        //statusTable.style.opacity = 1;
        fillStatusTable(jsonStatus);
    } else {
        alert(jsonStatus.Message);
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