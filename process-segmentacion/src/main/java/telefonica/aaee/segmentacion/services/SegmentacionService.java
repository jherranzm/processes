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

import telefonica.aaee.segmentacion.exceptions.SegmentacionNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.Segmentacion;


@Repository
public class SegmentacionService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String SEGMENTACION_FIND_BY_CODIGO = "Segmentacion.findByCuc";
	private static final String SEGMENTACION_FIND_ALL = "Segmentacion.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Segmentacion, Long> repo;
	

	public SegmentacionService() {
	}
	
	
	public SegmentacionService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Segmentacion, Long> entityInfo = new JpaMetamodelEntityInformation<Segmentacion, Long>(Segmentacion.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Segmentacion, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Segmentaciones:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Segmentacion por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Segmentacion> findAll() {
		
		List<Segmentacion> lista = null;
		
		Query query = em.createNamedQuery(SEGMENTACION_FIND_ALL);
	    lista = (List<Segmentacion>)query.getResultList();
		
		return lista;
	}

	public Iterable<Segmentacion> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Segmentacion findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Segmentacion update(Segmentacion modificada) throws SegmentacionNotFoundException {
		Segmentacion existente = repo.findOne(new Long(modificada.getId()));

		if (existente == null)
			throw new SegmentacionNotFoundException();

		Segmentacion result = repo.saveAndFlush(modificada);
		logger.info("Segmentacion " + result.toString());
		return result;
	}


	public Segmentacion findByCodigo(String cod) {
		Segmentacion oficina = null;
		
		Query query = em.createNamedQuery(SEGMENTACION_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			oficina = (Segmentacion) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado la Segmentacion con Código " + cod);
		}
	    
	    return oficina ;
	}



	@Transactional
	public Segmentacion create(Segmentacion nuevo) {
		
		logger.info("Guardamos la Segmentacion...");
		Segmentacion result = repo.saveAndFlush(nuevo);
		logger.info("Segmentacion guardada con ID " + result.getId());
		
		return result;
	}
	
	
	public List<Segmentacion> saveAll(Set<Segmentacion> oficinas){
		return repo.save(oficinas);
	}


	
}
