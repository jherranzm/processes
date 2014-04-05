package telefonica.aaee.informes.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * The persistent class for the tbl_InformesXLS database table.
 * 
 */
@Entity
@Table(name = "tbl_InformesXLS")
@NamedQueries({
		@NamedQuery(name = "Informe.findAll", query = "SELECT i FROM Informe i"),
		@NamedQuery(name = "Informe.findByNombreDuplicado", query = "SELECT p "
				+ "FROM Informe p " 
				+ "WHERE " 
				+ " 1 = 1 "
				+ " AND p.nombre = :nom " 
				+ " AND p.id <> :id "),
		@NamedQuery(name = "Informe.findByNombre", query = "SELECT p "
				+ "FROM Informe p " 
				+ "WHERE " 
				+ " 1 = 1 "
				+ " AND p.nombre = :nom ") 
})
public class Informe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String nombre;

	//bi-directional many-to-one association to InformePestanya
	@OneToMany(mappedBy = "informe"
			, orphanRemoval=true
			, cascade = { CascadeType.ALL }
	)
	private Set<InformePestanya> pestanyes = new HashSet<InformePestanya>();

	@Transient
	private long numPestanyes;

	public Informe() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

//	public List<InformePestanya> getPestanyes() {
	public Set<InformePestanya> getPestanyes() {
		return this.pestanyes;
	}

	public void setPestanyes(Set<InformePestanya> pestanyes) {
//	public void setPestanyes(List<InformePestanya> pestanyes) {
		this.pestanyes = pestanyes;
	}

	public long getNumPestanyes() {
		return pestanyes.size();
	}

	public void setNumPestanyes(long numPestanyes) {
		this.numPestanyes = numPestanyes;
	}

	public InformePestanya addPestanya(InformePestanya pestanya) {
		getPestanyes().add(pestanya);
		pestanya.setInforme(this);

		return pestanya;
	}

	public InformePestanya removePestanya(InformePestanya pestanya) {
		getPestanyes().remove(pestanya);
		pestanya.setInforme(null);

		return pestanya;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Informe [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", pestanyes=");
		builder.append(pestanyes);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String nombre;
		private Set<InformePestanya> pestanyes;
//		private List<InformePestanya> pestanyes;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder nombre(String nombre) {
			this.nombre = nombre;
			return this;
		}

		public Builder pestanyes(Set<InformePestanya> pestanyes) {
//		public Builder pestanyes(List<InformePestanya> pestanyes) {
			this.pestanyes = pestanyes;
			return this;
		}

		public Informe build() {
			return new Informe(this);
		}
	}

	private Informe(Builder builder) {
		this.id = builder.id;
		this.nombre = builder.nombre;
		this.pestanyes = builder.pestanyes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Informe other = (Informe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	
	
	
	
}