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

@Entity
@Table(name = "tbl_cliente")
@NamedQueries({
	@NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
	@NamedQuery(name = "Cliente.findByNombre", query = "SELECT p "
			+ "FROM Cliente p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.nomCliente = :nom "),
	@NamedQuery(name = "Cliente.findByCuc", query = "SELECT p "
			+ "FROM Cliente p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.cucCliente = :cod "),
	@NamedQuery(name = "Cliente.findByCucG", query = "SELECT p "
			+ "FROM Cliente p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.cucClienteG = :cod "),
	@NamedQuery(name = "Cliente.findByCif", query = "SELECT p "
			+ "FROM Cliente p " 
			+ "WHERE " 
			+ " 1 = 1 "
			+ " AND p.cifCliente = :cod ")  
})
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Cod_Cliente")
	private String cucCliente;

	@Column(name = "Tipo_Doc")
	private String tipoDocCliente;

	@Column(name = "CIF_Cliente")
	private String cifCliente;

	@Column(name = "NOM_Cliente")
	private String nomCliente;

	@Column(name = "Cod_Cliente_G", nullable=true)
	private String cucClienteG;

	public Cliente() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCucCliente() {
		return cucCliente;
	}

	public void setCucCliente(String cucCliente) {
		this.cucCliente = cucCliente;
	}

	public String getTipoDocCliente() {
		return tipoDocCliente;
	}

	public void setTipoDocCliente(String tipoDocCliente) {
		this.tipoDocCliente = tipoDocCliente;
	}

	public String getCifCliente() {
		return cifCliente;
	}

	public void setCifCliente(String cifCliente) {
		this.cifCliente = cifCliente;
	}

	public String getNomCliente() {
		return nomCliente;
	}

	public void setNomCliente(String nomCliente) {
		this.nomCliente = nomCliente;
	}

	public String getCucClienteG() {
		return cucClienteG;
	}

	public void setCucClienteG(String cucClienteG) {
		this.cucClienteG = cucClienteG;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cifCliente == null) ? 0 : cifCliente.hashCode());
		result = prime * result
				+ ((cucCliente == null) ? 0 : cucCliente.hashCode());
		result = prime * result
				+ ((cucClienteG == null) ? 0 : cucClienteG.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((nomCliente == null) ? 0 : nomCliente.hashCode());
		result = prime * result
				+ ((tipoDocCliente == null) ? 0 : tipoDocCliente.hashCode());
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
		Cliente other = (Cliente) obj;
		if (cifCliente == null) {
			if (other.cifCliente != null)
				return false;
		} else if (!cifCliente.equals(other.cifCliente))
			return false;
		if (cucCliente == null) {
			if (other.cucCliente != null)
				return false;
		} else if (!cucCliente.equals(other.cucCliente))
			return false;
		if (cucClienteG == null) {
			if (other.cucClienteG != null)
				return false;
		} else if (!cucClienteG.equals(other.cucClienteG))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nomCliente == null) {
			if (other.nomCliente != null)
				return false;
		} else if (!nomCliente.equals(other.nomCliente))
			return false;
		if (tipoDocCliente == null) {
			if (other.tipoDocCliente != null)
				return false;
		} else if (!tipoDocCliente.equals(other.tipoDocCliente))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", cucCliente=" + cucCliente
				+ ", tipoDocCliente=" + tipoDocCliente + ", cifCliente="
				+ cifCliente + ", nomCliente=" + nomCliente + ", cucClienteG="
				+ cucClienteG + "]";
	}

	public static class Builder {
		private Long id;
		private String cucCliente;
		private String tipoDocCliente;
		private String cifCliente;
		private String nomCliente;
		private String cucClienteG;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder cucCliente(String cucCliente) {
			this.cucCliente = cucCliente;
			return this;
		}

		public Builder tipoDocCliente(String tipoDocCliente) {
			this.tipoDocCliente = tipoDocCliente;
			return this;
		}

		public Builder cifCliente(String cifCliente) {
			this.cifCliente = cifCliente;
			return this;
		}

		public Builder nomCliente(String nomCliente) {
			this.nomCliente = nomCliente;
			return this;
		}

		public Builder cucClienteG(String cucClienteG) {
			this.cucClienteG = cucClienteG;
			return this;
		}

		public Cliente build() {
			return new Cliente(this);
		}
	}

	private Cliente(Builder builder) {
		this.id = builder.id;
		this.cucCliente = builder.cucCliente;
		this.tipoDocCliente = builder.tipoDocCliente;
		this.cifCliente = builder.cifCliente;
		this.nomCliente = builder.nomCliente;
		this.cucClienteG = builder.cucClienteG;
	}
}
