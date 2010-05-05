<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
</head>
<body>
	<h1>ADMIN PAGE</h1>
	<a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
</body>
</html>