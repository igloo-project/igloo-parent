<%@ include file="/WEB-INF/init.jsp" %>

<html>
<head>
</head>
<body>
	<h2>CHOICE PAGE</h2>
	<p>If you are here, you are logged in.</p>
	<p><sec:authorize access="hasRole('ROLE_USER')">If you are a simple user you can access <a href="<c:url value="user.jsp" />">the user page</a></sec:authorize></p>
	<p><sec:authorize access="hasRole('ROLE_SUPERVISOR')">If you are a supervisor you can access <a href="<c:url value="admin.jsp" />">the admin page</a></sec:authorize></p>
	<p><a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a></p>
</body>
</html>