<%@ include file="/WEB-INF/views/include.jsp" %>


<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="genera.informe.form.new.title" /></title>

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
					<spring:message text="missing"  code="genera.informe.form.new.title" />
	          </h3>
 	       </div>
        
        	<!-- contenido -->
        	
	        <div><!-- inicio contenido -->
				<form:form method="POST" commandName="generaInformeForm"
					 class="form-horizontal"
					action="${pageContext.request.contextPath}/generaInforme/apply">
		
					<div class="form-group">
						<label for="acuerdoId" class="col-lg-2 control-label">
							<spring:message code="genera.informe.form.field.acuerdo" />
						</label> 
						<div class="col-lg-10">
							<form:select path="acuerdoId">
								<form:options items="${acuerdos}" itemValue="id"
									itemLabel="acuerdo" />
							</form:select>
						</div> 
					</div>
		
					<div class="form-group">
						<div class="col-md-offset-4 col-md-4">
							<p class="text-center">
								<button id="btn-save" type="submit" class="btn btn-primary">
									<spring:message text="missing"  code="genera.informe.form.btn.apply" />
								</button>
								<a href="${pageContext.request.contextPath}/"
									id="btn-back" class="btn btn-default"> <spring:message text="missing" 
										code="genera.informe.form.btn.back" />
								</a>
							</p>
						</div>
					</div>
					 

					 <!-- Spring Security -->
					 
<%-- 					 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> --%>
					 
					 
				</form:form>
	        </div><!-- fin contenido -->


    	  </div>
		
		
		<%@ include file="/WEB-INF/views/include-goHome.jsp" %>
		

	</div><!-- .container -->


	<%@ include file="/WEB-INF/views/include-jquery.jsp" %>
	
</body>
</html>