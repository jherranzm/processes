package telefonica.aaee.informes.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The persistent class for the tbl_pestanyes database table.
 * 
 */
@Entity
@Table(name = "tbl_pestanyes")
@NamedQueries({
		@NamedQuery(name = "Pestanya.findAll", query = "SELECT p FROM Pestanya p"),
		@NamedQuery(name = "Pestanya.findByNombreDuplicado", query = "SELECT p "
				+ "FROM Pestanya p "
				+ "WHERE "
				+ " 1 = 1 "
				+ " AND p.nombre = :nom " 
				+ " AND p.id <> :id "),
		@NamedQuery(name = "Pestanya.findByNombre", query = "SELECT p "
				+ "FROM Pestanya p " 
				+ "WHERE " 
				+ " 1 = 1 "
				+ " AND p.nombre = :nom ") })
public class Pestanya implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String nombre;

	@NotNull
	private int numFilaInicial;

	@NotNull
	private String rango;

	//bi-directional many-to-one association to Consulta
	@ManyToOne
	private Consulta consulta;

	//bi-directional many-to-one association to InformePestanya
	@OneToMany(mappedBy = "pestanya")
	private Set<InformePestanya> informes;

	public Pestanya() {
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

	public int getNumFilaInicial() {
		return this.numFilaInicial;
	}

	public void setNumFilaInicial(int numFilaInicial) {
		this.numFilaInicial = numFilaInicial;
	}

	public String getRango() {
		return this.rango;
	}

	public void setRango(String rango) {
		this.rango = rango;
	}

	public Consulta getConsulta() {
		return this.consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Set<InformePestanya> getInformes() {
//	public List<InformePestanya> getInformes() {
		return this.informes;
	}

	public void setInformes(Set<InformePestanya> informes) {
//	public void setInformes(List<InformePestanya> informes) {
		this.informes = informes;
	}

	public InformePestanya addInforme(InformePestanya informe) {
		getInformes().add(informe);
		informe.setPestanya(this);

		return informe;
	}

	public InformePestanya removeInforme(InformePestanya informe) {
		getInformes().remove(informe);
		informe.setPestanya(null);

		return informe;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pestanya [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", numFilaInicial=");
		builder.append(numFilaInicial);
		builder.append(", rango=");
		builder.append(rango);
		builder.append(", consulta=");
		builder.append(consulta);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		private Long id;
		private String nombre;
		private int numFilaInicial;
		private String rango;
		private Consulta consulta;
		private Set<InformePestanya> informes;
//		private List<InformePestanya> informes;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder nombre(String nombre) {
			this.nombre = nombre;
			return this;
		}

		public Builder numFilaInicial(int numFilaInicial) {
			this.numFilaInicial = numFilaInicial;
			return this;
		}

		public Builder rango(String rango) {
			this.rango = rango;
			return this;
		}

		public Builder consulta(Consulta consulta) {
			this.consulta = consulta;
			return this;
		}

		public Builder informes(Set<InformePestanya> informes) {
//		public Builder informes(List<InformePestanya> informes) {
			this.informes = informes;
			return this;
		}

		public Pestanya build() {
			return new Pestanya(this);
		}
	}

	private Pestanya(Builder builder) {
		this.id = builder.id;
		this.nombre = builder.nombre;
		this.numFilaInicial = builder.numFilaInicial;
		this.rango = builder.rango;
		this.consulta = builder.consulta;
		this.informes = builder.informes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((consulta == null) ? 0 : consulta.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + numFilaInicial;
		result = prime * result + ((rango == null) ? 0 : rango.hashCode());
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
		Pestanya other = (Pestanya) obj;
		if (consulta == null) {
			if (other.consulta != null)
				return false;
		} else if (!consulta.equals(other.consulta))
			return false;
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
		if (numFilaInicial != other.numFilaInicial)
			return false;
		if (rango == null) {
			if (other.rango != null)
				return false;
		} else if (!rango.equals(other.rango))
			return false;
		return true;
	}


	
	
}