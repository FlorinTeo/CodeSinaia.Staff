const speakerLoginForm = document.getElementById("speakerLoginForm");
const speakerName = document.getElementById("speakerName");
const speakerPassword = document.getElementById("speakerPassword");
const speakerLogin = document.getElementById("speakerLogin");
const speakerLoginOutput = document.getElementById("speakerLoginOutput");
const speakerLoginSuggestion = document.getElementById("speakerLoginSuggestion");

const urlSpeakerAPI = window.location.origin + "/KnowledgeChecker/Speaker";
const urlSpeakerMain = window.location.origin + "/KnowledgeChecker/Speaker/Speaker-main.jsp";
const urlAttendantMain = window.location.origin + "/KnowledgeChecker/Attendant/Attendant-main.jsp";

speakerName.addEventListener("click", onNameClick)
speakerLogin.addEventListener("click", onLoginClick)

function onNameClick(e) {
    e.preventDefault();
    speakerLoginOutput.innerText = "";
    speakerLoginForm.reset();
}

function onLoginClick(e) {
    e.preventDefault();
    var name = speakerName.value;
    var password = speakerPassword.value;

    if(name == null || name == "") {
        speakerLoginOutput.innerText = "Error: Need a name!";
    } else {
        var request = new  XMLHttpRequest();
        request.open("GET", `${urlSpeakerAPI}?cmd=login&name=${name}&password=${password}`, true);
        request.timeout = 2000;
        request.onload = onLoginResponse;
        request.send();
    }
}

function onLoginResponse() {
    var jsonStatus = JSON.parse(this.response);
    if (jsonStatus.Success) {
        window.location.href = (jsonStatus.Role == "Speaker") 
            ? `${urlSpeakerMain}?name=${jsonStatus.Name}`
            : `${urlAttendantMain}?name=${jsonStatus.Name}`;
    } else if (jsonStatus.Role.length > 0) {
        speakerLoginOutput.innerHTML= jsonStatus.Message;
        urlSuggestion = (jsonStatus.Role == "Speaker") ? urlSpeakerMain : urlAttendantMain; 
        speakerLoginSuggestion.innerHTML= `Go to the <a href=${urlSuggestion}?name=${jsonStatus.Name}>${jsonStatus.Role}</a> main page.`;
    } else {
        speakerLoginOutput.innerHTML= jsonStatus.Message;
    }
}
