<%@ include file="/WEB-INF/init.jsp" %>
<html>
<head>
</head>
<body>
	<h3><spring:message code="general-error.titre" /></h3>
	<p><spring:message code="general-error.texte" /></p>
	<p><a href="<spring:url value="/index.html" />"><spring:message code="common.retour" /></a></p>
</body>
</html>