<%@ include file="/WEB-INF/init.jsp" %>
<html>
<head>
</head>
<body>
	<h3><spring:message code="modifentity.titre1" /></h3>
	<table>
		<tr>
			<td><spring:message code="modifentity.id" /></td>
			<!-- 
				c:out récupère les attributs qui ont été chargés dans le 
				modèle par le controleur 
			-->
			<td><c:out value="${entity.id}"/></td>
		</tr>
		<tr>
			<td><spring:message code="modifentity.descr" /></td>
			<td><c:out value="${entity.descr}"/></td>
		</tr>
		<tr>
			<td><spring:message code="modifentity.date" /></td>
			<td><owsi:dateformat date="${entity.date}" format="date.format.short.date"/></td>
		</tr>
	</table>

	<h3><br><spring:message code="modifentity.titre2" /></h3>
	<spring:url var="url" value="/springmvc/mycontroller/postentity" /> 
	<!-- 
		form:form crée un formulaire de type 'commandName' qui fera appel 
		lors du submit à l'URL 'action'. Il est possible de spécifier la 
		méthode (POST/GET) utilisée
	-->
	<form:form action="${url}" method="POST" commandName="myEntityForm">
		<spring:bind path="*">
			<c:if test="${not empty status.errors and status.errors.errorCount > 0}">
				<div style="color:red;">
					<spring:message code="validation.one-or-more-errors-found" />
				</div>
			</c:if>
		</spring:bind>
		<table>
			<tr>
				<spring:bind path="id">
					<td><spring:message code="modifentity.id" /></td>
					<!--
						form:input défini un champs de saisie pour la 
						variable 'path' du formulaire
					-->
					<td><form:input path="id" /></td>
					<!--
						form:errors affiche les erreurs associées à 
						la variable 'path' du formulaire
					-->
					<td><form:errors path="id" element="div" cssStyle="color:red;"/></td>
				</spring:bind>
			</tr>
			<tr>
				<spring:bind path="descr">
					<td><spring:message code="modifentity.descr" /></td>
					<td><form:input path="descr" /></td>
					<td><form:errors path="descr" element="div" cssStyle="color:red;"/></td>
				</spring:bind>
			</tr>
			<tr>
				<spring:bind path="date">
					<td><spring:message code="modifentity.date" /></td>
					<td><form:input path="date" /><spring:message code="modifentity.dateformat" /></td>
					<td><form:errors path="date" element="div" cssStyle="color:red;"/></td>
				</spring:bind>
			</tr>
		</table>
		
		<!--
			un input de type 'submit' permet de soumettre le formulaire 
			en appelant l'URL définie par form:form
		-->
		<p><input name="submit" type="submit" value="<spring:message code="modifentity.submit" />" /></p>
	</form:form>

	<h3><br><spring:message code="modifentity.titre3" /></h3>
	<!--
		On récupère la variable 'model_attribute_name' qui a été ajoutée 
		automatiquement au modèle par l'annotation @ModelAttribute sur la
		méthode getModelAttribute du controleur.
	-->
	<p><c:out value="${model_attribute_name}"/></p>

	<h3><br><spring:message code="modifentity.titre4" /></h3>
	<p><c:out value="${resource}"/></p>
</body>
</html>