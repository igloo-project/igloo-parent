<%@ include file="/WEB-INF/init.jsp" %>
<html>
<head>
</head>
<body>
	<h2><spring:message code="accueil.titre" /></h2>
	<p>
		<spring:message code="accueil.link" /><br/> 
		<a href="<spring:url value="springmvc/mycontroller/getentity" />"><spring:message code="accueil.click" /><br></a>
	</p>
</body>
</html>