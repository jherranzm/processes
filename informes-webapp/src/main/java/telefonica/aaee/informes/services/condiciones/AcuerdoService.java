package telefonica.aaee.informes.services.condiciones;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.helpers.Constants;
import telefonica.aaee.informes.model.condiciones.Acuerdo;

@Repository
@Transactional
public class AcuerdoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String ACUERDO_FIND_BY_NOMBRE = "Acuerdo.findByNombre";
	private static final String ACUERDO_FIND_ALL = "Acuerdo.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext
	private EntityManager em;
	
	private JpaRepository<Acuerdo, Long> repo;
	

	public AcuerdoService() {
	}
	
	
	public AcuerdoService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Acuerdo, Long> consultaEntityInfo = new JpaMetamodelEntityInformation<Acuerdo, Long>(Acuerdo.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Acuerdo, Long>(consultaEntityInfo, em);
        
        logger.info(Constants.SEP_V + "Número de Acuerdo:[" + repo.findAll().size() + "]" + Constants.SEP_V);

        logger.info(Constants.SEP_V + "Número de Acuerdo por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constants.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Acuerdo> findAll() {
		
		List<Acuerdo> lista = null;
		
		Query query = em.createNamedQuery(ACUERDO_FIND_ALL);
	    lista = (List<Acuerdo>)query.getResultList();
		
		return lista;
	}

	public Acuerdo findById(Long id) {
		return em.find(Acuerdo.class, id);
	}


	@SuppressWarnings("unchecked")
	public List<Acuerdo> findByNombre(String nombre) {
		List<Acuerdo> lista = null;
		
		Query query = em.createNamedQuery(ACUERDO_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
	    lista = (List<Acuerdo>)query.getResultList();
	    
	    return lista ;
	}

}
