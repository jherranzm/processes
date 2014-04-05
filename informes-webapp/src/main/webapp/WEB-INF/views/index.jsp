<%@ include file="/WEB-INF/views/include.jsp"%>


<!DOCTYPE html>
<html lang="en">
<head>

<meta charset="utf-8">



<%@ include file="/WEB-INF/views/include-css.jsp"%>
</head>

<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp"%>

	<div id="wrap">

		<div class="container">

			<div>
<%-- 				<sec:authorize access="hasAnyRole('ADMIN', 'ROLE_ADMIN')"> --%>
				<p>
					<a href="${pageContext.request.contextPath}/role/list.html">
						<spring:message code="role.form.list.title" text="missing" />
						<!-- 					Lista Consultas -->
					</a>
				</p>
				
				<p>
					<a href="${pageContext.request.contextPath}/usuario/list.html">
						<spring:message code="usuario.form.list.title" text="missing" />
						<!-- 					Lista Consultas -->
					</a>
				</p>
<%-- 				</sec:authorize> --%>
				
				<hr>

				<p>
					<a href="${pageContext.request.contextPath}/consulta/pages/1">
						<spring:message code="consulta.form.list.title" text="missing" />
						<!-- 					Lista Consultas -->
					</a>
				</p>
				<p>
					<a href="${pageContext.request.contextPath}/pestanya/pages/1">
						<spring:message code="pestanya.form.list.title" text="missing" />
						<!-- 					Lista Pestañas -->
					</a>
				</p>
				<p>
					<a href="${pageContext.request.contextPath}/informe/list.html">
						<spring:message code="informe.form.list.title" text="missing" />
						<!-- 					Lista Informes -->
					</a>
				</p>
				
				<hr>

				<p>
					<a href="${pageContext.request.contextPath}/ii/muploadiifiles-form.html">
						<spring:message code="ii.form.list.title" text="missing" />
					</a>
				</p>

				<p>
					<a href="${pageContext.request.contextPath}/hz/muploadhzfiles-form.html">
						<spring:message code="hz.form.list.title" text="missing" />
					</a>
				</p>

				<p>
					<a href="${pageContext.request.contextPath}/escenario/muploadescenariofiles-form.html">
						<spring:message code="escenario.form.list.title" text="missing" />
					</a>
				</p>

				<hr>

				<p>
					<a href="${pageContext.request.contextPath}/generaInforme/new">
						<spring:message code="genera.informe.form.list.title" text="missing" />
					</a>
				</p>
			</div>

		</div><!-- .container -->

	</div><!-- #wrap -->


	<%@ include file="/WEB-INF/views/footer.jsp"%>

	<%@ include file="/WEB-INF/views/include-jquery.jsp"%>

</body>
</html>