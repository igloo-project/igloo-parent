<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
</head>
<body>
	<h2>INDEX PAGE</h2>
	<p>You have to be logged in to go on <a href="secured/choice.jsp">this page</a></p>
	<p>You can test the secure pre-authorized method getDate on <a href="preauthorize.html">this page</a></p>
	<p>You can test the post-filtered method getList on <a href="postfilter.html">this page</a></p>
	<p><a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a></p>
</body>
</html>