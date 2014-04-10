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

import telefonica.aaee.informes.exceptions.PestanyaNotFoundException;
import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.Pestanya;

@Repository
@Transactional
public class PestanyaService {
	
	private static final int PAGE_SIZE = 5;
	
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@PersistenceContext
	private EntityManager em;
	

	private JpaRepository<Pestanya, Long> repo;

	public PestanyaService() {
	}

	public PestanyaService(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public List<Pestanya> findAll() {
		
		List<Pestanya> lista = null;
		
		
		Query query = em.createNamedQuery("Pestanya.findAll");
	    lista = (List<Pestanya>)query.getResultList();
		
		
		return lista;
	}
	
	public Pestanya findById(Long id) {
		return em.find(Pestanya.class, id);
	}


	@SuppressWarnings("unchecked")
	public List<Pestanya> findByNombreDuplicado(String nombre, long id) {
		List<Pestanya> lista = null;
		
		Query query = em.createNamedQuery("Pestanya.findByNombreDuplicado");
		query.setParameter("nom", nombre);
		query.setParameter("id", id);
	    lista = (List<Pestanya>)query.getResultList();
	    
	    return lista ;
	}


	public Pestanya update(Pestanya modificada) throws PestanyaNotFoundException {
		Pestanya pestanya = em.find(Pestanya.class, new Long(modificada.getId()));

		if (pestanya == null)
			throw new PestanyaNotFoundException();

		
		logger.info("modificada.getNombre(): " + modificada.getNombre());
		logger.info("modificada.getRango(): " + modificada.getRango());
		logger.info("modificada.getNumFilaInicial(): " + modificada.getNumFilaInicial());
		logger.info("modificada.getConsulta(): " + modificada.getConsulta());
		
		pestanya.setNombre(modificada.getNombre());
		pestanya.setRango(modificada.getRango());
		pestanya.setNumFilaInicial(modificada.getNumFilaInicial());
		pestanya.setConsulta(modificada.getConsulta());
		
		logger.info("pestanya.getNombre(): " + pestanya.getNombre());
		logger.info("pestanya.getRango(): " + pestanya.getRango());
		logger.info("pestanya.getNumFilaInicial(): " + pestanya.getNumFilaInicial());
		logger.info("pestanya.getConsulta(): " + pestanya.getConsulta());
		
		pestanya = em.merge(pestanya);
		em.flush();
		return pestanya;
	}


	@SuppressWarnings("unchecked")
	public List<Pestanya> findByNombre(String nombre) {
		List<Pestanya> lista = null;
		
		Query query = em.createNamedQuery("Pestanya.findByNombre");
		query.setParameter("nom", nombre);
	    lista = (List<Pestanya>)query.getResultList();
	    
	    return lista ;
	}


	public Pestanya create(Pestanya nuevaPestanya) {
		
		logger.info("nuevaPestanya.getNombre(): " + nuevaPestanya.getNombre());
		logger.info("nuevaPestanya.getRango(): " + nuevaPestanya.getRango());
		logger.info("nuevaPestanya.getNumFilaInicial(): " + nuevaPestanya.getNumFilaInicial());
		logger.info("nuevaPestanya.getConsulta(): " + nuevaPestanya.getConsulta());
		
		Pestanya pestanya = new Pestanya.Builder()
				.nombre(nuevaPestanya.getNombre())
				.rango(nuevaPestanya.getRango())
				.numFilaInicial(nuevaPestanya.getNumFilaInicial())
				.consulta(nuevaPestanya.getConsulta())
				.build();
		
		logger.info("pestanya.getNombre(): " + pestanya.getNombre());
		logger.info("pestanya.getRango(): " + pestanya.getRango());
		logger.info("pestanya.getNumFilaInicial(): " + pestanya.getNumFilaInicial());
		logger.info("pestanya.getConsulta(): " + pestanya.getConsulta());
		
		logger.info("Guardamos la pestanya...");
		em.persist(pestanya);
		em.flush();
		logger.info("pestanya guardada con ID " + pestanya.getId());
		
		
		return pestanya;
	}

	public Page<Pestanya> getPage(Integer pageNumber) {
		logger.info("Vamos a generar el request...");
		PageRequest request =
	            new PageRequest(pageNumber - 1, PAGE_SIZE);
		logger.info("Tenemos el request:" + request.getPageNumber());
	        return repo.findAll(request);
	}

	@PostConstruct
    public void init() {
        JpaEntityInformation<Pestanya, Long> entityInfo = new JpaMetamodelEntityInformation<Pestanya, Long>(Pestanya.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Pestanya, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Pestanya:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Pestanya por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
}
