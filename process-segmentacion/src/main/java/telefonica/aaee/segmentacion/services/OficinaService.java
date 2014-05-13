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

import telefonica.aaee.segmentacion.exceptions.OficinaNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.Oficina;


@Repository
public class OficinaService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String OFICINA_FIND_BY_NOMBRE = "Oficina.findByNombre";
	private static final String OFICINA_FIND_BY_CODIGO = "Oficina.findByCodigo";
	private static final String OFICINA_FIND_ALL = "Oficina.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Oficina, Long> repo;
	

	public OficinaService() {
	}
	
	
	public OficinaService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Oficina, Long> entityInfo = new JpaMetamodelEntityInformation<Oficina, Long>(Oficina.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Oficina, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Oficinas:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Oficina por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Oficina> findAll() {
		
		List<Oficina> lista = null;
		
		Query query = em.createNamedQuery(OFICINA_FIND_ALL);
	    lista = (List<Oficina>)query.getResultList();
		
		return lista;
	}

	public Iterable<Oficina> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Oficina findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Oficina update(Oficina modificada) throws OficinaNotFoundException {
		Oficina consulta = repo.findOne(new Long(modificada.getId()));

		if (consulta == null)
			throw new OficinaNotFoundException();

		Oficina result = repo.saveAndFlush(modificada);
		logger.info("Oficina actualizada : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Oficina> findByNombre(String nombre) {
		List<Oficina> lista = null;
		
		Query query = em.createNamedQuery(OFICINA_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Oficina>)query.getResultList();
	    
	    return lista ;
	}

	public Oficina findByCodigo(String cod) {
		Oficina oficina = null;
		
		Query query = em.createNamedQuery(OFICINA_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			oficina = (Oficina) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado la Oficina con Código " + cod);
		}
	    
	    return oficina ;
	}



	@Transactional
	public Oficina create(Oficina nuevo) {
		
		logger.info("Incluimos la Oficina en la tabla...");
		Oficina result = repo.saveAndFlush(nuevo);
		logger.info("Oficina creado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<Oficina> saveAll(Set<Oficina> oficinas){
		return repo.save(oficinas);
	}


	
}
