package telefonica.aaee.informes.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The persistent class for the tbl_ConsultasSQL database table.
 * 
 */
@Entity
@Table(name = "tbl_ConsultasSQL")
@NamedQueries({
		@NamedQuery(name = "Consulta.findAll", query = "SELECT c FROM Consulta c"),
		@NamedQuery(name = "Consulta.findByNombreDuplicado", query = "SELECT p "
				+ "FROM Consulta p "
				+ "WHERE "
				+ " 1 = 1 "
				+ " AND p.nombre = :nom " + " AND p.id <> :id "),
		@NamedQuery(name = "Consulta.findByNombre", query = "SELECT p "
				+ "FROM Consulta p " 
				+ "WHERE " 
				+ " 1 = 1 "
				+ " AND p.nombre = :nom ") 
})
public class Consulta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@NotNull
	private String definicion;

	@NotNull
	private String nombre;

	//bi-directional many-to-one association to Pestanya
	@OneToMany(mappedBy = "consulta")
	private List<Pestanya> pestanyes;

	public Consulta() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDefinicion() {
		return this.definicion;
	}

	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Pestanya> getPestanyes() {
		return this.pestanyes;
	}

	public void setPestanyes(List<Pestanya> pestanyes) {
		this.pestanyes = pestanyes;
	}

	public Pestanya addPestanya(Pestanya pestanye) {
		getPestanyes().add(pestanye);
		pestanye.setConsulta(this);

		return pestanye;
	}

	public Pestanya removePestanya(Pestanya pestanye) {
		getPestanyes().remove(pestanye);
		pestanye.setConsulta(null);

		return pestanye;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Consulta [id=");
		builder.append(id);
		builder.append(", definicion=");
		builder.append(definicion);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private long id;
		private String definicion;
		private String nombre;
		private List<Pestanya> pestanyes;

		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Builder definicion(String definicion) {
			this.definicion = definicion;
			return this;
		}

		public Builder nombre(String nombre) {
			this.nombre = nombre;
			return this;
		}

		public Builder pestanyes(List<Pestanya> pestanyes) {
			this.pestanyes = pestanyes;
			return this;
		}

		public Consulta build() {
			return new Consulta(this);
		}
	}

	private Consulta(Builder builder) {
		this.id = builder.id;
		this.definicion = builder.definicion;
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
		result = prime * result
				+ ((definicion == null) ? 0 : definicion.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Consulta other = (Consulta) obj;
		if (definicion == null) {
			if (other.definicion != null)
				return false;
		} else if (!definicion.equals(other.definicion))
			return false;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}