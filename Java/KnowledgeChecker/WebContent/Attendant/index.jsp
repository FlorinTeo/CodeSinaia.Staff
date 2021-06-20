<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>KC Attendant</title>
        <link rel="stylesheet" href="index.css?ver=1.2">
        <script defer src="index.js?ver=1.1.2"></script>
    </head>
    <body>
        <table>
            <tr><td>Knowledge Checker&nbsp;<span class="title-highlight">Attendant</span></td></tr>
        </table>
        <p>
        <form id="attendantLoginForm">
        <table>
            <tr><td class="label">Name:</td><td><input id="attendantName" type="text" name="username" ></td></tr>
            <tr><td></td><td><input id="attendantLogin" type="submit" value="Login" ></td></tr>
        </table>
        </form>
        <p>
        <div id="attendantLoginOutput"></div>
        <div id="attendantLoginSuggestion"></div>
    </body>
</html>