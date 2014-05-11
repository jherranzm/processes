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

import telefonica.aaee.segmentacion.exceptions.SectorNotFoundException;
import telefonica.aaee.segmentacion.helpers.Constants;
import telefonica.aaee.segmentacion.model.Sector;


@Repository
public class SectorService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String SECTOR_FIND_BY_NOMBRE = "Sector.findByNombre";
	private static final String SECTOR_FIND_BY_CODIGO = "Sector.findByCodigo";
	private static final String SECTOR_FIND_ALL = "Sector.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPASegmentacionApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Sector, Long> repo;
	

	public SectorService() {
	}
	
	
	public SectorService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Sector, Long> entityInfo = new JpaMetamodelEntityInformation<Sector, Long>(Sector.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Sector, Long>(entityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Sectors:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Sector por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Sector> findAll() {
		
		List<Sector> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_ALL);
	    lista = (List<Sector>)query.getResultList();
		
		return lista;
	}

	public Iterable<Sector> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Sector findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Sector update(Sector modificada) throws SectorNotFoundException {
		Sector item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new SectorNotFoundException();

		Sector result = repo.saveAndFlush(modificada);
		logger.info("Modificado : " + result.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<Sector> findByNombre(String nombre) {
		List<Sector> lista = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Sector>)query.getResultList();
	    
	    return lista ;
	}

	public Sector findByCodigo(String cod) {
		Sector sector = null;
		
		Query query = em.createNamedQuery(SECTOR_FIND_BY_CODIGO);
		query.setParameter("cod", cod);
		try {
			sector = (Sector) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No se ha encontrado el Sector con Código " + cod);
		}
	    
	    return sector ;
	}



	@Transactional
	public Sector create(Sector nuevo) {
		
		logger.info("Guardamos el Sector...");
		Sector result = repo.saveAndFlush(nuevo);
		logger.info("Sector guardado con ID " + result.getId());
		
		return result;
	}
	
	
	public List<Sector> saveAll(Set<Sector> oficinas){
		return repo.save(oficinas);
	}


	
}
