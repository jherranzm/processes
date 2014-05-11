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

import telefonica.aaee.segmentacion.exceptions.SubSectorNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.SubSector;


@Repository
public class SubSectorService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String SECTOR_FIND_BY_NOMBRE = "SubSector.findByNombre";
	private static final String SECTOR_FIND_BY_CODIGO = "SubSector.findByCodigo";
	private static final String SECTOR_FIND_ALL = "SubSector.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<SubSector, Long> repo;
	

	public SubSectorService() {
	}
	
	
	public SubSectorService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<SubSector, Long> entityInfo = new JpaMetamodelEntityInformation<SubSector, Long>(SubSector.class, em.getMetamodel());
        repo = new SimpleJpaRepository<SubSector, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de SubSectors:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de SubSector por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<SubSector> findAll() {
		
		List<SubSector> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_ALL);
	    lista = (List<SubSector>)query.getResultList();
		
		return lista;
	}

	public Iterable<SubSector> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public SubSector findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public SubSector update(SubSector modificada) throws SubSectorNotFoundException {
		SubSector item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new SubSectorNotFoundException();

		SubSector result = repo.saveAndFlush(modificada);
		logger.info("Modificado : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<SubSector> findByNombre(String nombre) {
		List<SubSector> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<SubSector>)query.getResultList();
	    
	    return lista ;
	}

	public SubSector findByCodigo(String cod) {
		SubSector sector = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			sector = (SubSector) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado el SubSector con Código " + cod);
		}
	    
	    return sector ;
	}



	@Transactional
	public SubSector create(SubSector nuevo) {
		
		logger.info("Guardamos el SubSector...");
		SubSector result = repo.saveAndFlush(nuevo);
		logger.info("SubSector guardado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<SubSector> saveAll(Set<SubSector> oficinas){
		return repo.save(oficinas);
	}


	
}
