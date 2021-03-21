<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>KC Control Panel</title>
		<style>
			table, th, td {
			  text-align: left;
			  border: 1px solid black;
			  border-collapse: collapse;
			}
			table {
			  width: 50%
			}
			th {
			  background-color: #dddddd;
			}
		</style>
	</head>
	<body>
		<a href="index.jsp"><span style="font-weight:900;font-size:24px">Knowledge Checker</span></a>&nbsp
		Control Panel
		<p>
		<a href="Control?cmd=context"><button>Context</button></a>
		&nbsp&nbsp&nbsp&nbsp
		<a href="Speaker.jsp" target="_blank"><button>Speaker</button></a>
		<a href="Attendant.jsp" target="_blank"><button>Attendant</button></a>
		<p>
		${output}
	</body>
</html>