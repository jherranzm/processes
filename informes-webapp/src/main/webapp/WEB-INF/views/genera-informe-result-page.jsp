<%@ include file="/WEB-INF/views/include.jsp"%>




<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message text="missing" code="genera.informe.success.title.text" /></title>


<%@ include file="/WEB-INF/views/include-css.jsp"%>


</head>
<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp"%>

	<div id="wrap">

		<div class="container">

			<div class="page-header">
				<h1>
					<spring:message text="missing" code="genera.informe.success.title.text" />
				</h1>
			</div><!-- .page-header -->

			<div id="urls">
				<h3 style="color: blue;">
					<spring:message text="missing" code="genera.informe.success.info.text" />
				</h3>
				<br>
				<c:forEach items="${FORM.urls}" var="url">
					<li><a href="${pageContext.request.contextPath}/getFile/file/${url.fileName}/${url.fileExt}">${url.fileName}</a> - ${url.fileSize} bytes</li>
				</c:forEach>
			</div><!-- #urls -->

		</div><!-- .container -->

	</div><!-- wrap -->



	<%@ include file="/WEB-INF/views/footer.jsp"%>

	<%@ include file="/WEB-INF/views/include-jquery.jsp"%>

</body>
</html>