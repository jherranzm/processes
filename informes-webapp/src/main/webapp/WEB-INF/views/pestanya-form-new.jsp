<%@ include file="/WEB-INF/views/include.jsp"%>


<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message text="missing" code="pestanya.form.new.title" /></title>

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

		<div class="panel panel-primary">
		
			<!-- cabecera -->
			
 	       <div class="panel-heading">
	          <h3 class="panel-title">
					<spring:message text="missing" code="pestanya.form.new.title" />
	          </h3>
 	       </div>
        
        	<!-- contenido -->
        	
	        <div><!-- inicio contenido -->
				<form:form method="POST" commandName="pestanyaForm"
					 class="form-horizontal"
					action="${pageContext.request.contextPath}/pestanya/save.html">
		
					<div class="form-group">
						<label for="nombre" class="col-lg-2 control-label">
							<spring:message code="pestanya.form.field.nombre" />
						</label>
						<div class="col-lg-10">
							<input id="nombre" name="nombre" type="text" 
							class="form-control" placeholder="Nombre">
						</div> 
					</div>
		
					<div class="form-group">
						<label for="rango" class="col-lg-2 control-label">
							<spring:message code="pestanya.form.field.rango" />
						</label> 
						<div class="col-lg-10">
							<input class="form-control" id="rango" name="rango" placeholder="Rango">
						</div> 
					</div>
		
					<div class="form-group">
						<label for="consultaSQL_id" class="col-lg-2 control-label">
							<spring:message code="pestanya.form.field.consulta" />
						</label> 
						<div class="col-lg-10">
							<form:select path="consultaSQL_id">
								<form:options items="${consultas}" itemValue="id"
									itemLabel="nombre" />
							</form:select>
						</div> 
					</div>
		
					<div class="form-group">
						<label for="numFilaInicial" class="col-lg-2 control-label">
							<spring:message code="pestanya.form.field.numfilainicial" />
						</label> 
						<div class="col-lg-10">
							<form:select path="numFilaInicial">
								<c:forEach items="${numsFilaInicial}" var="num">
									<form:option value="${num}">${num}</form:option>
								</c:forEach>
							</form:select>
						</div> 
					</div>

					<div class="form-group">
						<div class="col-md-offset-4 col-md-4">
							<p class="text-center">
								<button id="btn-save" type="submit" class="btn btn-primary">
									<spring:message text="missing" code="pestanya.form.btn.save" />
								</button>
								<a href="${pageContext.request.contextPath}/pestanya/pages/1"
									id="btn-back" class="btn btn-default"> 
									<spring:message text="missing" code="pestanya.form.btn.back" />
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