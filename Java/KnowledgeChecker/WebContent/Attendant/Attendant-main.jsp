<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>KC Attendant</title>
        <link rel="stylesheet" href="Attendant-main.css?ver=1.1.0">
        <script defer src="Attendant-main.js?ver=4.0.0"></script>
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
            <div id="divButtons" class="grid-buttons">
                <!--  input id="btn1" class="main-button" type="submit" value="Button 1">&nbsp;&nbsp; -->
            </div>
            <div id="divRange" class="grid-range">
                <span id="rngFrom">1</span>&nbsp;
                <input id="rngRange" class="main-range" type="range" min="1" max="10">&nbsp;
                <span id="rngTo">10</span>
            </div>
            <div id="divFree" class="grid-free">
                <textarea id="txtFree" class="main-free-area"></textarea>
                <p>
                <input id="btnFree" class="main-button" type="submit" value="Submit">
            </div>
        </div>
    </body>
</html>