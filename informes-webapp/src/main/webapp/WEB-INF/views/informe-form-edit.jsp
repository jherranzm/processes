<%@ include file="/WEB-INF/views/include.jsp"%>


<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><fmt:message key="informe.form.edit.title" /></title>

	<%@ include file="/WEB-INF/views/include-css.jsp" %>


</head>
<body>

	<%@ include file="/WEB-INF/views/include-menu.jsp" %>
		

	<div class="container">

		<div id="informe-mensajeError">
			<c:if test="${not empty mensajeError}">
				<div class="alert alert-error">
					<p>${mensajeError}</p>
				</div>
			</c:if>
		</div>

		<div id="informe-form" class="panel panel-primary">
		
			<!-- cabecera -->
			
 	       <div id="informe-form-titulo" class="panel-heading">
	          <h3 class="panel-title">
					<fmt:message key="informe.form.edit.title" />
					${informeForm.id}
	          </h3>
 	       </div>
        
        	<!-- contenido -->
        	
	        <div id="informe-form-contenido"><!-- inicio contenido -->
				<form:form method="POST" commandName="informeForm"
					 class="form-horizontal"
					action="${pageContext.request.contextPath}/informe/edit/${informeForm.id}.html">
		
					<div class="form-group">
						<label for="nombre" class="col-lg-2 control-label">
							<spring:message code="informe.form.field.nombre" />
						</label>
						<div class="col-lg-10">
							<input id="nombre" name="nombre" type="text" 
							class="form-control" placeholder="${informeForm.nombre}" value="${informeForm.nombre}">
						</div> 
					</div>
		
					<div class="form-group">
						<label for="pestanyes" class="col-lg-2 control-label">
							<spring:message code="informe.form.field.pestanyes" />
						</label> 
						
						<!-- Para que las pestanyes aparezcan checked, la entidad Pestanya tiene que 
							implementar los métodos equals() y hashcode() -->
						<div class="col-lg-10">
							<form:checkboxes 
												items="${pestanyesDisponibles}" 
												path="pestanyes"
												itemValue="id"
												itemLabel="nombre" 
												delimiter="<br/>"
												/>
						</div> 
					</div>

					<!-- Botones Save & Back -->
					<div class="form-group">
						<div class="col-md-offset-4 col-md-4">
							<p class="text-center">
								<button id="btn-save" type="submit" class="btn btn-primary">
									<fmt:message key="informe.form.btn.save" />
								</button>
								<a href="${pageContext.request.contextPath}/consulta/pages/1"
									id="btn-back" class="btn btn-default"> <fmt:message
										key="informe.form.btn.back" />
								</a>
							</p>
						</div>
					</div>


					 <!-- Spring Security -->
					 
					 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					 
					 
				</form:form>
	        </div><!-- fin contenido -->


    	  </div>





		<%@ include file="/WEB-INF/views/include-goHome.jsp"%>

	</div>



	<%@ include file="/WEB-INF/views/include-jquery.jsp"%>

</body>
</html>