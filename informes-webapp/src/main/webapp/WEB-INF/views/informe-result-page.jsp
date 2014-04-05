<%@ include file="/WEB-INF/views/include.jsp" %>


<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><fmt:message key="informe.form.list.title" /></title>

	<%@ include file="/WEB-INF/views/include-css.jsp" %>


</head>
<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp" %>
		
	<div class="container">
	
		<div>
			<c:if test="${not empty mensajeError}">
				<div class="alert alert-error">
					<p>${mensajeError}</p>
				</div>
		    </c:if>		
		</div>
		

		<div>
			<c:if test="${not empty nuevoInforme}">
				<div id="informe-new-info" class="panel panel-primary">

					<!-- cabecera -->

					<div class="panel-heading">
						<h3 class="panel-title">
							<fmt:message key="informe.form.new.title" />
						</h3>
					</div>

					<!-- contenido -->

					<div>
						<!-- inicio contenido -->
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="informe.form.field.nombre" />
							</div>
							<div class="col-lg-9">${nuevoInforme.nombre}.</div>
						</div>
					</div>
					<!-- fin contenido -->

				</div>
				<!-- class="panel panel-primary" -->
			</c:if>
		</div>
	

		<div>
			<p>
				<a class="btn btn-primary" 
					href="${pageContext.request.contextPath}/informe/new.html">
					<fmt:message key="informe.form.new.title" />
				</a>
			</p>
		</div>

		<!-- Importante: pagination! -->
		<c:set var="baseUrl" value="/informe/pages" />
		
		<c:url var="firstUrl" value="${baseUrl}/1" />
		<c:url var="lastUrl" value="${baseUrl}/${totalPages}" />
		<c:url var="prevUrl" value="${baseUrl}/${currentIndex - 1}" />
		<c:url var="nextUrl" value="${baseUrl}/${currentIndex + 1}" />
		

		<div id="pagination-up">
			<%@ include file="/WEB-INF/views/include-pagination.jsp"%>
		</div>
		

		
		<div>
			<table class="table table-hover table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>#</th>
						<th>ID</th>
						<th><spring:message code="informe.form.field.nombre" /></th>
						<th><spring:message code="informe.form.field.numPestanyes" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${informes}" var="informe">
						<tr>
							<td><a
								href="${pageContext.request.contextPath}/informe/delete/${informe.id}.html"
								class="btn btn-primary btn-xs"> <fmt:message
										key="informe.form.btn.del" />
							</a></td>

							<td><a
								href="${pageContext.request.contextPath}/informe/edit/${informe.id}.html" >
									<c:out value="${informe.id}" /></a></td>
							<td><a
								href="${pageContext.request.contextPath}/informe/edit/${informe.id}.html" >
									<c:out value="${informe.nombre}" /></a></td>
							<td><a
								href="${pageContext.request.contextPath}/informe/edit/${informe.id}.html" >
									<c:out value="${informe.numPestanyes}" /></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>

		<div id="pagination-down">
			<%@ include file="/WEB-INF/views/include-pagination.jsp"%>
		</div>
		
		<%@ include file="/WEB-INF/views/include-goHome.jsp" %>
		
	</div>

	<%@ include file="/WEB-INF/views/include-jquery.jsp" %>
	
</body>
</html>