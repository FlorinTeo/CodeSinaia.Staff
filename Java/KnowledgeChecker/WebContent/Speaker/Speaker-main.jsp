<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>KC Speaker</title>
        <link rel="stylesheet" href="Speaker-main.css?ver=1.0.0">
        <script defer src="Speaker-main.js?ver=2.0.0"></script>
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
            <div class="grid-choice">Choice<br><input id="ckbChoice" class="main-checkbox" name="Choice" type="checkbox"></div>
            <div class="grid-range">Range 1 to 10<br><input id="ckbRange" class="main-checkbox" name="Range" type="checkbox"></div>
            <div class="grid-free">Free text<br><input id="ckbFree" class="main-checkbox" name="Free" type="checkbox"></div>

            <div class="grid-buttons">
                <input id="btnAsk" class="main-button" type="submit" value="Ask">
                <input id="btnClear" class="main-button" type="submit" value="Clear">
            </div>

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