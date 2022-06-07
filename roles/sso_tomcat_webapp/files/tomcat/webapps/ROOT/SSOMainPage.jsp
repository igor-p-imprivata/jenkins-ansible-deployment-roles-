<html>
<head>

<title>SSO Web</title>
</head>

<body>
<center>
<h2> SSO Web Application </h2> <br>
<%
	HttpSession ses	= request.getSession();
	String username = (String)ses.getAttribute("username");
	String password = (String)ses.getAttribute("password");
	String domain = (String)ses.getAttribute("domain");
	
	if (username != null && username != "" && password != null && password != "" && domain != null && domain != "")
	{
		out.println("Hello, " + username + "!<br><br><br>");
		
		String error_code = (String)session.getAttribute("error_code");
		if (!error_code.isEmpty())
			out.println("<font color = red>" + error_code + "</font><br>"); 
		session.setAttribute("error_code", ""); // avoid repetetive errors
		out.println("<a href=SSOChangePasswordPage.jsp><br>Change password</a>");
		out.println("<br><a href=SSOLogOut>Log out</a>");
	}
	else
	{
		out.println("<h1>Session timeout. Please, <a href=/SSOLoginPage.jsp>log in</a>.</h1>");
	}
%>
</center>
</body></html>