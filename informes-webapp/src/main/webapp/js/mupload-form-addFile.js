$(document).ready(function() {
	
    //add more file components if Add is clicked
    $("#addFile").click(function(){

    	console.log("entramos con :" + $('#fileTable tr').children().length + "..!");
    	var fileIndex = $('#fileTable tr').children().length;
    	console.log("addFile." + fileIndex + "..!");
        $('#fileTable').append(
                '<tr><td>'+
                '   <input type="file" name="files['+ fileIndex +']" />'+
                '</td></tr>');
    	console.log("salimos con :" + $('#fileTable tr').children().length + "..!");
    });
    
    
    
     
});
