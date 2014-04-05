		$(function(){
			
			
			$("#informe-save").live("click", informeSaveSubmitJson);
			
			$("#informe-saved").hide();

			
			$( "#pestanyesDisponibles, #pestanyesAsignadas" ).sortable({
				connectWith: ".connectedSortable",
				placeholder: "ui-state-highlight",
				receive : function(event,ui){
					//alert("${pageContext.request.contextPath}");
					var numPestanyes = $("#pestanyesAsignadas li").length;
					//alert("numPestanyes:" + numPestanyes);
					if(numPestanyes < 1){
						alert("Ha de haber como mínimo una pestaña...");
						$(ui.sender).sortable('cancel');
					}else{
						$("#00").remove();
					}
				}
			}).disableSelection();
			
		});
	
		function informeSaveSubmit(){
			
			var _nombre = $("#nombre").val();
			
			var queryString = $('#informe-form-new').serialize();
			alert("submit! data:[" + queryString + "]" );
			
			$.ajax({
				type: "POST",
				url: "/springExample03/informe/saveSubmit.do",
				
		 		data: queryString,
		 		
				success: function(data){
					var msg = "Modificación correctamente realizada!";
					alert("OK:" + msg);
				},

				error: function(data){
					alert("ERROR:" + data);
				}
			});

			return false;
		}
		
		
		function informeSaveSubmitJson(){
			
			var _nombre = $("#nombre").val();
			
			if(_nombre == null || _nombre == "" || $.trim(_nombre) == "" ){
				alert("ERROR: el Nombre del informe NO puede estar vacío...!");
				return false;
			}
			
			var queryString = $('#informe-form-new').serialize();
			alert("queryString:[" + queryString + "]" );
			
			$.ajax({
				type: "POST",
				url: "/springExample03/informe/saveSubmitJson.do",
				
		 		data: queryString,
		 		
				success: function(response){
					var msg = "Modificación correctamente realizada!";
					if(response.status == "SUCCESS"){
						$("#informe-saved-id").html(response.result.id);
						$("#informe-saved-nombre").html(response.result.nombre);
						$("#informe-saved").show();
						alert("OK:" + msg);
						
					}
				},

				error: function(data){
					alert("ERROR:" + data);
				}
			});

			return false;
		}