package telefonica.aaee.segmentacion.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import telefonica.aaee.segmentacion.util.Constantes;

@Entity
@Table(name = "tbl_reddeventas")
@NamedQueries({
	@NamedQuery(name = "RedDeVentas.findAll", query = "SELECT c FROM RedDeVentas c"),
	@NamedQuery(name = "RedDeVentas.findByNombre", query = "SELECT p "
			+ "FROM RedDeVentas p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND lower(p.nombre) = :nom "),
	@NamedQuery(name = "RedDeVentas.findByMatricula", query = "SELECT p "
			+ "FROM RedDeVentas p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.matricula = :mat ") 
})
public class RedDeVentas implements Serializable, Exportable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Matricula")
	private String matricula;

	@Column(name = "Nombre")
	private String nombre;

	public RedDeVentas() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((matricula == null) ? 0 : matricula.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedDeVentas other = (RedDeVentas) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("RedDeVentas [id=");
		builder2.append(id);
		builder2.append(", matricula=");
		builder2.append(matricula);
		builder2.append(", nombre=");
		builder2.append(nombre);
		builder2.append("]");
		return builder2.toString();
	}



	public static class Builder {
		private Long id;
		private String matricula;
		private String nombre;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder matricula(String matricula) {
			this.matricula = matricula;
			return this;
		}

		public Builder nombre(String nombre) {
			this.nombre = nombre;
			return this;
		}

		public RedDeVentas build() {
			return new RedDeVentas(this);
		}
	}

	private RedDeVentas(Builder builder) {
		this.id = builder.id;
		this.matricula = builder.matricula;
		this.nombre = builder.nombre;
	}

	public String toCSV(){
		StringBuilder sb = new StringBuilder();
		sb
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.matricula)
		.append(Constantes.COMILLAS_DOBLES).append(";")
		.append(Constantes.COMILLAS_DOBLES)
			.append(this.nombre)
		.append(Constantes.COMILLAS_DOBLES).append(";")
			;
		return sb.toString();
	}
}
