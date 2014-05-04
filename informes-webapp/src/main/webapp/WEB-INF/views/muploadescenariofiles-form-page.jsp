<%@ include file="/WEB-INF/views/include.jsp"%>



<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message text="missing" code="muploadescenario.form.title.text" /></title>

<%@ include file="/WEB-INF/views/include-css.jsp"%>


</head>
<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp"%>


	<div id="wrap">

		<div class="container">

			<div class="page-header">
				<h1>
					<spring:message text="missing" code="muploadescenario.form.title.text" />
				</h1>
			</div>

			<form:form method="post"
				action="${pageContext.request.contextPath}/escenario?${_csrf.parameterName}=${_csrf.token}"
				modelAttribute="uploadForm" enctype="multipart/form-data">

				<p>
					<spring:message text="missing" code="mupload.form.info.text" />
				</p>
				<table id="fileTable">
					<tr>
						<td><input name="files[0]" type="file" /></td>
					</tr>
				</table>
				<br />
				<input 
					type="submit"
					value="<spring:message text="missing" code="mupload.form.btn.upload.text" />" />

					 
					 <!-- Spring Security -->
					 
					 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					 
					 
			</form:form>

		</div>

	</div>
	<!-- #wrap -->



	<%@ include file="/WEB-INF/views/footer.jsp"%>

	<%@ include file="/WEB-INF/views/include-jquery.jsp"%>


</body>
</html>
