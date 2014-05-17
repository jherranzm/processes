package telefonica.aaee.dao.service;

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

import telefonica.aaee.dao.exceptions.TipoRegistroNotFoundException;
import telefonica.aaee.dao.model.TipoRegistroFactel5;
import telefonica.aaee.util.Constantes;


@Repository
public class TipoRegistroFactel5Service {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String FIND_BY_NOMBRE = "TipoRegistroFactel5.findByTipoRegistro";
	private static final String FIND_ALL = "TipoRegistroFactel5.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPACapture977rApp")
	private EntityManager em;
	
	private SimpleJpaRepository<TipoRegistroFactel5, Long> repo;
	

	public TipoRegistroFactel5Service() {
	}
	
	
	public TipoRegistroFactel5Service(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<TipoRegistroFactel5, Long> entityInfo = new JpaMetamodelEntityInformation<TipoRegistroFactel5, Long>(TipoRegistroFactel5.class, em.getMetamodel());
        repo = new SimpleJpaRepository<TipoRegistroFactel5, Long>(entityInfo, em);
        
        logger.info(Constantes.SEP_V + "Número de TipoRegistros:[" + repo.findAll().size() + "]" + Constantes.SEP_V);

        logger.info(Constantes.SEP_V + "Número de TipoRegistro por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constantes.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoRegistroFactel5> findAll() {
		
		List<TipoRegistroFactel5> lista = null;
		
		Query query = em.createNamedQuery(FIND_ALL);
	    lista = (List<TipoRegistroFactel5>)query.getResultList();
		
		return lista;
	}

	public Iterable<TipoRegistroFactel5> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public TipoRegistroFactel5 findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public TipoRegistroFactel5 update(TipoRegistroFactel5 modificada) throws TipoRegistroNotFoundException {
		TipoRegistroFactel5 item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new TipoRegistroNotFoundException();

		TipoRegistroFactel5 result = repo.saveAndFlush(modificada);
		logger.info("Modificado : " + result.toString());
		return result;
	}


	public TipoRegistroFactel5 findByNombre(String nombre) {
		TipoRegistroFactel5 acuerdo = null;
		
		try {
			Query query = em.createNamedQuery(FIND_BY_NOMBRE);
			query.setParameter("nom", nombre);
			acuerdo = (TipoRegistroFactel5)query.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return acuerdo;
	}

	
	@Transactional
	public TipoRegistroFactel5 create(TipoRegistroFactel5 nuevo) {
		TipoRegistroFactel5 acuerdo = null;
		
		try {
			logger.info("Creamos el TipoRegistro..." + nuevo.toString());
			acuerdo = repo.saveAndFlush(nuevo);
			logger.info("TipoRegistro creado con ID " + acuerdo.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return acuerdo;
	}
	
	
	public List<TipoRegistroFactel5> saveAll(Set<TipoRegistroFactel5> ficheros){
		return repo.save(ficheros);
	}
	
	
}
