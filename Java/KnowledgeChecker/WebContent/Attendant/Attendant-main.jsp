<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <!--  meta http-equiv="refresh" content="5" -->
        <title>KC Attendant</title>
        <link rel="stylesheet" href="Attendant-main.css?ver=1.2">
        <script defer src="Attendant-main.js?ver=1.5"></script>
    </head>
    <body>
        <table>
            <tr>
                <td>Knowledge Checker&nbsp;<span class="title-highlight">Attendant</span>&nbsp;<span id="attendantName" class="name-highlight">Attd.Name</span></td>
                <td><input id="attendantLogout" type="submit" value="logout" ></td>
            </tr>
        </table>
        <p>
        <div class="main-grid">
            <div class="grid-question-label">Question</div>
            <div class="grid-question">
                <textarea readonly id="txtQ" class="main-text-area">No question was broadcasted yet!</textarea>
            </div>
            <div id="divAnswerLabel" class="grid-answer-label">
                Answer: <span id="spnAnswer" class="your-answer"></span>
            </div>
            <div id="divTrueFalse" class="grid-true-false">
                <input id="btnTrue" class="main-button" type="submit" value="True">
                &nbsp;&nbsp;
                <input id="btnFalse" class="main-button" type="submit" value="False">
            </div>
            <div id="divYesNo" class="grid-yes-no">
                <input id="btnYes" class="main-button" type="submit" value="Yes">
                &nbsp;&nbsp;
                <input id="btnNo" class="main-button" type="submit" value="No">
            </div>
            <div id="divNumeric" class="grid-numeric">
                1<input id="rngNumeric" class="main-range" type="range" min="1" max="5">5
            </div>
            <div id="divFree" class="grid-free">
                <textarea id="txtFree" class="main-free-area"></textarea>
                <p>
                <input id="btnFree" class="main-button" type="submit" value="Submit">
            </div>
        </div>
    </body>
</html>