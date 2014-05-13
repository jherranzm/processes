package telefonica.aaee.segmentacion.services;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.segmentacion.exceptions.ClienteNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.Cliente;


@Repository
public class ClienteService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String SECTOR_FIND_BY_NOMBRE = "Cliente.findByNombre";
	private static final String SECTOR_FIND_BY_CUC = "Cliente.findByCuc";
	private static final String SECTOR_FIND_BY_CIF = "Cliente.findByCif";
	private static final String SECTOR_FIND_BY_CUC_G = "Cliente.findByCucG";
	private static final String SECTOR_FIND_ALL = "Cliente.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Cliente, Long> repo;
	

	public ClienteService() {
	}
	
	
	public ClienteService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Cliente, Long> entityInfo = new JpaMetamodelEntityInformation<Cliente, Long>(Cliente.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Cliente, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Clientes:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Cliente por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> findAll() {
		
		List<Cliente> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_ALL);
	    lista = (List<Cliente>)query.getResultList();
		
		return lista;
	}

	public Iterable<Cliente> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Cliente findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Cliente update(Cliente modificada) throws ClienteNotFoundException {
		Cliente item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new ClienteNotFoundException();

		Cliente result = repo.saveAndFlush(modificada);
		logger.info("Modificado : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Cliente> findByNombre(String nombre) {
		List<Cliente> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Cliente>)query.getResultList();
	    
	    return lista ;
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> findByCucG(String cod) {
		List<Cliente> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_CUC_G);
		query.setParameter("cod", cod);
	    lista = (List<Cliente>)query.getResultList();
	    
	    return lista ;
	}

	public Cliente findByCuc(String cod) {
		Cliente sector = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_CUC);
		query.setParameter("cod", cod);
		try {
			sector = (Cliente) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado el Cliente con Código " + cod);
		}
	    
	    return sector ;
	}

	public Cliente findByCif(String cod) {
		Cliente sector = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_CIF);
		query.setParameter("cod", cod);
		try {
			sector = (Cliente) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado el Cliente con Cif " + cod);
		}
	    
	    return sector ;
	}


	@Transactional
	public Cliente create(Cliente nuevo) {
		
		logger.info("Guardamos el Cliente...");
		Cliente result = repo.saveAndFlush(nuevo);
		logger.info("Cliente guardado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<Cliente> saveAll(Set<Cliente> oficinas){
		return repo.save(oficinas);
	}
	
	@Transactional
	public void loadDataInfile(String file){
		String sql = "LOAD DATA LOCAL INFILE '"+ file +"' "
				+ "into table maestras.tbl_cliente "
				+ "fields terminated by ';' enclosed by '\"' "
				+ "(Cod_Cliente, Tipo_Doc, CIF_Cliente, NOM_Cliente, Cod_Cliente_G); ";
		
		logger.info(sql);
		Query query = em.createNativeQuery(sql);
		logger.info("Inicio de carga masiva de clientes...");
		long ret = query.executeUpdate();
		logger.info(String.format("Fin de carga masiva! Se han cargado [%d] registros.", ret));
	}


	
}
