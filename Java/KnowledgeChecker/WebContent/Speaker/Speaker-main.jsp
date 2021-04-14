<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <!--  meta http-equiv="refresh" content="5" -->
        <title>KC Speaker</title>
        <link rel="stylesheet" href="Speaker-main.css?ver=1.7">
        <script defer src="Speaker-main.js?ver=1.5"></script>
    </head>
    <body>
        <table>
            <tr>
                <td>Knowledge Checker&nbsp;<span class="title-highlight">Speaker</span>&nbsp;<span id="speakerName" class="name-highlight">Your Name</span></td>
                <td><input id="speakerLogout" type="submit" value="logout" ></td>
            </tr>
        </table>
        <p>
        <div class="main-grid">
            <div class="grid-label">Question</div>
            <div class="grid-question"><textarea id = "txtQ" class="main-text-area"></textarea></div>
            <div class="grid-true-false">True / False<br><input id="ckbTF" class="main-checkbox" name="TrueFalse" type="checkbox"></div>
            <div class="grid-yes-no">Yes / No<br><input id="ckbYN" class="main-checkbox" name="YesNo" type="checkbox"></div>
            <div class="grid-number">Number 1 to 5<br><input id="ckbNum" class="main-checkbox" name="Numeric" type="checkbox"></div>
            <div class="grid-free">Free text<br><input id="ckbFree" class="main-checkbox" name="Free" type="checkbox"></div>
            <div class="grid-ask"><input id="btnAsk" class="main-button" type="submit" value="Ask"></div>
            <div class="grid-clear"><input id="btnClear" class="main-button" type="submit" value="Clear"></div>
            <div class="grid-status" >
                <p>
                <table id="statusTable" class="status-table" >
                    <tr id="statusHeader" class="status-tr">
                        <th class="status-th" style="width: 35%">Name</th>
                        <th class="status-th" style="width: 50%">Answer</th>
                        <th class="status-th" style="width: 15%">State</th>
                    </tr>
                    <tr class="status-tr"></tr>
                </table>
            </div>
        </div>
        <p>
    </body>
</html>