<%@ include file="/WEB-INF/init.jsp" %>

<html>
<head>
</head>
<body>
	<h2>INDEX PAGE</h2>
	<p>You have to be logged in to go on <a href="<c:url value="secured/choice.jsp" />">this page</a></p>
	<p>You can test the secure pre-authorized method getDate on <a href="<c:url value="springmvc/preauthorize" />">this page</a></p>
	<p>You can test the post-filtered method getList on <a href="<c:url value="springmvc/postfilter" />">this page</a></p>
	<p><a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a></p>
</body>
</html>