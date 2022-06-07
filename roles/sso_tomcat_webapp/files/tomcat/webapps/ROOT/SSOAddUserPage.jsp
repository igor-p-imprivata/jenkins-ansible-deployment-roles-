<html>
<head>
<title>SSO Web</title>
</head>

<body>

<center>
<h2> SSO Web Application </h2> <br>
<table><form action=SSOAddUser method=post>
<tr><td>Username</td> <td><input type=text style="width: 180;" name=username></td> </tr>
<tr><td>Domain</td> <td><input type=text style="width: 180;" name=domain></td> </tr>
<tr><td>Password</td> <td><input type=password style="width: 180;" name=password></td> </tr>
<tr><td>Password again</td> <td><input type=password style="width: 180;" name=password_again></td> </tr>
<tr><td colspan=2 align=right><input type=submit value=OK></form>&nbsp;<a href=SSOLoginPage.jsp>
<button>Cancel</button></td> </tr>
</table>
<%
	String error_code = (String)session.getAttribute("error_code");
	if (error_code != null && error_code != "")
		out.println("<font color = red>" + error_code + "</font>"); 
	session.setAttribute("error_code", ""); // avoid repetetive errors
%>

</center>
</body></html>