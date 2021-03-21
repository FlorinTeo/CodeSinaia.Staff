<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>KC Attendant</title>
        <style>
            button { 
                height: 50px; 
                width: 60px; 
            }
        </style>
    </head>
    <body>
        Knowledge Checker
        <a href="Attendant.jsp"><span style="font-weight:900;font-size:24px">Attendant</span></a>&nbsp
        Control Panel
        <p>
        <form name="loginAttendant" action="Attendant" method="GET">
            <table>
               <tr>
                  <td><input type="submit" name="cmd" value="login" style="width:60px"/></td>
                  <td><input type="text" name="name"/></td>
                </tr>
            </table>
        </form>    
        <form name="logoutAttendant" action="Attendant" method="GET">
            <table>
                <tr>
                    <td><input type="submit" name="cmd" value="logout" style="width:60px"/></td>
                </tr>
            </table>
        </form>
        <p>
        <a href="Attendant?cmd=ping"><button>Ping!</button></a>
        <p>
        ${output}
    </body>
</html>