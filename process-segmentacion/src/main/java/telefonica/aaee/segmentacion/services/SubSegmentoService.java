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

import telefonica.aaee.segmentacion.exceptions.SubSegmentoNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.SubSegmento;


@Repository
public class SubSegmentoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String SUBSEGMENTO_FIND_BY_NOMBRE = "SubSegmento.findByNombre";
	private static final String SUBSEGMENTO_FIND_BY_CODIGO = "SubSegmento.findByCodigo";
	private static final String SUBSEGMENTO_FIND_ALL = "SubSegmento.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<SubSegmento, Long> repo;
	

	public SubSegmentoService() {
	}
	
	
	public SubSegmentoService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<SubSegmento, Long> entityInfo = new JpaMetamodelEntityInformation<SubSegmento, Long>(SubSegmento.class, em.getMetamodel());
        repo = new SimpleJpaRepository<SubSegmento, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de SubSegmentos:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de SubSegmento por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<SubSegmento> findAll() {
		
		List<SubSegmento> lista = null;
		
		Query query = em.createNamedQuery(SUBSEGMENTO_FIND_ALL);
	    lista = (List<SubSegmento>)query.getResultList();
		
		return lista;
	}

	public Iterable<SubSegmento> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public SubSegmento findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public SubSegmento update(SubSegmento modificada) throws SubSegmentoNotFoundException {
		SubSegmento consulta = repo.findOne(new Long(modificada.getId()));

		if (consulta == null)
			throw new SubSegmentoNotFoundException();

		SubSegmento result = repo.saveAndFlush(modificada);
		logger.info("SubSegmento actualizada : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<SubSegmento> findByNombre(String nombre) {
		List<SubSegmento> lista = null;
		
		Query query = em.createNamedQuery(SUBSEGMENTO_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<SubSegmento>)query.getResultList();
	    
	    return lista ;
	}

	public SubSegmento findByCodigo(String cod) {
		SubSegmento oficina = null;
		
		Query query = em.createNamedQuery(SUBSEGMENTO_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			oficina = (SubSegmento) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado la SubSegmento con Código " + cod);
		}
	    
	    return oficina ;
	}



	@Transactional
	public SubSegmento create(SubSegmento nuevo) {
		
		logger.info("Incluimos la SubSegmento en la tabla...");
		SubSegmento result = repo.saveAndFlush(nuevo);
		logger.info("SubSegmento creado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<SubSegmento> saveAll(Set<SubSegmento> oficinas){
		return repo.save(oficinas);
	}


	
}
