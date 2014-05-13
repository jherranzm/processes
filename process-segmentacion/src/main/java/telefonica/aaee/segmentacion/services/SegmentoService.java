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

import telefonica.aaee.segmentacion.exceptions.SegmentoNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.Segmento;


@Repository
public class SegmentoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String SEGMENTO_FIND_BY_NOMBRE = "Segmento.findByNombre";
	private static final String SEGMENTO_FIND_BY_CODIGO = "Segmento.findByCodigo";
	private static final String SEGMENTO_FIND_ALL = "Segmento.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Segmento, Long> repo;
	

	public SegmentoService() {
	}
	
	
	public SegmentoService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Segmento, Long> entityInfo = new JpaMetamodelEntityInformation<Segmento, Long>(Segmento.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Segmento, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Segmentos:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Segmento por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Segmento> findAll() {
		
		List<Segmento> lista = null;
		
		Query query = em.createNamedQuery(SEGMENTO_FIND_ALL);
	    lista = (List<Segmento>)query.getResultList();
		
		return lista;
	}

	public Iterable<Segmento> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Segmento findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Segmento update(Segmento modificada) throws SegmentoNotFoundException {
		Segmento consulta = repo.findOne(new Long(modificada.getId()));

		if (consulta == null)
			throw new SegmentoNotFoundException();

		Segmento result = repo.saveAndFlush(modificada);
		logger.info("Segmento actualizada : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Segmento> findByNombre(String nombre) {
		List<Segmento> lista = null;
		
		Query query = em.createNamedQuery(SEGMENTO_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Segmento>)query.getResultList();
	    
	    return lista ;
	}

	public Segmento findByCodigo(String cod) {
		Segmento oficina = null;
		
		Query query = em.createNamedQuery(SEGMENTO_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			oficina = (Segmento) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado la Segmento con Código " + cod);
		}
	    
	    return oficina ;
	}



	@Transactional
	public Segmento create(Segmento nuevo) {
		
		logger.info("Incluimos la Segmento en la tabla...");
		Segmento result = repo.saveAndFlush(nuevo);
		logger.info("Segmento creado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<Segmento> saveAll(Set<Segmento> oficinas){
		return repo.save(oficinas);
	}


	
}
