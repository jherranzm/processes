package telefonica.aaee.model.datos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the tbl_acuerdos database table.
 * 
 */
@Entity
@Table(name="tbl_acuerdos")
@XmlRootElement
public class TblAcuerdos 

	extends telefonica.aaee.model.GenericXML
	implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	

	private String acuerdo;

   
    

	public String getAcuerdo() {
		return this.acuerdo;
	}

	public void setAcuerdo(String acuerdo) {
		this.acuerdo = acuerdo;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}














	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TblAcuerdos [id=" + id + ",\\n acuerdo=" + acuerdo + "]";
	}
	
	
	

}