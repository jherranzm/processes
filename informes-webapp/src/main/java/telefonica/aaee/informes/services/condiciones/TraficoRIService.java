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

import telefonica.aaee.informes.exceptions.TraficoRINotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.TraficoRI;

@Repository
@Transactional
public class TraficoRIService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String TRAFICORI_FIND_ALL = "TRAFICORI.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<TraficoRI, Long> repo;
	

	public TraficoRIService() {
	}
	
	
	public TraficoRIService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<TraficoRI, Long> conceptoFacturableEntityInfo = new JpaMetamodelEntityInformation<TraficoRI, Long>(TraficoRI.class, em.getMetamodel());
        repo = new SimpleJpaRepository<TraficoRI, Long>(conceptoFacturableEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de TraficoRI:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de TraficoRI por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<TraficoRI> findAll() {
		
		List<TraficoRI> lista = null;
		
		Query query = em.createNamedQuery(TRAFICORI_FIND_ALL);
	    lista = (List<TraficoRI>)query.getResultList();
		
		return lista;
	}

	public Iterable<TraficoRI> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<TraficoRI> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	

	public TraficoRI findById(Long id) {
		return em.find(TraficoRI.class, id);
	}


	public TraficoRI update(TraficoRI mod) throws TraficoRINotFoundException {
		TraficoRI original = em.find(TraficoRI.class, new Long(mod.getId()));

		if (original == null)
			throw new TraficoRINotFoundException();
		
		original.setEstLlamada(mod.getEstLlamada());
		original.setPrecioPorMinuto(mod.getPrecioPorMinuto());
		original.setPorcentajeDescuento(mod.getPorcentajeDescuento());
		original.setPrecioEspecial(mod.getPrecioEspecial());

		
		
		TraficoRI result = em.merge(original);
		em.flush();
		logger.info("TraficoRI " + result.toString());
		return result;
	}


	public TraficoRI create(TraficoRI nuevo) {
		
		
		logger.info("Guardamos el TraficoRI...");
		em.persist(nuevo);
		em.flush();
		logger.info("TraficoRI guardado con ID " + nuevo.getId());
		
		
		return nuevo;
	}

}
