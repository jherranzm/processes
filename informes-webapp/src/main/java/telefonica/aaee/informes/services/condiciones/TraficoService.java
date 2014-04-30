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

import telefonica.aaee.informes.exceptions.TraficoNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.Trafico;

@Repository
@Transactional
public class TraficoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String TRAFICO_FIND_ALL = "TRAFICO.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<Trafico, Long> repo;
	

	public TraficoService() {
	}
	
	
	public TraficoService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Trafico, Long> conceptoFacturableEntityInfo = new JpaMetamodelEntityInformation<Trafico, Long>(Trafico.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Trafico, Long>(conceptoFacturableEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Trafico:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Trafico por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Trafico> findAll() {
		
		List<Trafico> lista = null;
		
		Query query = em.createNamedQuery(TRAFICO_FIND_ALL);
	    lista = (List<Trafico>)query.getResultList();
		
		return lista;
	}

	public Iterable<Trafico> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<Trafico> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public Trafico findById(Long id) {
		return repo.findOne(id);
	}


	public Trafico update(Trafico mod) throws TraficoNotFoundException {
		Trafico original = repo.findOne(mod.getId());

		if (original == null)
			throw new TraficoNotFoundException();
		
		Trafico result = repo.saveAndFlush(mod);
		logger.info("Trafico " + result.toString());
		return result;
	}


	public Trafico create(Trafico nuevo) {
		
		logger.info("Guardamos el Trafico...");
		Trafico result = repo.saveAndFlush(nuevo);
		logger.info("Reasignacion guardado con ID " + result.getId());
		
		return result;
	}

}
