<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <!-- Text on the browser tab: "IR Guest" -->
        <title>IR Guest</title>
        <link rel="stylesheet" href="Guest-main.css?ver=1.0">
        <script defer src="Guest-main.js?ver=1.0"></script>
    </head>
    <body>
        <!-- Title of the page: "Instant Reaction Guest username" .... [logout]" -->
        <table>
            <tr>
                <td>
                    <span>Instant Reaction</span>
                    <span class="title-highlight">Guest</span>
                    <span class="title-name" id="titleName"></span>
                </td>
                <td>
                    <input id="guestLogout" type="submit" value="logout" >
                </td>
            </tr>
        </table>
        <p>
        <!--  Main grid for answering the question depending on its type [Choice][Range][FreeForm]-->
        <div class="main-grid">
            <!--  Controls for displaying the question -->
            <!-- Question is rendered as a "Question" label followed by a read only text area for the content-->
            <div class="grid-question-label">Question</div>
            <div class="grid-question">
               <textarea readonly id="txtQuestion">No outstanding question!</textarea>
            </div>
            
            <!--  Controls for displaying the chosen answer -->
            <div id="divAnswerLabel" class="grid-answer-label">
                Answer: <span id="spnAnswer" class="your-answer"></span>
            </div>
            
            <!--  Controls for displaying a choice answer -->
            <div id="divChoice" class="grid-choice">
                <input id="btnChoiceA" type="submit" value="Choice A">&nbsp;&nbsp;
                <input id="btnChoiceB" type="submit" value="Choice B">
            </div>
            
            <!--  Controls for displaying a range answer -->
            <div id="divRange" class="grid-range">
                <span id="rngFrom">1</span>&nbsp;
                <input id="rngRange" class="main-range" type="range" min="1" max="10">&nbsp;
                <span id="rngTo">10</span>
            </div>
            
            <!--  Controls for displaying a free form answer -->
            <div id="divFreeForm" class="grid-freeform">
                <textarea id="txtFreeFormAnswer"></textarea>
                <br>
                <input id="btnFreeFormSubmit" type="submit" value="Submit">
            </div>
        </div>
    </body>
</html>