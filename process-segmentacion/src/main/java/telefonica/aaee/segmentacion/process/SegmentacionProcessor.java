package telefonica.aaee.segmentacion.process;


import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telefonica.aaee.segmentacion.model.Cliente;
import telefonica.aaee.segmentacion.model.Gerencia;
import telefonica.aaee.segmentacion.model.Oficina;
import telefonica.aaee.segmentacion.model.RedDeVentas;
import telefonica.aaee.segmentacion.model.Sector;
import telefonica.aaee.segmentacion.model.Segmento;
import telefonica.aaee.segmentacion.model.SubSector;
import telefonica.aaee.segmentacion.model.Territorio;
import telefonica.aaee.segmentacion.services.ClienteService;
import telefonica.aaee.segmentacion.services.OficinaService;
import telefonica.aaee.segmentacion.services.RedDeVentasService;
import telefonica.aaee.segmentacion.services.SectorService;
import telefonica.aaee.segmentacion.services.SubSectorService;
import telefonica.aaee.segmentacion.services.TerritorioService;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.RuntimeIOException;
import com.healthmarketscience.jackcess.Table;

@Service
public class SegmentacionProcessor {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String NIVEL_DE_ATENCION = "Nivel de Atencion";
	private static final String NOM_GERENCIA = "Nom_Gerencia";
	private static final String COD_GERENCIA = "Cod_Gerencia";
	
	private static final String NOM_SECTOR = "Nom_Sector";
	private static final String COD_SECTOR = "Cod_Sector";
	
	private static final String NOM_SUB_SECTOR = "Nom_SubSector";
	private static final String COD_SUB_SECTOR = "Cod_SubSector";
	
	private static final String NOM_TERRITORIO = "Nom_Territorio";
	private static final String COD_TERRITORIO = "Cod_Territorio";
	
	private static final String NOM_GERENTE = "Nom_Gerente";
	private static final String NOM_J_AREA = "Nom_JArea";
	private static final String NOM_J_VENTAS = "Nom_JVentas";
	private static final String NOM_DESARROLLADOR = "Nom_Desarrollador";
	private static final String NOM_VENDEDOR = "Nom_Vendedor";
	
	private static final String MAT_GERENTE = "Mat_Gerente";
	private static final String MAT_J_AREA = "Mat_JArea";
	private static final String MAT_J_VENTAS = "Mat_JVentas";
	private static final String MAT_DESARROLLADOR = "Mat_Desarrollador";
	private static final String MAT_VENDEDOR = "Mat_Vendedor";
	
	private static final String SUBSEGMENTO = "Subsegmento";
	private static final String SEGMENTO = "Segmento";
	
	private static final String NOM_OFICINA = "Nom_Oficina";
	private static final String COD_OFICINA = "Cod_Oficina";
	
	private static final String ERROR_CIF_ALREADY_IN_LIST = "ERROR: El tipoDoc+Cif [%s] ya estaba en la lista!";
	private static final String ERROR_CUC_ALREADY_IN_LIST = "ERROR: El CUC [%s] ya estaba en la lista";
	
	private Set<Sector> sectores = new HashSet<Sector>();
	private Set<SubSector> subSectores = new HashSet<SubSector>();
	private Set<Territorio> territorios = new HashSet<Territorio>();
	private Set<Oficina> oficinas = new HashSet<Oficina>();
	private Set<RedDeVentas> comerciales = new HashSet<RedDeVentas>();
	private Set<Gerencia> gerencias = new HashSet<Gerencia>();
	private Set<Segmento> segmentos = new HashSet<Segmento>();

	private Map<String, Cliente> clientes = new HashMap<String, Cliente>();
	private Map<String, Cliente> duplicados = new HashMap<String, Cliente>();

	private Map<String, Cliente> cifs = new HashMap<String, Cliente>();
	private Map<String, Cliente> cifsDuplicados = new HashMap<String, Cliente>();
	
	private String dir = "/Users/jherranzm/dev/testFiles/";
	
	@Autowired
	private OficinaService oficinaService;

	@Autowired
	private SectorService sectorService;

	@Autowired
	private SubSectorService subSectorService;

	@Autowired
	private RedDeVentasService redDeVentasService;

	@Autowired
	private TerritorioService territorioService;

	@Autowired
	private ClienteService clienteService;

	public void execute() {

		String[] mdbs = { 
				
				"NP.mdb",
				"Pymes.mdb",
				"Operadoras.mdb",
				"Empresas.mdb"
				};

		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

		try {
			
			for(String mdb : mdbs){
				Database db = DatabaseBuilder.open(new File(this.dir + mdb));
				
				listTables(db);

				for (String tabla : db.getTableNames()) {
					if(tabla.contains("Clientes")){
						procesaTabla(db, tabla);
					}
				}
			}
			
			
			actualizarOficinas();
			actualizarSectores();
			actualizarSubSectores();
			actualizarRedDeVentas();
			actualizarTerritorios();
			
			actualizarClientes();

//			showInfoGerencias();
//
//			showInfoSectores();
//
//			showInfoSubSectores();
//
//			showInfoTerritorios();
//
//			showInfoOficinas();
//
//			showInfoRedDeVentas();
//
//			showInfoClientes();
			
//			showInfoSegmentos();

			logger.info(String.format("Número de clientes:[%d]",
					clientes.size()));

			showInfoCucsDuplicados();

			showInfoCifsDuplicados();

		} catch (IOException e) {

			logger.info(memoryBean.getHeapMemoryUsage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {

			logger.info(memoryBean.getHeapMemoryUsage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void actualizarOficinas() {
		for(Oficina oficina : oficinas){
			Oficina oficinaExistente = oficinaService.findByCodigo(oficina.getCodOficina());
			if(oficinaExistente == null){
				oficina.setId(0L);
				Oficina ret = oficinaService.create(oficina);
				logger.info("Oficina guardada con Id: " + ret.getId());
			}else{
				oficinaExistente.setNomOficina(oficina.getNomOficina());
				Oficina ret = oficinaService.create(oficinaExistente);
				logger.info("Oficina modificada: " + ret.toString() );
			}
		}
	}

	private void actualizarSectores() {
		for(Sector sector : sectores){
			Sector existente = sectorService.findByCodigo(sector.getCodSector());
			if(existente == null){
				sector.setId(0L);
				Sector ret = sectorService.create(sector);
				logger.info("Sector guardado con Id: " + ret.getId());
			}else{
				existente.setNomSector(sector.getNomSector());
				Sector ret = sectorService.create(existente);
				logger.info("Sector modificado: " + ret.toString() );
			}
		}
	}

	private void actualizarSubSectores() {
		for(SubSector sector : subSectores){
			SubSector existente = subSectorService.findByCodigo(sector.getCodSubSector());
			if(existente == null){
				sector.setId(0L);
				SubSector ret = subSectorService.create(sector);
				logger.info("SubSector guardado con Id: " + ret.getId());
			}else{
				existente.setNomSubSector(sector.getNomSubSector());
				SubSector ret = subSectorService.create(existente);
				logger.info("SubSector modificado: " + ret.toString() );
			}
		}
	}

	private void actualizarTerritorios() {
		for(Territorio item : territorios){
			Territorio existente = territorioService.findByCodigo(item.getCodTerritorio());
			if(existente == null){
				item.setId(0L);
				Territorio ret = territorioService.create(item);
				logger.info("Territorio guardado con Id: " + ret.getId());
			}else{
				existente.setNomTerritorio(item.getNomTerritorio());
				Territorio ret = territorioService.create(existente);
				logger.info("Territorio modificado: " + ret.toString() );
			}
		}
	}

	private void actualizarClientes() {
		for(Cliente item : clientes.values()){
			Cliente existente = clienteService.findByCuc(item.getCucCliente());
			if(existente == null){
				item.setId(0L);
				Cliente ret = clienteService.create(item);
				logger.info("Cliente guardado con Id: " + ret.getId());
			}else{
				existente.setNomCliente(item.getNomCliente());
				existente.setCucClienteG(item.getCucClienteG());
				Cliente ret = clienteService.create(existente);
				logger.info("Cliente modificado: " + ret.toString() );
			}
		}
	}

	private void actualizarRedDeVentas() {
		for(RedDeVentas elem : comerciales){
			RedDeVentas existente = redDeVentasService.findByMatricula(elem.getMatricula());
			if(existente == null){
				elem.setId(0L);
				RedDeVentas ret = redDeVentasService.create(elem);
				logger.info("Sector guardado con Id: " + ret.getId());
			}else{
				existente.setNombre(elem.getNombre());
				RedDeVentas ret = redDeVentasService.create(existente);
				logger.info("RedDeVentas modificado: " + ret.toString() );
			}
		}
	}

	private void showInfoClientes() {
		for (String key : clientes.keySet()) {
			logger.info(String.format("Cliente:[%s]",
					clientes.get(key).toString()));
		}
		logger.info(String.format("Número de clientes:[%d]",
				clientes.size()));
	}

	private void showInfoCifsDuplicados() {
		for (String key : cifsDuplicados.keySet()) {
			logger.info(String.format("CIF Duplicado:[%s]",
					cifsDuplicados.get(key).toString()));
		}
		logger.info(String.format("Número de cifs duplicados:[%d]",
				cifsDuplicados.size()));
	}

	private void showInfoSegmentos() {
		for (Segmento segmento : segmentos) {
			logger.info(String.format("Segmento:[%s]",
					segmento.toString()));
		}
		logger.info(String.format("Número de segmentos:[%d]",
				segmentos.size()));
	}

	private void showInfoCucsDuplicados() {
		for (String key : duplicados.keySet()) {
			logger.info(String.format("CUC Duplicado:[%s]", duplicados
					.get(key).toString()));
		}
		logger.info(String.format("Número de cucs duplicados:[%d]",
				duplicados.size()));
	}

	private void showInfoRedDeVentas() {
		for (RedDeVentas comercial : comerciales) {
			logger.info(String.format("RedDeVentas:[%s]",
					comercial.toString()));
		}
		logger.info(String.format("Número de comerciales:[%d]",
				comerciales.size()));
	}

	private void showInfoOficinas() {
		for (Oficina oficina : oficinas) {
			logger.info(String.format("Oficina:[%s]",
					oficina.toString()));
		}
		logger.info(String.format("Número de oficinas:[%d]",
				oficinas.size()));
	}

	private void showInfoTerritorios() {
		for (Territorio territorio : territorios) {
			logger.info(String.format("Territorio:[%s]",
					territorio.toString()));
		}
		logger.info(String.format("Número de territorios:[%d]",
				territorios.size()));
	}

	private void showInfoSubSectores() {
		for (SubSector sector : subSectores) {
			logger.info(String.format("SubSector:[%s]",
					sector.toString()));
		}
		logger.info(String.format("Número de subsectores:[%d]",
				subSectores.size()));
	}

	private void showInfoSectores() {
		for (Sector sector : sectores) {
			logger.info(String.format("Sector:[%s]",
					sector.toString()));
		}
		logger.info(String.format("Número de sectores:[%d]",
				sectores.size()));
	}

	private void showInfoGerencias() {
		for (Gerencia gerencia : gerencias) {
			logger.info(String.format("Gerencia:[%s]",
					gerencia.toString()));
		}
		logger.info(String.format("Número de gerencias:[%d]",
				gerencias.size()));
	}

	private void procesaTabla(Database db, String tableName) {
		
		Row cloned = null;
		int count = 0;
		int numRows = 0;
		try {
			Table table = db.getTable(tableName);
			numRows = table.getRowCount();
			
			int index = 0;
			while( index < table.getRowCount() ){
				try {
					Row row = table.getNextRow();
					procesaFila(row);
					count++;
				} catch (Exception e) {
					System.err.println("RuntimeIOException!!");
					System.err.println("Número registro: " + index);
					System.err.println("Total registros: " + numRows);
				}
				index++;
			}
			
			logger.info(String.format("numRows:[%d], procesadas:[%d]",
					numRows, count));

		} catch (RuntimeIOException e) {
			
			System.err.println("RuntimeIOException!!");
			System.err.println("Número registro: " + count);
			System.err.println("Total registros: " + numRows);
			System.err.println("Errónea: " + cloned);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			
			System.err.println("IOException!!");
			System.err.println("Número registro: " + count);
			System.err.println("Total registros: " + numRows);
			System.err.println("Errónea: " + cloned);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			
			System.err.println("Exception!!");
			System.err.println("Número registro: " + count);
			System.err.println("Total registros: " + numRows);
			System.err.println("Errónea: " + cloned);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void procesaFila(Row row) {
		
		// Datos 
		captureGerencia(row);
		captureSector(row);
		captureSubSector(row);
		captureTerritorio(row);
		captureOficina(row);
		captureSegmento(row);

		// RedDeVentas
		captureRedDeVentas(row, MAT_VENDEDOR, NOM_VENDEDOR);
		captureRedDeVentas(row, MAT_DESARROLLADOR, NOM_DESARROLLADOR);
		captureRedDeVentas(row, MAT_J_VENTAS, NOM_J_VENTAS);
		captureRedDeVentas(row, MAT_J_AREA, NOM_J_AREA);
		captureRedDeVentas(row, MAT_GERENTE, NOM_GERENTE);

		// cliente
		captureCliente(row);
	}

	private void captureCliente(Row row) {
		
		String codCliente = (String) row.get("Cod_Clie");
		String tipoDocCliente = (String) row.get("Tipo_Doc");
		String cifCliente = (String) row.get("CIF");
		String nomCliente = (String) row.get("Cliente");
		
		String codClienteG = (String) row.get("Cod_Clie_G");
		String tipoDocClienteG = (String) row.get("Tipo_Doc_G");
		String cifClienteG = (String) row.get("CIF_G");
		String nomClienteG = (String) row.get("Cliente_G");
		
		Cliente cliente = null;
		Cliente clienteG = null;
		
		if (codClienteG != null) {
			clienteG = new Cliente.Builder()
					.cucCliente(codClienteG)
					.tipoDocCliente(tipoDocClienteG)
					.cifCliente(cifClienteG)
					.nomCliente(nomClienteG)
					.cucClienteG(codClienteG)
					.build();

			addListaClientesCabeceraPorCuc(codClienteG, clienteG);

			addListaClientesCabeceraPorTipoDocCif(tipoDocClienteG, cifClienteG, clienteG);
			
		}// if(codCliente != null){

		if (codCliente != null) {
			cliente = new Cliente.Builder()
					.cucCliente(codCliente)
					.tipoDocCliente(tipoDocCliente)
					.cifCliente(cifCliente)
					.nomCliente(nomCliente)
					.cucClienteG(codClienteG)
					.build();

			// Sólo añadimos al cliente si es diferente del Supra
			if(!cliente.equals(clienteG)){
				addListaClientesPorCuc(codCliente, cliente);
				addListaClientesPorTipoDocCif(tipoDocCliente, cifCliente, cliente);
			}
			
		}// if(codCliente != null){

	}

	private void addListaClientesPorTipoDocCif(
			String tipoDocCliente,
			String cifCliente, 
			Cliente cliente) {
		// Lista de clientes por cif
		if (!cifs.containsKey(tipoDocCliente + cifCliente)) {
			cifs.put(tipoDocCliente + cifCliente, cliente);
		} else {
			System.err.println(
					String.format(ERROR_CIF_ALREADY_IN_LIST, tipoDocCliente + cifCliente));
			cifsDuplicados.put(tipoDocCliente + cifCliente, cliente);
		}
	}

	private void addListaClientesCabeceraPorTipoDocCif(
			String tipoDocCliente,
			String cifCliente, 
			Cliente cliente) {
		// Lista de clientes por cif
		if (!cifs.containsKey(tipoDocCliente + cifCliente)) {
			cifs.put(tipoDocCliente + cifCliente, cliente);
		}
	}

	private void addListaClientesPorCuc(
			String codCliente, 
			Cliente cliente) {
		// Lista de clientes por cuc
		if (!clientes.containsKey(codCliente)) {
			clientes.put(codCliente, cliente);
		} else {
			System.err.println(
					String.format(ERROR_CUC_ALREADY_IN_LIST, codCliente));
			duplicados.put(codCliente, cliente);
		}
	}

	private void addListaClientesCabeceraPorCuc(
			String codCliente, 
			Cliente cliente) {
		// Lista de clientes por cuc
		if (!clientes.containsKey(codCliente)) {
			clientes.put(codCliente, cliente);
		}
	}

	private void listTables(Database db) throws IOException {
		Set<String> tablas = db.getTableNames();
		for (String tabla : tablas) {
			logger.info(String.format("Tabla:[%s]", tabla));
		}
	}

	private void captureRedDeVentas(Row row, String matricula, String nombre) {
		// Vendedor
		String mat = (String) row.get(matricula);
		String nom = (String) row.get(nombre);
		RedDeVentas rdv;
		if (mat != null) {
			rdv = new RedDeVentas.Builder()
					.matricula(mat)
					.nombre(nom.replace("#", "Ñ")
							.replace("|", "Ñ")
							.replace("š", "a")
							.replace("•", "Í")
							.replace("Æ", "Í")
							.replace("‘", "É"))
					.build();

			comerciales.add(rdv);
		}
	}

	private void captureOficina(Row row) {
		// Oficina
		String codOficina = (String) row.get(COD_OFICINA);
		String nomOficina = (String) row.get(NOM_OFICINA);
		Oficina oficina;
		if (codOficina != null) {
			oficina = new Oficina.Builder()
					.codOficina(codOficina)
					.nomOficina(nomOficina)
					.build();
			oficinas.add(oficina);
		}
	}

	private void captureSegmento(Row row) {
		// Oficina
		String segmento = (String) row.get(SEGMENTO);
		String subSegmento = (String) row.get(SUBSEGMENTO);
		String nivelDeAtencion = (String) row.get(NIVEL_DE_ATENCION);
		Segmento seg;
		if (segmento != null) {
			seg = new Segmento.Builder().segmento(segmento)
					.subSegmento(subSegmento).nivelDeAtencion(nivelDeAtencion)
					.build();
			segmentos.add(seg);
		}
	}

	private void captureTerritorio(Row row) {
		// Territorio
		String codTerritorio = (String) row.get(COD_TERRITORIO);
		String nomTerritorio = (String) row.get(NOM_TERRITORIO);
		Territorio territorio;
		if (codTerritorio != null) {
			territorio = new Territorio.Builder().codTerritorio(codTerritorio)
					.nomTerritorio(nomTerritorio).build();
			territorios.add(territorio);
		}
	}

	private void captureSubSector(Row row) {
		// SubSector
		String codSubSector = (String) row.get(COD_SUB_SECTOR);
		String nomSubSector = (String) row.get(NOM_SUB_SECTOR);
		SubSector subSector;
		if (codSubSector != null) {
			subSector = new SubSector.Builder().codSubSector(codSubSector)
					.nomSubSector(nomSubSector).build();
			subSectores.add(subSector);
		}
	}

	private void captureSector(Row row) {
		// Sector
		String codSector = (String) row.get(COD_SECTOR);
		String nomSector = (String) row.get(NOM_SECTOR);
		if (codSector != null) {
			Sector sector = new Sector.Builder().codSector(codSector)
					.nomSector(nomSector).build();
			sectores.add(sector);
		}
	}

	private void captureGerencia(Row row) {
		// Gerencia
		String codGerencia = (String) row.get(COD_GERENCIA);
		String nomGerencia = (String) row.get(NOM_GERENCIA);
		if (codGerencia != null) {
			Gerencia gerencia = new Gerencia.Builder().codGerencia(codGerencia)
					.nomGerencia(nomGerencia).build();
			gerencias.add(gerencia);
		}
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
