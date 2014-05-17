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

import telefonica.aaee.capture977r.exceptions.FicheroNotFoundException;
import telefonica.aaee.capture977r.model.Fichero;
import telefonica.aaee.capture977r.util.Constantes;


@Repository
public class FicheroService {
	
	private static final int PAGE_SIZE = 5;
	
	private static final String FICHERO_FIND_BY_NOMBRE = "Fichero.findByNombre";
	private static final String FICHERO_FIND_ALL = "Fichero.findAll";
	private static final String FICHERO_FIND_BY_NOMBRE_FECHA_FACTURA = "Fichero.findByNombreFechaFactura";
	private static final String FICHERO_FIND_BY_NOMBRE_FECHA_FACTURA_CIF = "Fichero.findByNombreFechaFacturaCif";

	protected final Log logger = LogFactory.getLog(getClass());
		
	@PersistenceContext(name="JPA977rApp")
	private EntityManager em;
	
	private SimpleJpaRepository<Fichero, Long> repo;
	

	public FicheroService() {
	}
	
	
	public FicheroService(EntityManager em) {
		this.em = em;
	}
	
	
	@PostConstruct
    public void init() {
        JpaEntityInformation<Fichero, Long> entityInfo = new JpaMetamodelEntityInformation<Fichero, Long>(Fichero.class, em.getMetamodel());
        repo = new SimpleJpaRepository<Fichero, Long>(entityInfo, em);
        
        logger.info(Constantes.SEP_V + "Número de Ficheros:[" + repo.findAll().size() + "]" + Constantes.SEP_V);

        logger.info(Constantes.SEP_V + "Número de Fichero por Página:[" + repo.findAll(new PageRequest(0, PAGE_SIZE)).getNumberOfElements() + "]" + Constantes.SEP_V);
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Fichero> findAll() {
		
		List<Fichero> lista = null;
		
		Query query = em.createNamedQuery(FICHERO_FIND_ALL);
	    lista = (List<Fichero>)query.getResultList();
		
		return lista;
	}

	public Iterable<Fichero> findAll(PageRequest page) {
		return repo.findAll(page);
	}
	
	
	public Fichero findById(Long id) {
		return repo.findOne(id);
	}




	@Transactional
	public Fichero update(Fichero modificada) throws FicheroNotFoundException {
		Fichero item = repo.findOne(new Long(modificada.getId()));

		if (item == null)
			throw new FicheroNotFoundException();

		Fichero result = repo.saveAndFlush(modificada);
		logger.info("Modificado : " + result.toString());
		return result;
	}


	public Fichero findByNombre(String nombre) {
		Fichero acuerdo = null;
		
		try {
			Query query = em.createNamedQuery(FICHERO_FIND_BY_NOMBRE);
			query.setParameter("nom", nombre);
			acuerdo = (Fichero)query.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return acuerdo;
	}

	
	@SuppressWarnings("unchecked")
	public Fichero findByNombreFechaFactura(String nombre, String fechaFactura) {
		
		Fichero fichero = null;
		
		try {
			Query query = em.createNamedQuery(FICHERO_FIND_BY_NOMBRE_FECHA_FACTURA);
			query.setParameter("elfichero", nombre);
			query.setParameter("laFecha", fechaFactura);
			fichero = (Fichero)query.getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fichero;
	}

	@SuppressWarnings("unchecked")
	public Fichero findByNombreFechaFacturaCif(String nombre, String fechaFactura, String cif) {
		
		Fichero fichero = null;
		
		try {
			Query query = em.createNamedQuery(FICHERO_FIND_BY_NOMBRE_FECHA_FACTURA_CIF);
			query.setParameter("elfichero", nombre);
			query.setParameter("laFecha", fechaFactura);
			query.setParameter("elCif", cif);
			fichero = (Fichero)query.getSingleResult();
		} catch(javax.persistence.NoResultException e){
			logger.info(String.format("El fichero [%s] de fecha [%s] del CIF [%s] NO existe!",nombre, fechaFactura, cif));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fichero;
	}

	@Transactional
	public Fichero create(Fichero nuevo) {
		Fichero acuerdo = null;
		
		try {
			logger.info("Creamos el Fichero..." + nuevo.toString());
			acuerdo = repo.saveAndFlush(nuevo);
			logger.info("Fichero creado con ID " + acuerdo.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return acuerdo;
	}
	
	
	public List<Fichero> saveAll(Set<Fichero> ficheros){
		return repo.save(ficheros);
	}
	
	
}
