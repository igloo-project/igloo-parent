<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
</head>
<body>
	<h2>CHOICE PAGE</h2>
	<p>If you are here, you are logged in.</p>
	<p><sec:authorize access="hasRole('ROLE_USER')">If you are a simple user you can access <a href="user.jsp">the user page</a></sec:authorize></p>
	<p><sec:authorize access="hasRole('ROLE_SUPERVISOR')">If you are a supervisor you can access <a href="admin.jsp">the admin page</a></sec:authorize></p>
	<p><a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a></p>
</body>
</html>