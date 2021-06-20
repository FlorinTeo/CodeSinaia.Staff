<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>KC Speaker</title>
        <link rel="stylesheet" href="index.css?ver=1.2">
        <script defer src="index.js?ver=1.1.1"></script>
    </head>
    <body>
        <table>
            <tr><td>Knowledge Checker&nbsp;<span class="title-highlight">Speaker</span></td></tr>
        </table>
        <p>
        <form id="speakerLoginForm">
        <table>
            <tr><td class="label">Name:</td><td><input id="speakerName" type="text" name="username" ></td></tr>
            <tr><td class="label">Password:</td><td><input id="speakerPassword" type="password" name="password" ></td></tr>
            <tr><td></td><td><input id="speakerLogin" type="submit" value="Login" ></td></tr>
        </table>
        </form>
        <p>
        <div id="speakerLoginOutput"></div>
        <div id="speakerLoginSuggestion"></div>
    </body>
</html>