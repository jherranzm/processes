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

import telefonica.aaee.segmentacion.exceptions.TerritorioNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.Territorio;


@Repository
public class TerritorioService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String TERRITORIO_FIND_BY_NOMBRE = "Territorio.findByNombre";
	private static final String TERRITORIO_FIND_BY_CODIGO = "Territorio.findByCodigo";
	private static final String TERRITORIO_FIND_ALL = "Territorio.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Territorio, Long> repo;
	

	public TerritorioService() {
	}
	
	
	public TerritorioService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Territorio, Long> entityInfo = new JpaMetamodelEntityInformation<Territorio, Long>(Territorio.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Territorio, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Territorios:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Territorio por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Territorio> findAll() {
		
		List<Territorio> lista = null;
		
		Query query = em.createNamedQuery(TERRITORIO_FIND_ALL);
	    lista = (List<Territorio>)query.getResultList();
		
		return lista;
	}

	public Iterable<Territorio> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Territorio findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Territorio update(Territorio modificada) throws TerritorioNotFoundException {
		Territorio item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new TerritorioNotFoundException();

		Territorio result = repo.saveAndFlush(modificada);
		logger.info("Territorio actualizado : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Territorio> findByNombre(String nombre) {
		List<Territorio> lista = null;
		
		Query query = em.createNamedQuery(TERRITORIO_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Territorio>)query.getResultList();
	    
	    return lista ;
	}

	public Territorio findByCodigo(String cod) {
		Territorio sector = null;
		
		Query query = em.createNamedQuery(TERRITORIO_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			sector = (Territorio) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado el Territorio con Código " + cod);
		}
	    
	    return sector ;
	}



	@Transactional
	public Territorio create(Territorio nuevo) {
		
		logger.info("Incluimos el Territorio en la tabla...");
		Territorio result = repo.saveAndFlush(nuevo);
		logger.info("Territorio creado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<Territorio> saveAll(Set<Territorio> oficinas){
		return repo.save(oficinas);
	}


	
}
