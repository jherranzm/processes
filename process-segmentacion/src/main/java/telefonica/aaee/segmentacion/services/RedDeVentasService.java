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

import telefonica.aaee.segmentacion.exceptions.RedDeVentasNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.RedDeVentas;


@Repository
public class RedDeVentasService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String RDV_FIND_BY_MATRICULA = "RedDeVentas.findByMatricula";
	private static final String RDV_FIND_BY_NOMBRE = "RedDeVentas.findByNombre";
	private static final String SECTOR_FIND_ALL = "RedDeVentas.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<RedDeVentas, Long> repo;
	

	public RedDeVentasService() {
	}
	
	
	public RedDeVentasService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<RedDeVentas, Long> entityInfo = new JpaMetamodelEntityInformation<RedDeVentas, Long>(RedDeVentas.class, em.getMetamodel());
        repo = new SimpleJpaRepository<RedDeVentas, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de RedDeVentass:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de RedDeVentas por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<RedDeVentas> findAll() {
		
		List<RedDeVentas> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_ALL);
	    lista = (List<RedDeVentas>)query.getResultList();
		
		return lista;
	}

	public Iterable<RedDeVentas> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public RedDeVentas findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public RedDeVentas update(RedDeVentas modificada) throws RedDeVentasNotFoundException {
		RedDeVentas item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new RedDeVentasNotFoundException();

		RedDeVentas result = repo.saveAndFlush(modificada);
		logger.info("RedDeVentas actualizado : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<RedDeVentas> findByNombre(String nombre) {
		List<RedDeVentas> lista = null;
		
		
		Query query = em.createNamedQuery(RDV_FIND_BY_NOMBRE);
		query.setParameter("nom", "%" + nombre.toLowerCase() + "%");
		
	    lista = (List<RedDeVentas>)query.getResultList();
	    
	    return lista ;
	}

	public RedDeVentas findByMatricula(String matricula) {
		RedDeVentas elem = null;
		
		Query query = em.createNamedQuery(RDV_FIND_BY_MATRICULA);
		query.setParameter("mat", matricula);
		try {
			elem = (RedDeVentas) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado el RedDeVentas con Matrícula como " + matricula);
		}
	    
	    return elem ;
	}



	@Transactional
	public RedDeVentas create(RedDeVentas nuevo) {
		
		logger.info("Incluimos RedDeVentas en la tabla...");
		RedDeVentas result = repo.saveAndFlush(nuevo);
		logger.info("RedDeVentas creado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<RedDeVentas> saveAll(Set<RedDeVentas> oficinas){
		return repo.save(oficinas);
	}


	
}
