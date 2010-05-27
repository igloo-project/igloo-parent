<%@ include file="/WEB-INF/init.jsp" %>
<html>
<head>
</head>
<body>
	<h3><spring:message code="aide.titre" /></h3>
	
	<spring:message code="aide.texte" />
	
	<img src="<owsi:url code="aide.image.file" arguments="logo-openwide.png" />" />
	
	<p><a href="<spring:url value="/index.html" />"><spring:message code="common.retour" /></a></p>
</body>
</html>