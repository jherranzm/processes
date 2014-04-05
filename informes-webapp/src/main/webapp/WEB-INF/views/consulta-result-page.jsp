<%@ include file="/WEB-INF/views/include.jsp"%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="consulta.form.list.title"
		text="missing" /></title>

<%@ include file="/WEB-INF/views/include-css.jsp"%>


</head>
<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp"%>

	<div class="container">

		<div>
			<c:if test="${not empty mensajeError}">
				<div class="alert alert-error">
					<p>${mensajeError}</p>
				</div>
			</c:if>
		</div>

		<div>
			<c:if test="${not empty nuevaConsulta}">
				<div class="panel panel-primary">

					<!-- cabecera -->

					<div class="panel-heading">
						<h3 class="panel-title">
							<fmt:message key="consulta.form.new.title" />
						</h3>
					</div>

					<!-- contenido -->

					<div>
						<!-- inicio contenido -->
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="consulta.form.field.nombre" />
							</div>
							<div class="col-lg-9">${nuevaConsulta.nombre}.</div>
						</div>
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="consulta.form.field.definicion" />
							</div>
							<div class="col-lg-9">${nuevaConsulta.definicion}.</div>
						</div>
					</div>
					<!-- fin contenido -->

				</div>
				<!-- class="panel panel-primary" -->
			</c:if>
		</div>


		<div>
			<c:if test="${not empty deletedConsulta}">
				<div class="panel panel-primary">

					<!-- cabecera -->

					<div class="panel-heading">
						<h3 class="panel-title">
							<fmt:message key="consulta.form.deleted.title" />
						</h3>
					</div>

					<!-- contenido -->

					<div>
						<!-- inicio contenido -->
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="consulta.form.field.nombre" />
							</div>
							<div class="col-lg-9">${deletedConsulta.nombre}.</div>
						</div>
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="consulta.form.field.definicion" />
							</div>
							<div class="col-lg-9">${deletedConsulta.definicion}.</div>
						</div>
					</div>
					<!-- fin contenido -->

				</div>
				<!-- class="panel panel-primary" -->
			</c:if>
		</div>


		<div>
			<a class="btn btn-primary btn-sm"
				href="${pageContext.request.contextPath}/consulta/new.html"> <fmt:message
					key="consulta.form.new.title" />
			</a>
		</div>
		
		
		<!-- Importante: pagination! -->
		<c:set var="baseUrl" value="/consulta/pages" />
		
		<c:url var="firstUrl" value="${baseUrl}/1" />
		<c:url var="lastUrl" value="${baseUrl}/${totalPages}" />
		<c:url var="prevUrl" value="${baseUrl}/${currentIndex - 1}" />
		<c:url var="nextUrl" value="${baseUrl}/${currentIndex + 1}" />
		

		<div id="pagination-up">
			<%@ include file="/WEB-INF/views/include-pagination.jsp"%>
		</div>
		
		<div>
			<table
				class="table table-hover table-striped table-bordered table-condensed">
	        <thead>
	          <tr>
	            <th>#</th>
	            <th>ID</th>
	            <th><spring:message code="consulta.form.field.nombre" /></th>
	            <th><spring:message code="consulta.form.field.definicion" /></th>
	          </tr>
	        </thead>
	         <tbody>
				<c:forEach items="${consultas}" var="consulta">
			          <tr>
			            <td><a href="${pageContext.request.contextPath}/consulta/delete/${consulta.id}.html"
							class="btn btn-primary btn-xs">
							<fmt:message key="consulta.form.btn.del" />
							</a></td>

			            <td><a href="${pageContext.request.contextPath}/consulta/edit/${consulta.id}.html">
			            	<c:out value="${consulta.id}" />
										</a></td>
			            <td><a href="${pageContext.request.contextPath}/consulta/edit/${consulta.id}.html">
			            	<c:out value="${consulta.nombre}" />
										</a></td>
			            <td><a href="${pageContext.request.contextPath}/consulta/edit/${consulta.id}.html">
			            	<c:out value="${fn:substring(consulta.definicion, 0, 20)}" />
										</a></td>
			          </tr>
	 		    </c:forEach>
	          </tbody>
	        </table>
		
		</div>

		<div id="pagination-down">
			<%@ include file="/WEB-INF/views/include-pagination.jsp"%>
		</div>
		
		<%@ include file="/WEB-INF/views/include-goHome.jsp"%>
		
	</div>


	<%@ include file="/WEB-INF/views/include-jquery.jsp"%>
	
</body>
</html>