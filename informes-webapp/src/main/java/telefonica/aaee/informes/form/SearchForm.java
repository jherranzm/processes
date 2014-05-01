package telefonica.aaee.informes.form;

import java.io.Serializable;

public class SearchForm   implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String queBuscar;

	public SearchForm() {
		super();
	}

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQueBuscar() {
		return queBuscar;
	}

	public void setQueBuscar(String queBuscar) {
		this.queBuscar = queBuscar;
	}



	@Override
	public String toString() {
		return "SearchForm [id=" + id + ", queBuscar=" + queBuscar + "]";
	}
	
	
	
	

}