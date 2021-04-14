const attendantLoginForm = document.getElementById("attendantLoginForm");
const attendantName = document.getElementById("attendantName");
const attendantLogin = document.getElementById("attendantLogin");
const attendantLoginOutput = document.getElementById("attendantLoginOutput");
const attendantLoginSuggestion= document.getElementById("attendantLoginSuggestion");

const urlAttendantAPI = window.location.origin + "/KnowledgeChecker/Attendant";
const urlAttendantMain = window.location.origin + "/KnowledgeChecker/Attendant/Attendant-main.jsp";
const urlSpeakerMain = window.location.origin + "/KnowledgeChecker/Speaker/Speaker-main.jsp";

attendantName.addEventListener("click", onNameClick)
attendantLogin.addEventListener("click", onLoginClick)

function onNameClick(e) {
    e.preventDefault();
    attendantLoginOutput.innerText = "";
    attendantLoginForm.reset();
}

function onLoginClick(e) {
    e.preventDefault();
    var name = attendantName.value;

    if(name == null || name == "") {
        attendantLoginOutput.innerText = "Error: Need a name!";
    } else {
        var request = new  XMLHttpRequest();
        request.open("GET", `${urlAttendantAPI}?cmd=login&name=${name}`, true);
        request.timeout = 2000;
        request.onload = onLoginResponse;
        request.send();
    }
}

function onLoginResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        window.location.href = urlAttendantMain; 
    } else if (jsonStatus.Role.length > 0) {
        attendantLoginOutput.innerHTML= jsonStatus.Message;
        urlSuggestion = (jsonStatus.Role == "Speaker") ? urlSpeakerMain : urlAttendantMain; 
        attendantLoginSuggestion.innerHTML= `Go to the <a href=${urlSuggestion}>${jsonStatus.Role}</a> main page.`;
    } else {
        attendantLoginOutput.innerHTML= jsonStatus.Message;
    }
}