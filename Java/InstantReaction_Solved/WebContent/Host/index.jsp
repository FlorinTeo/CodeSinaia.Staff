<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <!-- Text on the browser tab: "IR Host" -->  
        <title>IR Host</title>
        <link rel="stylesheet" href="index.css?ver=1.0">
        <script defer src="index.js?ver=1.0"></script>
    </head>
    <body>

        <!-- Title of the page: "Instant Reaction HOST -->    
        <table>
            <tr>
                <td>
                    <span>Instant Reaction</span>
                    <span class="title-highlight">Host</span>
                </td>
            </tr>
        </table>
        <p>

        <!-- Controls for [Host name][Password] and [Login button] -->
        <table>
            <tr>
                <td>Name:</td>
                <td><input id="hostName" type="text"></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input id="hostPassword" type="password"></td>
            </tr>
            <tr>
                <td></td>
                <td><input id="hostLogin" class="input-login" type="submit" value="Login" ></td>
            </tr>
        </table>
        
        <!--  Output area -->
        <p>
        <div id="hostOutput"></div>
    </body>
</html>
