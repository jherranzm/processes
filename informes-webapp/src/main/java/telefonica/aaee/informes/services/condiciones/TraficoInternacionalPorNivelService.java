package telefonica.aaee.informes.services.condiciones;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.exceptions.TraficoInternacionalPorNivelNotFoundException;
import telefonica.aaee.informes.model.condiciones.TraficoInternacionalPorNivel;

@Repository
@Transactional
public class TraficoInternacionalPorNivelService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String TRFINTNIVEL_FIND_ALL = "TRFINTNIVEL.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<TraficoInternacionalPorNivel, Long> repo;
	

	public TraficoInternacionalPorNivelService() {
	}
	
	
	public TraficoInternacionalPorNivelService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<TraficoInternacionalPorNivel, Long> entityInfo = new JpaMetamodelEntityInformation<TraficoInternacionalPorNivel, Long>(TraficoInternacionalPorNivel.class, em.getMetamodel());
        repo = new SimpleJpaRepository<TraficoInternacionalPorNivel, Long>(entityInfo, em);
        
        logger.info("\n\n\n");
        logger.info("Número de TraficoInternacionalPorNivel:" + repo.findAll().size());
        logger.info("\n\n\n");

        logger.info("\n\n\n");
        logger.info("Número de TraficoInternacionalPorNivel:" + repo.findAll(new PageRequest(0, 5)).getNumberOfElements());
        logger.info("\n\n\n");
	
	}
	
	@SuppressWarnings("unchecked")
	public List<TraficoInternacionalPorNivel> findAll() {
		
		List<TraficoInternacionalPorNivel> lista = null;
		
		Query query = em.createNamedQuery(TRFINTNIVEL_FIND_ALL);
	    lista = (List<TraficoInternacionalPorNivel>)query.getResultList();
		
		return lista;
	}

	public Iterable<TraficoInternacionalPorNivel> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<TraficoInternacionalPorNivel> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public TraficoInternacionalPorNivel findById(Long id) {
		return em.find(TraficoInternacionalPorNivel.class, id);
	}


	public TraficoInternacionalPorNivel update(TraficoInternacionalPorNivel mod) throws TraficoInternacionalPorNivelNotFoundException {
		TraficoInternacionalPorNivel original = em.find(TraficoInternacionalPorNivel.class, new Long(mod.getId()));

		if (original == null)
			throw new TraficoInternacionalPorNivelNotFoundException();
		
		original.setPrecioPorMinuto(mod.getPrecioPorMinuto());
		original.setPrecioEspecial(mod.getPrecioEspecial());

		
		
		TraficoInternacionalPorNivel result = em.merge(original);
		em.flush();
		logger.info("TraficoInternacionalPorNivel " + result.toString());
		return result;
	}


	public TraficoInternacionalPorNivel create(TraficoInternacionalPorNivel nuevoCf) {
		
		
		logger.info("Guardamos el TraficoInternacionalPorNivel...");
		em.persist(nuevoCf);
		em.flush();
		logger.info("TraficoInternacionalPorNivel guardado con ID " + nuevoCf.getId());
		
		
		return nuevoCf;
	}

}
