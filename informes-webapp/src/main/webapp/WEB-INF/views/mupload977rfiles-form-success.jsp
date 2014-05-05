<%@ include file="/WEB-INF/views/include.jsp"%>




<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message text="missing" code="mupload977r.success.title.text" /></title>


<%@ include file="/WEB-INF/views/include-css.jsp"%>


</head>
<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp"%>

	<div id="wrap">

		<div class="container">

			<div class="page-header">
				<h1>
					<spring:message text="missing" code="mupload977r.success.title.text" />
				</h1>
			</div><!-- .page-header -->

			<div id="listUploadedFiles">
				<h3 style="color: green;">
					<spring:message text="missing" code="mupload.success.info.text" />
				</h3>
				<br>
				<c:forEach items="${FORM.uploadedFiles}" var="file">
					<li><a href="${file.url}">${file.fileName}</a> - ${file.fileSize} bytes </li>
				</c:forEach>
			</div><!-- #listUploadedFiles -->

		</div><!-- .container -->

	</div><!-- wrap -->



	<%@ include file="/WEB-INF/views/footer.jsp"%>

	<%@ include file="/WEB-INF/views/include-jquery.jsp"%>

</body>
</html>