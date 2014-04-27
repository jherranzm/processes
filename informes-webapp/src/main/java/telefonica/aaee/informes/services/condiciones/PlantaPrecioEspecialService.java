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

import telefonica.aaee.informes.exceptions.PlantaPrecioEspecialNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.PlantaPrecioEspecial;

@Repository
public class PlantaPrecioEspecialService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String PPE_FIND_ALL = "PPE.findAll";
	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<PlantaPrecioEspecial, Long> repo;
	

	public PlantaPrecioEspecialService() {
	}
	
	
	public PlantaPrecioEspecialService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<PlantaPrecioEspecial, Long> PlantaPrecioEspecialEntityInfo = new JpaMetamodelEntityInformation<PlantaPrecioEspecial, Long>(PlantaPrecioEspecial.class, em.getMetamodel());
        repo = new SimpleJpaRepository<PlantaPrecioEspecial, Long>(PlantaPrecioEspecialEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de PlantaPrecioEspecial:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de PlantaPrecioEspecial por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<PlantaPrecioEspecial> findAll() {
		
		List<PlantaPrecioEspecial> lista = null;
		
		Query query = em.createNamedQuery(PPE_FIND_ALL);
	    lista = (List<PlantaPrecioEspecial>)query.getResultList();
		
		return lista;
	}

	public Iterable<PlantaPrecioEspecial> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<PlantaPrecioEspecial> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public PlantaPrecioEspecial findById(Long id) {
		return 
				//em.find(PlantaPrecioEspecial.class, id);
				repo.findOne(id);
	}


	@Transactional
	public PlantaPrecioEspecial update(PlantaPrecioEspecial mod) throws PlantaPrecioEspecialNotFoundException {
		PlantaPrecioEspecial cfOriginal = 
				//em.find(PlantaPrecioEspecial.class, new Long(mod.getId()));
				repo.findOne(mod.getId());

		if (cfOriginal == null)
			throw new PlantaPrecioEspecialNotFoundException();
		
		cfOriginal.setImporteAcuerdo(mod.getImporteAcuerdo());
		cfOriginal.setPrecioEspecial(mod.getPrecioEspecial());
		cfOriginal.setTipoPrecioEspecial(mod.getTipoPrecioEspecial());
		
		PlantaPrecioEspecial cfResult = repo.saveAndFlush(cfOriginal);
		logger.info("PlantaPrecioEspecial " + cfResult.toString());
		return cfResult;
	}


	@Transactional
	public PlantaPrecioEspecial create(PlantaPrecioEspecial nuevoCf) {
		
		
		logger.info("Guardamos el PlantaPrecioEspecial...");
//		em.persist(nuevoCf);
//		em.flush();
		PlantaPrecioEspecial ret = repo.saveAndFlush(nuevoCf);
		logger.info("PlantaPrecioEspecial guardado con ID " + ret.getId());
		
		
		return ret;
	}

}
