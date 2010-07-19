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
	<p>
		<spring:message code="accueil.general-error" /><br/> 
		<a href="<spring:url value="springmvc/exceptioncontroller/general-error" />"><spring:message code="accueil.click" /><br></a>
	</p>
	<p>
		<spring:message code="accueil.state-error" /><br/> 
		<a href="<spring:url value="springmvc/exceptioncontroller/state-error" />"><spring:message code="accueil.click" /><br></a>
	</p>
	
	<p>
		<spring:message code="accueil.static-resource" /><br/> 
		<a href="<spring:url value="springmvc/aide" />"><spring:message code="accueil.click" /><br></a>
	</p>
</body>
</html>