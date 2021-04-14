const statusTable = document.getElementById("statusTable");
const statusHeader = document.getElementById("statusHeader");
const serverAPI = window.location.origin + "/KnowledgeChecker/Server";

document.addEventListener("DOMContentLoaded", onPageLoad);

function onPageLoad() {
    setInterval(onStatusRequest, 4000);
    onStatusRequest();
}

function onStatusRequest() {
    var request = new  XMLHttpRequest();
    request.open("GET", serverAPI + "?cmd=status", true);
    request.timeout = 2000;
    request.onload = onStatusResponse;
    request.send();
}

function onStatusResponse() {
    var jsonServer = JSON.parse(this.response);
    var statusTableHTML = `<table id="statusTable" style="width: 80%" class="status-table">`;
    statusTableHTML += statusHeader.innerHTML;
    
    // add the newly fetched ones 
    for (i = 0; i < jsonServer.Members.length; i++) {
        statusTableHTML += `<tr class="status-tr">`;
        statusTableHTML += `<td>${jsonServer.Members[i].IPAddress}</td>`;
        statusTableHTML += `<td>${jsonServer.Members[i].Name}</td>`;
        statusTableHTML += `<td>${jsonServer.Members[i].Role}</td>`;
        statusTableHTML += `<td>${jsonServer.Members[i].State}</td>`;
        statusTableHTML += `</tr>`;
    }
    
    statusTable.innerHTML = statusTableHTML;
}
