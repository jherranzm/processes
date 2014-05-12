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

import telefonica.aaee.segmentacion.exceptions.GerenciaNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.Gerencia;


@Repository
public class GerenciaService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String SECTOR_FIND_BY_NOMBRE = "Gerencia.findByNombre";
	private static final String SECTOR_FIND_BY_CODIGO = "Gerencia.findByCodigo";
	private static final String SECTOR_FIND_ALL = "Gerencia.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Gerencia, Long> repo;
	

	public GerenciaService() {
	}
	
	
	public GerenciaService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Gerencia, Long> entityInfo = new JpaMetamodelEntityInformation<Gerencia, Long>(Gerencia.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Gerencia, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Gerencias:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Gerencia por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Gerencia> findAll() {
		
		List<Gerencia> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_ALL);
	    lista = (List<Gerencia>)query.getResultList();
		
		return lista;
	}

	public Iterable<Gerencia> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Gerencia findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Gerencia update(Gerencia modificada) throws GerenciaNotFoundException {
		Gerencia item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new GerenciaNotFoundException();

		Gerencia result = repo.saveAndFlush(modificada);
		logger.info("Modificado : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Gerencia> findByNombre(String nombre) {
		List<Gerencia> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Gerencia>)query.getResultList();
	    
	    return lista ;
	}

	public Gerencia findByCodigo(String cod) {
		Gerencia sector = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			sector = (Gerencia) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado el Gerencia con Código " + cod);
		}
	    
	    return sector ;
	}



	@Transactional
	public Gerencia create(Gerencia nuevo) {
		
		logger.info("Guardamos el Gerencia...");
		Gerencia result = repo.saveAndFlush(nuevo);
		logger.info("Gerencia guardado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<Gerencia> saveAll(Set<Gerencia> oficinas){
		return repo.save(oficinas);
	}


	
}
