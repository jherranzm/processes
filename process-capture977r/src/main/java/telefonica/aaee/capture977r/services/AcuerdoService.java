package telefonica.aaee.capture977r.services;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
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

import telefonica.aaee.capture977r.exceptions.AcuerdoNotFoundException;
import telefonica.aaee.capture977r.model.Acuerdo;
import telefonica.aaee.capture977r.util.Constantes;


@Repository
public class AcuerdoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String ACUERDO_FIND_BY_NOMBRE = "Acuerdo.findByNombre";
	private static final String ACUERDO_FIND_ALL = "Acuerdo.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPA977rApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Acuerdo, Long> repo;
	

	public AcuerdoService() {
	}
	
	
	public AcuerdoService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Acuerdo, Long> entityInfo = new JpaMetamodelEntityInformation<Acuerdo, Long>(Acuerdo.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Acuerdo, Long>(entityInfo, em);
        
        logger.info(Constantes.SEP_V + "Número de Acuerdos:[" + repo.findAll().size() + "]" + Constantes.SEP_V);

        logger.info(Constantes.SEP_V + "Número de Acuerdo por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constantes.SEP_V);
	
        logger.info(Constantes.SEP_V + "Número de Acuerdo por Página:[" + this.findByNombre("LHOSP_2012_1T").getAcuerdo() + "]" + Constantes.SEP_V);
	}
	
	@SuppressWarnings("unchecked")
	public List<Acuerdo> findAll() {
		
		List<Acuerdo> lista = null;
		
		Query query = em.createNamedQuery(ACUERDO_FIND_ALL);
	    lista = (List<Acuerdo>)query.getResultList();
		
		return lista;
	}

	public Iterable<Acuerdo> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Acuerdo findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Acuerdo update(Acuerdo modificada) throws AcuerdoNotFoundException {
		Acuerdo item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new AcuerdoNotFoundException();

		Acuerdo result = repo.saveAndFlush(modificada);
		logger.info("Modificado : " + result.toString());
		return result;
	}


	public Acuerdo findByNombre(String nombre) {
		Acuerdo acuerdo = null;
		
		try {
			Query query = em.createNamedQuery(ACUERDO_FIND_BY_NOMBRE);
			query.setParameter("nom", nombre);
			acuerdo = (Acuerdo)query.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return acuerdo;
	}

	@Transactional
	public Acuerdo create(Acuerdo nuevo) {
		Acuerdo acuerdo = null;
		
		try {
			logger.info("Creamos el Acuerdo..." + nuevo.toString());
			acuerdo = repo.saveAndFlush(nuevo);
			logger.info("Acuerdo creado con ID " + acuerdo.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return acuerdo;
	}
	
	
	public List<Acuerdo> saveAll(Set<Acuerdo> oficinas){
		return repo.save(oficinas);
	}
	
	
	@Transactional
	public void executeSQLQuery(String sql){
		logger.info(sql);
		Query query = em.createNativeQuery(sql);
		logger.info("Inicio de proceso...");
		long ret = query.executeUpdate();
		logger.info(String.format("Fin de proceso! Se han tratado [%d] registros.", ret));
	}
}
