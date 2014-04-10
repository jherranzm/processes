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

import telefonica.aaee.informes.exceptions.ReasignacionCargoNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.ReasignacionCargo;

@Repository
@Transactional
public class ReasignacionCargoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String RC_FIND_ALL = "RC.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<ReasignacionCargo, Long> repo;
	

	public ReasignacionCargoService() {
	}
	
	
	public ReasignacionCargoService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<ReasignacionCargo, Long> conceptoFacturableEntityInfo = new JpaMetamodelEntityInformation<ReasignacionCargo, Long>(ReasignacionCargo.class, em.getMetamodel());
        repo = new SimpleJpaRepository<ReasignacionCargo, Long>(conceptoFacturableEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de ReasignacionCargo:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de ReasignacionCargo por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<ReasignacionCargo> findAll() {
		
		List<ReasignacionCargo> lista = null;
		
		Query query = em.createNamedQuery(RC_FIND_ALL);
	    lista = (List<ReasignacionCargo>)query.getResultList();
		
		return lista;
	}

	public Iterable<ReasignacionCargo> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<ReasignacionCargo> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public ReasignacionCargo findById(Long id) {
		return em.find(ReasignacionCargo.class, id);
	}


	public ReasignacionCargo update(ReasignacionCargo mod) throws ReasignacionCargoNotFoundException {
		ReasignacionCargo original = em.find(ReasignacionCargo.class, new Long(mod.getId()));

		if (original == null)
			throw new ReasignacionCargoNotFoundException();
		
		original.setTipoDocReasignado(mod.getTipoDocReasignado());
		original.setCifReasignado(mod.getCifReasignado());
		original.setNombreClienteReasignado(mod.getNombreClienteReasignado());
		
		original.setComentarios(mod.getComentarios());

		
		
		ReasignacionCargo result = em.merge(original);
		em.flush();
		logger.info("ReasignacionCargo " + result.toString());
		return result;
	}


	public ReasignacionCargo create(ReasignacionCargo nuevo) {
		
		
		logger.info("Guardamos el ReasignacionCargo...");
		em.persist(nuevo);
		em.flush();
		logger.info("ReasignacionCargo guardado con ID " + nuevo.getId());
		
		
		return nuevo;
	}

}
