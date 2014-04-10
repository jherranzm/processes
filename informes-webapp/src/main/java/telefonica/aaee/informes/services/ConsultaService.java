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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.exceptions.ConsultaNotFoundException;
import telefonica.aaee.informes.model.Consulta;

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
	
	private JpaRepository<Consulta, Long> repo;
	

//	@PersistenceContext
//	public void setEm(EntityManager em) {
//		this.em = em;
//	}


	public ConsultaService() {
	}
	
	
	public ConsultaService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Consulta, Long> consultaEntityInfo = new JpaMetamodelEntityInformation<Consulta, Long>(Consulta.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Consulta, Long>(consultaEntityInfo, em);
        
        logger.info("\n\n\n");
        logger.info("Número de consultas:" + repo.findAll().size());
        logger.info("\n\n\n");

        logger.info("\n\n\n");
        logger.info("Número de consultas:" + repo.findAll(new PageRequest(0, 5)).getNumberOfElements());
        logger.info("\n\n\n");
	
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
	

	public Consulta findById(Long id) {
		return em.find(Consulta.class, id);
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
		Consulta consulta = em.find(Consulta.class, new Long(modificada.getId()));

		if (consulta == null)
			throw new ConsultaNotFoundException();

		
		logger.info("modificada.getNombre(): " + modificada.getNombre());
		logger.info("modificada.getDefinicion(): " + modificada.getDefinicion());
		consulta.setNombre(modificada.getNombre());
		consulta.setDefinicion(modificada.getDefinicion());
		
		logger.info("consulta.getNombre(): " + consulta.getNombre());
		logger.info("consulta.getDefinicion(): " + consulta.getDefinicion());
		
		consulta = em.merge(consulta);
		em.flush();
		return consulta;
	}


	@SuppressWarnings("unchecked")
	public List<Consulta> findByNombre(String nombre) {
		List<Consulta> lista = null;
		
		Query query = em.createNamedQuery(CONSULTA_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Consulta>)query.getResultList();
	    
	    return lista ;
	}


	public Consulta create(Consulta nuevaConsulta) {
		
		logger.info("nuevaConsulta.getNombre(): " + nuevaConsulta.getNombre());
		logger.info("nuevaConsulta.getDefinicion(): " + nuevaConsulta.getDefinicion());
		
		Consulta consulta = new Consulta.Builder()
				.nombre(nuevaConsulta.getNombre())
				.definicion(nuevaConsulta.getDefinicion())
				.build();
		
		logger.info("consulta.getNombre(): " + consulta.getNombre());
		logger.info("consulta.getDefinicion(): " + consulta.getDefinicion());
		
		logger.info("Guardamos la consulta...");
		em.persist(consulta);
		em.flush();
		logger.info("consulta guardada con ID " + consulta.getId());
		
		
		return consulta;
	}

}
