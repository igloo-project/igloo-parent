<%@ include file="/WEB-INF/init.jsp" %>
<html>
<head>
</head>
<body>
	<h3><spring:message code="modifsuccess.titre" /></h3>
		
		<!--
			c:set défini une variable à partir d'un attribut 'var' qui 
			se trouve dans le périmètre 'scope' (request/session)
		-->
		<c:set var="successes" value="${successes}" scope="session"/>
		<c:if test="${not empty successes}">
			<div style="color:green;">
				<c:forEach var="item" items="${successes}">
					<div style="color:green;"><spring:message code="${item}" text="${item}"/></div>
				</c:forEach>
			</div>
			<c:remove scope="session" var="successes"/>
		</c:if>
		<br>
	
	<table>
		<tr>
			<td><spring:message code="modifsuccess.id" /></td>
			<td><c:out value="${entity.id}"/></td>
		</tr>
		<tr>
			<td><spring:message code="modifsuccess.descr" /></td>
			<td><c:out value="${entity.descr}"/></td>
		</tr>
		<tr>
			<td><spring:message code="modifsuccess.date" /></td>
			<td><owsi:dateformat date="${entity.date}" format="date.format.short.date"/></td>
		</tr>
	</table>
	
	<p><a href="<spring:url value="/index.html" />"><spring:message code="common.retour" /></a></p>
</body>
</html>