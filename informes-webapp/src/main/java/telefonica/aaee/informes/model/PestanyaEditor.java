package telefonica.aaee.informes.model;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import telefonica.aaee.informes.services.PestanyaService;

@Component
public class PestanyaEditor extends PropertyEditorSupport{

	protected final Log logger = LogFactory.getLog(getClass());

	private PestanyaService pestanyaService;
	
	public PestanyaEditor(){
	}
	
	public PestanyaEditor(PestanyaService pestanyaService){
		this.pestanyaService = pestanyaService;
	};

	
	
	// Converts a String to a Category (when submitting form)
    @Override
    public void setAsText(String text) {
    	logger.info("Llega:" + text);
    	long key = new Long(text);
    	logger.info("key:" + text);
    	if(pestanyaService == null){
    		logger.error("\n\nMal vamos...!");
    	}else{
	        Pestanya c = pestanyaService.findById(key);
	        logger.info("Pestanya:" + c.toString());
	        this.setValue(c);
    	}
    }

    // Converts a Category to a String (when displaying form)
    @Override
    public String getAsText() {
    	Pestanya c = (Pestanya) this.getValue();
        return (new Long(c.getId())).toString();
    }
}