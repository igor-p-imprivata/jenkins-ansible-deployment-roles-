<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<html>
<head>

<title>SSO Web</title>
</head>

<body>
<center>
<h2> SSO Web Application </h2> <br>
<table> <form method=post action=SSOLogin>
<tr><td> Login </td>  <td> <input type=text style="width: 180;" name=username> </td></tr>
<tr><td> Password </td>  <td> <input type=password name=password style="width: 180;"> </td></tr>
<tr><td> Domain </td> <td> <select name=domain  style="width: 180;">


<% 
		String sDriverName = "org.sqlite.JDBC";
		String sTempDb = "mydatabase.sqlite";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + sTempDb;
		int count = 0;
		String sMakeSelect = "SELECT DISTINCT DOMAIN FROM Users";
		try 
		{
        Class.forName(sDriverName);
		Connection conn = DriverManager.getConnection(sDbUrl);
		try {
            Statement statement = conn.createStatement();
            try {
                ResultSet resultSet = statement.executeQuery(sMakeSelect);
                try {
                    while(resultSet.next())
                        {
							count++;
                            String sResult = resultSet.getString("DOMAIN");
                            out.println("<option value=" + sResult + ">" + sResult + "</option>");
                        }
                } finally {
                    try { resultSet.close(); } catch (Exception ignore) {}
                }
            } finally {
                try { statement.close(); } catch (Exception ignore) {}
            }
        } finally {
            try { conn.close(); } catch (Exception ignore) {}
        }
		}
		catch (Exception e)
		{
			
		}
		if (count == 0)
			out.println("<option disabled>EMPTY DATABASE!</option>");
%>
</select> </td> </tr>
<tr><td align=center><a href=SSOAddUserPage.jsp>Create user</a></td> <td align=right> <input type=submit value=OK> </td> </tr>
</form></table>


<%
	String error_code = (String)session.getAttribute("error_code");
	if (!(error_code == null) && !(error_code == ""))
		out.println("<font color = red>" + error_code + "</font>"); 
	session.setAttribute("error_code", ""); // avoid repetetive errors
%>
</center>
</body></html>