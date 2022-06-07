<html>
<head>
<title>SSO Web</title>
</head>

<body>
<center>
<h2> SSO Web Application </h2> <br>
<%
	String username = (String)session.getAttribute("username");
	String password = (String)session.getAttribute("password");
	String domain = (String)session.getAttribute("domain");
	
	if (username != null && username != "" && password != null && password != "" && domain != null && domain != "")
	{
		response.setContentType("text/html");
		out.println("<table><form action=SSOChangePassword method=post>");
		out.println("<tr><td>Old password</td><td><input type=password style=width:180; name=current_password></td></tr>");
		out.println("<tr><td>New password</td><td><input type=password style=width:180; name=new_password></td></tr>");
		out.println("<tr><td>New password again</td><td><input type=password style=width:180; name=new_password_again></td></tr>");
		out.println("<tr><td align=center><a href=SSOLogOut>Log out</td><td align=right><input type=submit value=OK></form>&nbsp;<a href=SSOMainPage.jsp><button>Cancel</button></a></td></tr>");
		
		out.println("</table>");
		String error_code = (String)session.getAttribute("error_code");
		if (!error_code.isEmpty())
		out.println("<font color = red>" + error_code + "</font>"); 
		session.setAttribute("error_code", ""); // avoid repetetive errors
	}
	else
	{
		out.println("<h1>Session timeout. Please, <a href=/SSOLoginPage.jsp>log in</a>.</h1>");
	}
	
%>

</center>
</body></html>