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

import telefonica.aaee.informes.exceptions.ConceptoFacturableNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.ConceptoFacturable;

@Repository
@Transactional
public class ConceptoFacturableService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String CF_FIND_ALL = "CF.findAll";
	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<ConceptoFacturable, Long> repo;
	

	public ConceptoFacturableService() {
	}
	
	
	public ConceptoFacturableService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<ConceptoFacturable, Long> conceptoFacturableEntityInfo = new JpaMetamodelEntityInformation<ConceptoFacturable, Long>(ConceptoFacturable.class, em.getMetamodel());
        repo = new SimpleJpaRepository<ConceptoFacturable, Long>(conceptoFacturableEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de ConceptoFacturable:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de ConceptoFacturable por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturable> findAll() {
		
		List<ConceptoFacturable> lista = null;
		
		Query query = em.createNamedQuery(CF_FIND_ALL);
	    lista = (List<ConceptoFacturable>)query.getResultList();
		
		return lista;
	}

	public Iterable<ConceptoFacturable> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<ConceptoFacturable> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public ConceptoFacturable findById(Long id) {
		return em.find(ConceptoFacturable.class, id);
	}


	public ConceptoFacturable update(ConceptoFacturable mod) throws ConceptoFacturableNotFoundException {
		ConceptoFacturable cfOriginal = em.find(ConceptoFacturable.class, new Long(mod.getId()));

		if (cfOriginal == null)
			throw new ConceptoFacturableNotFoundException();
		
		cfOriginal.setImporteAcuerdo(mod.getImporteAcuerdo());
		cfOriginal.setPrecioEspecial(mod.getPrecioEspecial());

		
		
		ConceptoFacturable cfResult = em.merge(cfOriginal);
		em.flush();
		logger.info("ConceptoFacturable " + cfResult.toString());
		return cfResult;
	}


	public ConceptoFacturable create(ConceptoFacturable nuevoCf) {
		
		
		logger.info("Guardamos el ConceptoFacturable...");
		em.persist(nuevoCf);
		em.flush();
		logger.info("ConceptoFacturable guardado con ID " + nuevoCf.getId());
		
		
		return nuevoCf;
	}

}
