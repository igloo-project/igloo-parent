<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/init.jsp" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- [HEAD] -->
	<decorator:head />
	<!-- [HEAD] -->
</head>
<body>
	<h4><spring:message code="main.langue" /></h4>
	<p>
		<spring:url var="url_en" value="/springmvc/localecontroller">
			<spring:param name="locale" value="en" />
		</spring:url>
		<a href="${url_en}"><spring:message code="main.changeLocaleEn" /></a>
		
		<spring:url var="url_fr" value="/springmvc/localecontroller">
			<spring:param name="locale" value="fr" />
		</spring:url>
		<a href="${url_fr}"><spring:message code="main.changeLocaleFr" /></a><br>
	</p>
	<hr>
	<p>
		<!-- [CONTENU DE LA PAGE] -->
		<decorator:body />
		<!-- [CONTENU DE LA PAGE] -->
	</p>
</body>
</html>