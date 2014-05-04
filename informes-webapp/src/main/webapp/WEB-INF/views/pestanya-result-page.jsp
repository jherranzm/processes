<%@ include file="/WEB-INF/views/include.jsp"%>


<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="pestanya.form.list.title" text="missing" /></title>

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
			<c:if test="${not empty nuevaPestanya}">
				<div class="panel panel-primary">

					<!-- cabecera -->

					<div class="panel-heading">
						<h3 class="panel-title">
							<spring:message text="missing" code="pestanya.form.new.title" />
						</h3>
					</div>

					<!-- contenido -->

					<div>
						<!-- inicio contenido -->
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="pestanya.form.field.nombre" />
							</div>
							<div class="col-lg-9">${nuevaPestanya.nombre}.</div>
						</div>
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="pestanya.form.field.rango" />
							</div>
							<div class="col-lg-9">${nuevaPestanya.rango}.</div>
						</div>
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="pestanya.form.field.numfilainicial" />
							</div>
							<div class="col-lg-9">${nuevaPestanya.numFilaInicial}.</div>
						</div>
						<div class="row">
							<div class="col-lg-3">
								<spring:message code="pestanya.form.field.consulta" />
							</div>
							<div class="col-lg-9">${nuevaPestanya.consulta.nombre}.</div>
						</div>
					</div>
					<!-- fin contenido -->

				</div>
				<!-- class="panel panel-primary" -->
			</c:if>
		</div>


		<div>
			<div class="row">
				<div class="col-lg-2">
					<a class="btn btn-primary btn-sm"
						href="${pageContext.request.contextPath}/pestanya/new.html"> 
						<spring:message text="missing" code="pestanya.form.new.title" />
					</a>
				</div>
				<div class="col-lg-10">
					<form:form method="POST" 
						modelAttribute="searchForm"
						class="form-horizontal"
						action="${pageContext.request.contextPath}/pestanya/search.html">
						<div class="form-group">

							<div class="row">
								<div class="col-lg-6">
									<input id="queBuscar" name="queBuscar" type="text"
										class="form-control">
								</div>
								<div>
									<button id="btn-save" type="submit" class="btn btn-primary btn-sm">
										<spring:message text="missing" code="pestanya.form.btn.search" />
									</button>
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		
		<!-- Importante: pagination! -->
		<c:set var="baseUrl" value="/pestanya/pages" />
		
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
						<th><spring:message code="pestanya.form.field.nombre" /></th>
						<th><spring:message code="pestanya.form.field.rango" /></th>
						<th><spring:message code="pestanya.form.field.numfilainicial" /></th>
						<th><spring:message code="pestanya.form.field.consulta" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pestanyes}" var="pestanya">
						<tr>
							<td><a
								href="${pageContext.request.contextPath}/pestanya/delete/${pestanya.id}.html"
								class="btn btn-primary btn-xs"> 
								<spring:message text="missing" code="pestanya.form.btn.del" />
							</a></td>

							<td><a
								href="${pageContext.request.contextPath}/pestanya/edit/${pestanya.id}.html">
									<c:out value="${pestanya.id}" /></a></td>
							<td><a
								href="${pageContext.request.contextPath}/pestanya/edit/${pestanya.id}.html">
									<c:out value="${pestanya.nombre}" /></a></td>
							<td><a
								href="${pageContext.request.contextPath}/pestanya/edit/${pestanya.id}.html">
									<c:out value="${pestanya.rango}" /></a></td>
							<td><a
								href="${pageContext.request.contextPath}/pestanya/edit/${pestanya.id}.html">
								<c:out value="${pestanya.numFilaInicial}" /></a></td>
							<td><a
								href="${pageContext.request.contextPath}/pestanya/edit/${pestanya.id}.html">
								<c:out value="${pestanya.consulta.nombre}" /></a></td>
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