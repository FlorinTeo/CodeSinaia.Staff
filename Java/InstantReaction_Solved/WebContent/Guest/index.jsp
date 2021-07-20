<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <!-- Text on the browser tab: "IR Guest" -->
        <title>IR Guest</title>
        <link rel="stylesheet" href="index.css?ver=1.0">
        <script defer src="index.js?ver=1.0"></script>
    </head>
    <body>
    
        <!-- Title of the page: "Instant Reaction GUEST" -->
        <table>
            <tr>
                <td>
                    <span>Instant Reaction</span>
                    <span class="title-highlight">Guest</span>
                </td>
            </tr>
        </table>
        <p>
        
        <!-- Controls for [Guest name][Login button] -->
        <table>
            <tr>
                <td>Name:</td>
                <td><input id="guestName" type="text"></td>
            </tr>
            <tr>
                <td></td>
                <td><input id="guestLogin" class="input-login" type="submit" value="Login"></td>
            </tr>
        </table>
        
        <!--  Output area -->
        <p>
        <div id="guestOutput"></div>
	</body>
</html>