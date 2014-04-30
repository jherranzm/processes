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

import telefonica.aaee.informes.exceptions.TraficoInternacionalNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.TraficoInternacional;

@Repository
@Transactional
public class TraficoInternacionalService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String TRFINT_FIND_ALL = "TRFINT.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<TraficoInternacional, Long> repo;
	

	public TraficoInternacionalService() {
	}
	
	
	public TraficoInternacionalService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<TraficoInternacional, Long> conceptoFacturableEntityInfo = new JpaMetamodelEntityInformation<TraficoInternacional, Long>(TraficoInternacional.class, em.getMetamodel());
        repo = new SimpleJpaRepository<TraficoInternacional, Long>(conceptoFacturableEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de TraficoInternacional:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de TraficoInternacional por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<TraficoInternacional> findAll() {
		
		List<TraficoInternacional> lista = null;
		
		Query query = em.createNamedQuery(TRFINT_FIND_ALL);
	    lista = (List<TraficoInternacional>)query.getResultList();
		
		return lista;
	}

	public Iterable<TraficoInternacional> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<TraficoInternacional> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public TraficoInternacional findById(Long id) {
		return repo.findOne(id);
	}


	public TraficoInternacional update(TraficoInternacional mod) throws TraficoInternacionalNotFoundException {
		TraficoInternacional original = repo.findOne(new Long(mod.getId()));

		if (original == null)
			throw new TraficoInternacionalNotFoundException();
		
		TraficoInternacional result = repo.saveAndFlush(mod);
		logger.info("TraficoInternacional " + result.toString());
		return result;
	}


	public TraficoInternacional create(TraficoInternacional nuevo) {
		
		logger.info("Guardamos el TraficoInternacional...");
		TraficoInternacional result = repo.saveAndFlush(nuevo);
		logger.info("TraficoInternacional guardado con ID " + result.getId());
		
		return result;
	}

}
