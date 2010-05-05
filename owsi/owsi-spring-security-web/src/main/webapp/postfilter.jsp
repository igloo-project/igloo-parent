<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
</head>
<body>
	<h1>POST-FILTERED METHOD DATE PAGE</h1>
	<p>Users will see only "User", while Supervisor will see "User" and "Supervior"</p>
	<p>
		<c:forEach items="${list}" var="value">
	    	<c:out value="${value}"/><br>
	    </c:forEach>
	</p>
	<p><a href="index.jsp">Index Page</a></p>
</body>
</html>