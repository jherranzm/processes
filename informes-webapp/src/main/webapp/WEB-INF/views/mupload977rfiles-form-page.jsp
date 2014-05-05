<%@ include file="/WEB-INF/views/include.jsp"%>



<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message text="missing" code="mupload977r.form.title.text" /></title>

<%@ include file="/WEB-INF/views/include-css.jsp"%>


</head>
<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp"%>


	<div id="wrap">

		<div class="container">

			<div class="page-header">
				<h1>
					<spring:message text="missing" code="mupload977r.form.title.text" />
				</h1>
			</div>

			<form:form method="post"
				action="${pageContext.request.contextPath}/977r?${_csrf.parameterName}=${_csrf.token}"
				modelAttribute="uploadForm" 
				enctype="multipart/form-data">

				<p>
					<spring:message text="missing" code="mupload.form.info.text" />
				</p>
				
				<div>
					<a id="addFile" class="btn btn-primary" href="#"> 
					<spring:message text="missing" code="mupload977r.form.btn.addfile.text" />
					</a>
				</div>
				
				<div class="row">
					<div class="form-group">
						<label for="nombre" class="col-lg-2 control-label">
							<spring:message text="missing" code="mupload977r.form.field.acuerdo" />
						</label>
						<div class="col-lg-10">
							<input id="acuerdo" name="acuerdo" type="text" 
							class="form-control" 
							placeholder="${uploadForm.acuerdo}" 
							value="${uploadForm.acuerdo}">
						</div> 
					</div>
				</div>

				<div class="row">
					<div class="form-group">
						<div class="col-lg-2">
							<spring:message text="missing" code="mupload977r.form.field.detalleLlamadas" />
						</div> 
						<div class="col-lg-10">
							<form:checkbox path="detalleLlamadas" value="false" />
						</div> 
					</div>
				</div>

				<div class="row">

					<div class="form-group">
						<div class="col-lg-2">
							<spring:message text="missing" code="mupload977r.form.field.detalleLlamadasRI" />
						</div> 
						<div class="col-lg-10">
							<form:checkbox path="detalleLlamadasRI" value="false" />
						</div> 
					</div>
				</div>


				<table id="fileTable">
					<tr>
						<td><input name="files[0]" type="file" /></td>
					</tr>
					<tr>
						<td><input name="files[1]" type="file" /></td>
					</tr>
				</table>
				<br />
				<input 
					type="submit"
					value="<spring:message text="missing" code="mupload977r.form.btn.upload.text" />" />

					 
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
