package telefonica.aaee.dao.service;

import java.util.List;
import java.util.Set;

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

import telefonica.aaee.dao.exceptions.AcuerdoNotFoundException;
import telefonica.aaee.dao.model.Acuerdo;
import telefonica.aaee.util.Constantes;


@Repository
public class AcuerdoService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String ACUERDO_FIND_BY_NOMBRE = "Acuerdo.findByNombre";
	private static final String ACUERDO_FIND_ALL = "Acuerdo.findAll";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(unitName = "JPACapture977rApp")
	private EntityManager em;
	
	private JpaRepository<telefonica.aaee.dao.model.Acuerdo, Long> repo;
	

	public AcuerdoService() {
	}
	
	
	public AcuerdoService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<telefonica.aaee.dao.model.Acuerdo, Long> consultaEntityInfo = new JpaMetamodelEntityInformation<Acuerdo, Long>(Acuerdo.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Acuerdo, Long>(consultaEntityInfo, em);
        
        logger.info(Constantes.SEP_V + "Número de Acuerdo:[" + repo.findAll().size() + "]" + Constantes.SEP_V);

        logger.info(Constantes.SEP_V + "Número de Acuerdo por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constantes.SEP_V);
        
        logger.info(Constantes.SEP_V + "Número de Acuerdos:[" + this.findAll().size() + "]" + Constantes.SEP_V);
	
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
		return em.find(Acuerdo.class, id);
	}
	
	


	public Acuerdo findByNombre(String nombre) {
		Acuerdo acuerdo = null;
		
		Query query = em.createNamedQuery(ACUERDO_FIND_BY_NOMBRE);
		query.setParameter("nom", nombre);
		acuerdo = (Acuerdo)query.getSingleResult();
	    
	    return acuerdo ;
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
