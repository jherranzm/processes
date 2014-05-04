package telefonica.aaee.informes.services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.exceptions.InformeNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.Informe;
import telefonica.aaee.informes.model.InformePestanya;
import telefonica.aaee.informes.services.specifications.InformeSpecifications;


@Repository
@Transactional
public class InformeService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String INFORME_FIND_ALL = "Informe.findAll";
	private static final String INFORME_FIND_BY_NOMBRE_DUPLICADO = "Informe.findByNombreDuplicado";
	private static final String INFORME_FIND_BY_NOMBRE = "Informe.findByNombre";

	protected final Log logger = LogFactory.getLog(getClass());
	
	@PersistenceContext
	private EntityManager em;
	
	private SimpleJpaRepository<Informe, Long> repo;
	

	public InformeService() {
	}

	public InformeService(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	
	public List<Informe> findAll() {
		
		List<Informe> lista = null;
		
		Query query = em.createNamedQuery(INFORME_FIND_ALL);
	    lista = (List<Informe>)query.getResultList();
	    
	    /**
	     * Localizando las pestanyas, se instancian
	     */
	    for(Informe informe : lista){
	    	informe.getPestanyes().size();
	    }
		
		return lista;
	}

	public Iterable<Informe> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Informe findById(Long id) {
		return em.find(Informe.class, id);
	}


	@SuppressWarnings("unchecked")
	public List<Informe> findByNombreDuplicado(String nombre, long id) {
		List<Informe> lista = null;
		
		Query query = em.createNamedQuery(INFORME_FIND_BY_NOMBRE_DUPLICADO);
		query.setParameter("nom", nombre);
		query.setParameter("id", id);
	    lista = (List<Informe>)query.getResultList();
	    
	    return lista ;
	}


	public Informe update(Informe modificada) throws InformeNotFoundException {
		Informe informe = em.find(Informe.class, new Long(modificada.getId()));
		
		if (informe == null)
			throw new InformeNotFoundException();

		for(InformePestanya ip : informe.getPestanyes()){
			logger.info(ip.toString());
		}

		logger.info("informe.original: " + informe.toString());
		logger.info("informe.modificado: " + modificada.toString());

		informe.setNombre(modificada.getNombre());
		informe.setPestanyes(modificada.getPestanyes());
		
		logger.info("Guardamos el informe la tabla con merge...");
		informe = em.merge(informe);
		
		logger.info("Nos aseguramos de que se apliquen los cambios...");
		em.flush();
		
		logger.info("Limpiamos el entityManager...");
		em.clear();
		
		// Recuperamos de la tabla
		logger.info("Recuperamos el informe que hay en la tabla...");
		informe = em.find(Informe.class, new Long(modificada.getId()));
		return informe;
	}


	@SuppressWarnings("unchecked")
	public List<Informe> findByNombre(String nombre) {
		List<Informe> lista = null;
		
		Query query = em.createNamedQuery(INFORME_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Informe>)query.getResultList();
	    
	    return lista ;
	}

	public Informe create(Informe nuevoInforme) {
		
		logger.info("nuevoInforme.getNombre(): " + nuevoInforme.getNombre());
		
		Informe informe = new Informe();
		informe.setNombre(nuevoInforme.getNombre());
		
		for(InformePestanya ip : nuevoInforme.getPestanyes()){
			InformePestanya newIp = new InformePestanya();
			newIp.setInforme(informe);
			newIp.setPestanya(ip.getPestanya());
			newIp.setOrden(ip.getOrden());
			informe.getPestanyes().add(newIp);
			logger.info("informe.getNombre(): " + informe.toString());
		}
		
		logger.info("informe.getNombre(): " + informe.getNombre());
		
		logger.info("Guardamos el informe...");
		em.persist(informe);
		em.flush();
		logger.info("informe guardado con ID " + informe.getId());
		
		
		return informe;
	}

	public Informe delete(Long informeId) throws InformeNotFoundException{
		
		Informe informe = em.find( Informe.class, informeId );
		
		if( informe == null){
			throw new InformeNotFoundException();
		}
		
		em.remove(informe);
		em.flush();
		
		return informe;
	}
	
	
	public Page<Informe> getPage(Integer pageNumber) {
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	
	public Page<Informe> getPage(String search, Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
		return repo.findAll(InformeSpecifications.searchByNombre(search), request);
	}

	public Page<Informe> getPage(String search){
		logger.info("Vamos a generar el request...");
		PageRequest request = new PageRequest(0, 10000);
		logger.info("Tenemos el request:" + request.getPageNumber());
		return repo.findAll(InformeSpecifications.searchByNombre(search), request);
	}
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Informe, Long> entityInfo = new JpaMetamodelEntityInformation<Informe, Long>(Informe.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Informe, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Informe:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Informe por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
}
