package telefonica.aaee.segmentacion.services;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.segmentacion.exceptions.NivelDeAtencionNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.NivelDeAtencion;


@Repository
public class NivelDeAtencionService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String NIVEL_FIND_BY_NOMBRE = "NivelDeAtencion.findByNombre";
	private static final String NIVEL_FIND_BY_CODIGO = "NivelDeAtencion.findByCodigo";
	private static final String NIVEL_FIND_ALL = "NivelDeAtencion.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<NivelDeAtencion, Long> repo;
	

	public NivelDeAtencionService() {
	}
	
	
	public NivelDeAtencionService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<NivelDeAtencion, Long> entityInfo = new JpaMetamodelEntityInformation<NivelDeAtencion, Long>(NivelDeAtencion.class, em.getMetamodel());
        repo = new SimpleJpaRepository<NivelDeAtencion, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de NivelDeAtencions:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de NivelDeAtencion por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<NivelDeAtencion> findAll() {
		
		List<NivelDeAtencion> lista = null;
		
		Query query = em.createNamedQuery(NIVEL_FIND_ALL);
	    lista = (List<NivelDeAtencion>)query.getResultList();
		
		return lista;
	}

	public Iterable<NivelDeAtencion> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public NivelDeAtencion findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public NivelDeAtencion update(NivelDeAtencion modificada) throws NivelDeAtencionNotFoundException {
		NivelDeAtencion consulta = repo.findOne(new Long(modificada.getId()));

		if (consulta == null)
			throw new NivelDeAtencionNotFoundException();

		NivelDeAtencion result = repo.saveAndFlush(modificada);
		logger.info("NivelDeAtencion actualizada : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<NivelDeAtencion> findByNombre(String nombre) {
		List<NivelDeAtencion> lista = null;
		
		Query query = em.createNamedQuery(NIVEL_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<NivelDeAtencion>)query.getResultList();
	    
	    return lista ;
	}

	public NivelDeAtencion findByCodigo(String cod) {
		NivelDeAtencion oficina = null;
		
		Query query = em.createNamedQuery(NIVEL_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			oficina = (NivelDeAtencion) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado la NivelDeAtencion con Código " + cod);
		}
	    
	    return oficina ;
	}



	@Transactional
	public NivelDeAtencion create(NivelDeAtencion nuevo) {
		
		logger.info("Incluimos la NivelDeAtencion en la tabla...");
		NivelDeAtencion result = repo.saveAndFlush(nuevo);
		logger.info("NivelDeAtencion creado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<NivelDeAtencion> saveAll(Set<NivelDeAtencion> oficinas){
		return repo.save(oficinas);
	}


	
}
