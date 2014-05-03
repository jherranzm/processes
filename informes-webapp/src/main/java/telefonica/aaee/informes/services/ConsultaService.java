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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.exceptions.ConsultaNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.Consulta;
import telefonica.aaee.informes.services.specifications.ConsultaSpecifications;

@Repository
@Transactional
public class ConsultaService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String CONSULTA_FIND_BY_NOMBRE = "Consulta.findByNombre";
	private static final String CONSULTA_FIND_BY_NOMBRE_DUPLICADO = "Consulta.findByNombreDuplicado";
	private static final String CONSULTA_FIND_ALL = "Consulta.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private SimpleJpaRepository<Consulta, Long> repo;
	

	public ConsultaService() {
	}
	
	
	public ConsultaService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Consulta, Long> consultaEntityInfo = new JpaMetamodelEntityInformation<Consulta, Long>(Consulta.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Consulta, Long>(consultaEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Consulta:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Consulta por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
        logger.info(Constants.SEP_V + "Número de Consultas con CCF:[" + repo.findAll(
        		ConsultaSpecifications.searchByNombre("CCF")
        		, new PageRequest(0, 1000)
        		).getNumberOfElements() + "]" + Constants.SEP_V);
	}
	
	@SuppressWarnings("unchecked")
	public List<Consulta> findAll() {
		
		List<Consulta> lista = null;
		
		Query query = em.createNamedQuery(CONSULTA_FIND_ALL);
	    lista = (List<Consulta>)query.getResultList();
		
		return lista;
	}

	public Iterable<Consulta> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	public Page<Consulta> getPage(Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}
	
	public Page<Consulta> getPage(String search, Integer pageNumber){
		logger.info("Vamos a generar el request...");
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
		return repo.findAll(ConsultaSpecifications.searchByNombre(search), request);
	}

	public Page<Consulta> getPage(String search){
		logger.info("Vamos a generar el request...");
		PageRequest request = new PageRequest(0, 10000);
		logger.info("Tenemos el request:" + request.getPageNumber());
		return repo.findAll(ConsultaSpecifications.searchByNombre(search), request);
	}
	
	public Consulta findById(Long id) {
		return repo.findOne(id);
	}


	@SuppressWarnings("unchecked")
	public List<Consulta> findByNombreDuplicado(String nombre, long id) {
		List<Consulta> lista = null;
		
		Query query = em.createNamedQuery(CONSULTA_FIND_BY_NOMBRE_DUPLICADO);
		query.setParameter("nom", nombre);
		query.setParameter("id", id);
	    lista = (List<Consulta>)query.getResultList();
	    
	    return lista ;
	}


	public Consulta update(Consulta modificada) throws ConsultaNotFoundException {
		Consulta consulta = repo.findOne(new Long(modificada.getId()));

		if (consulta == null)
			throw new ConsultaNotFoundException();

		Consulta result = repo.saveAndFlush(modificada);
		logger.info("Consulta " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Consulta> findByNombre(String nombre) {
		List<Consulta> lista = null;
		
		Query query = em.createNamedQuery(CONSULTA_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Consulta>)query.getResultList();
	    
	    return lista ;
	}


	public Consulta create(Consulta nuevo) {
		
		logger.info("Guardamos el ConceptoFacturable...");
		Consulta result = repo.saveAndFlush(nuevo);
		logger.info("ConceptoFacturable guardado con ID " + result.getId());
		
		return result;
	}


	
}
