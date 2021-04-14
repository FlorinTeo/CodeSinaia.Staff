<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>KC Control Panel</title>
        <link rel="stylesheet" href="index.css?ver=1.3">
        <script defer src="index.js?ver=1.1"></script>
    </head>
    <body>
        <table>
            <tr><td>Knowledge Checker&nbsp;<span class="title-highlight">Control Panel</span></td></tr>
        </table>
        <p>
        <a href="Speaker/index.jsp" target="_blank"><button>Speaker</button></a>
        <a href="Attendant/index.jsp" target="_blank"><button>Attendant</button></a>
        <p>
        <table id="statusTable" style="width: 80%" class="status-table">
            <tr id="statusHeader" class="status-tr">
                <th width="20%" class="status-th">IP</th>
                <th width="55%" class="status-th">Name</th>
                <th width="15%" class="status-th">Role</th>
                <th width="10%" class="status-th">State</th>
            </tr>
            <tr class="status-tr"></tr>
        </table>
    </body>
</html>