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

import telefonica.aaee.informes.exceptions.ReasignacionNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.Reasignacion;

@Repository
@Transactional
public class ReasignacionService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String R_FIND_ALL = "R.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<Reasignacion, Long> repo;
	

	public ReasignacionService() {
	}
	
	
	public ReasignacionService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Reasignacion, Long> conceptoFacturableEntityInfo = new JpaMetamodelEntityInformation<Reasignacion, Long>(Reasignacion.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Reasignacion, Long>(conceptoFacturableEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Reasignacion:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Reasignacion por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Reasignacion> findAll() {
		
		List<Reasignacion> lista = null;
		
		Query query = em.createNamedQuery(R_FIND_ALL);
	    lista = (List<Reasignacion>)query.getResultList();
		
		return lista;
	}

	public Iterable<Reasignacion> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<Reasignacion> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public Reasignacion findById(Long id) {
		return em.find(Reasignacion.class, id);
	}


	public Reasignacion update(Reasignacion mod) throws ReasignacionNotFoundException {
		Reasignacion original = em.find(Reasignacion.class, new Long(mod.getId()));

		if (original == null)
			throw new ReasignacionNotFoundException();
		
		original.setCifNuevo(mod.getCifNuevo());
		original.setNombreNuevo(mod.getNombreNuevo());
		original.setCentroCoste(mod.getCentroCoste());
		original.setNumeroCuenta(mod.getNumeroCuenta());
		
		//original.setComentarios(mod.getComentarios());

		
		
		Reasignacion result = em.merge(original);
		em.flush();
		logger.info("Reasignacion " + result.toString());
		return result;
	}


	public Reasignacion create(Reasignacion nuevo) {
		
		
		logger.info("Guardamos el Reasignacion...");
		em.persist(nuevo);
		em.flush();
		logger.info("Reasignacion guardado con ID " + nuevo.getId());
		
		
		return nuevo;
	}

}
