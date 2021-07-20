<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <!-- Text on the browser tab: "IR Host" -->
        <title>IR Host</title>
        <link rel="stylesheet" href="Host-main.css?ver=1.0">
        <script defer src="Host-main.js?ver=1.0"></script>
    </head>
    <body>
        <!-- Title of the page: "Instant Reaction Host username" .... [logout] -->
        <table>
            <tr>
                <td>
                    <span>Instant Reaction</span>
                    <span class="title-highlight">Host</span>
                    <span class="title-name" id="titleName"></span>
                </td>
                <td>
                    <input id="hostLogout" type="submit" value="logout" >
                </td>
            </tr>
        </table>
        <p>
        <!--  Main grid for asking a question and seeing the answers -->
        <div class="main-grid">
            <div class="grid-label">Question</div>
            
            <!--  Text area for filling in the question -->
            <div class="grid-question">
                <textarea id = "txtQ" class="main-text-area"></textarea>
            </div>
            
            <!--  Row of buttons for the question type: [Choice] [Range] [Free] -->
            <div class="grid-choice">
                Choice<br><input id="ckbChoice" class="main-checkbox" name="Choice" type="checkbox">
            </div>
            <div class="grid-range">
                Range 1 to 10<br><input id="ckbRange" class="main-checkbox" name="Range" type="checkbox">
            </div>
            <div class="grid-free">
                Free text<br><input id="ckbFree" class="main-checkbox" name="Free" type="checkbox">
            </div>
            
            <!--  Row of buttons for asking or clearing the question: [Ask] [Clear] -->
            <div class="grid-buttons">
                <input id="btnAsk" class="main-button" type="submit" value="Ask">
                <input id="btnClear" class="main-button" type="submit" value="Clear">
            </div>
            
            <!--  Status table for displaying the answers -->
            <div class="grid-status" >
                <p>
                <table id="statusTable" class="status-table" >
                    <thead>
	                    <tr id="statusHeader" class="status-tr">
	                        <th class="status-th" style="width:35%">Name</th>
	                        <th class="status-th" style="width:65%">Answer</th>
	                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>