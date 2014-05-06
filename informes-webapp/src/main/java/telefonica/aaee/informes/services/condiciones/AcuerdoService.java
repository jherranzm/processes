package telefonica.aaee.informes.services.condiciones;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

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
public class AcuerdoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String ACUERDO_FIND_BY_NOMBRE = "Acuerdo.findByNombre";
	private static final String ACUERDO_FIND_ALL = "Acuerdo.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(unitName = "JPAInformesWebApp")
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
        
        logger.info(Constants.SEP_V + "Número de Acuerdos:[" + this.findAll().size() + "]" + Constants.SEP_V);
	
        List<Acuerdo> acuerdos = this.findAll();
		boolean ret = this.applyCondiciones(acuerdos.get(0).getAcuerdo());

        logger.info(Constants.SEP_V + "Aplicado:[" + ret + "]" + Constants.SEP_V);
        
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

	
	@Transactional(value = "JPAInformesWebApp")
	public boolean applyCondiciones(String elAcuerdo){
		
		boolean ret = false;
		
		logger.info("Inicio del Procedimiento...");
		logger.info("Aplicar condiciones al acuerdo: ["+ elAcuerdo +"]");
		
//		StoredProcedureQuery spq = em.createStoredProcedureQuery("977r.977r_SP_APPLY_COND_ALL");
//		
//		spq.registerStoredProcedureParameter("elAcuerdo", String.class, ParameterMode.IN);
//		
//		spq.setParameter("elAcuerdo", elAcuerdo);
//		
//		ret = spq.execute();
		
		Query query = em.createNativeQuery("{call 977r.977r_SP_APPLY_COND_ALL (?)}")
					.setParameter(1, elAcuerdo);
		
		@SuppressWarnings("unchecked")
		List<String> res = (List<String>)query.getResultList();
		for(String r : res){
			logger.info("Resultados: " + r);
		}
		
		logger.info("...fin del Procedimiento!");
		
		return ret;
		
	}

	@Transactional
	public boolean applyBusinessRulesAplicarPlantaPreciosEspeciales(String elAcuerdo){
		
		boolean ret = false;
		
		logger.info("Inicio del Procedimiento...");
		logger.info("Aplicar PlantaPreciosEspeciales al acuerdo: ["+ elAcuerdo +"]");
		
		StoredProcedureQuery spq = em.createStoredProcedureQuery("977r.977r_regneg_AplicarPlantaPreciosEspeciales");
		
		spq.registerStoredProcedureParameter("elAcuerdo", String.class, ParameterMode.IN);
		
		spq.setParameter("elAcuerdo", elAcuerdo);
		
		ret = spq.execute();
		
		logger.info("...fin del Procedimiento!");
		
		return ret;
		
	}

	@Transactional
	public boolean applyBusinessRulesLineaSoporteVPNIPSinCoste(String elAcuerdo){
		
		boolean ret = false;
		
		logger.info("Inicio del Procedimiento...");
		logger.info("Aplicar LineaSoporteVPNIPSinCoste al acuerdo: ["+ elAcuerdo +"]");
		
		StoredProcedureQuery spq = em.createStoredProcedureQuery("977r.977r_regneg_LineaSoporteVPNIP_SinCoste");
		
		spq.registerStoredProcedureParameter("elAcuerdo", String.class, ParameterMode.IN);
		
		spq.setParameter("elAcuerdo", elAcuerdo);
		
		ret = spq.execute();
		
		logger.info("...fin del Procedimiento!");
		
		return ret;
		
	}
	
	//CompensacionAumentoCuotaLineaBOE
	@Transactional
	public boolean applyBusinessRulesCompensacionAumentoCuotaLineaBOE(String elAcuerdo){
		
		boolean ret = false;
		
		logger.info("Inicio del Procedimiento...");
		logger.info("Aplicar CompensacionAumentoCuotaLineaBOE al acuerdo: ["+ elAcuerdo +"]");
		
		StoredProcedureQuery spq = em.createStoredProcedureQuery("977r.977r_regneg_CompensacionAumentoCuotaLineaBOE");
		
		spq.registerStoredProcedureParameter("elAcuerdo", String.class, ParameterMode.IN);
		
		spq.setParameter("elAcuerdo", elAcuerdo);
		
		ret = spq.execute();
		
		logger.info("...fin del Procedimiento!");
		
		return ret;
		
	}
}
