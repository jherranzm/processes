package telefonica.aaee.segmentacion.process;


import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telefonica.aaee.segmentacion.model.Cliente;
import telefonica.aaee.segmentacion.model.Exportable;
import telefonica.aaee.segmentacion.model.Gerencia;
import telefonica.aaee.segmentacion.model.Oficina;
import telefonica.aaee.segmentacion.model.RedDeVentas;
import telefonica.aaee.segmentacion.model.Sector;
import telefonica.aaee.segmentacion.model.Segmentacion;
import telefonica.aaee.segmentacion.model.Segmento;
import telefonica.aaee.segmentacion.model.SubSector;
import telefonica.aaee.segmentacion.model.Territorio;
import telefonica.aaee.segmentacion.services.ClienteService;
import telefonica.aaee.segmentacion.services.GerenciaService;
import telefonica.aaee.segmentacion.services.OficinaService;
import telefonica.aaee.segmentacion.services.RedDeVentasService;
import telefonica.aaee.segmentacion.services.SectorService;
import telefonica.aaee.segmentacion.services.SubSectorService;
import telefonica.aaee.segmentacion.services.TerritorioService;
import telefonica.aaee.segmentacion.util.writer.ToCSVFileWriter;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.RuntimeIOException;
import com.healthmarketscience.jackcess.Table;

@Service
public class SegmentacionProcessor {

	protected final Log logger = LogFactory.getLog(getClass());

	private static final String CLIENTE = "Cliente";
	private static final String CIF = "CIF";
	private static final String TIPO_DOC = "Tipo_Doc";
	private static final String COD_CLIENTE = "Cod_Clie";
	
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
	
//	private Set<Oficina> oficinas = new HashSet<Oficina>();
	private SortedMap<String, Oficina> oficinas = new TreeMap<String, Oficina>();

	private SortedMap<String, Sector> sectores = new TreeMap<String, Sector>();
	private SortedMap<String, SubSector> subSectores = new TreeMap<String, SubSector>();
	private SortedMap<String, Territorio> territorios = new TreeMap<String, Territorio>();
	private SortedMap<String, RedDeVentas> comerciales = new TreeMap<String, RedDeVentas>();
	private Set<Gerencia> gerencias = new HashSet<Gerencia>();
	private Set<Segmento> segmentos = new HashSet<Segmento>();
	private Set<Segmentacion> segmentaciones = new HashSet<Segmentacion>();

	private Map<String, Cliente> clientes = new HashMap<String, Cliente>();
	private Map<String, Cliente> duplicados = new HashMap<String, Cliente>();

	private Map<String, Cliente> cifs = new HashMap<String, Cliente>();
	private Map<String, Cliente> cifsDuplicados = new HashMap<String, Cliente>();
	
	private String dir = "/Users/jherranzm/dev/testFiles/";
	
	@Autowired
	private OficinaService oficinaService;

	@Autowired
	private GerenciaService gerenciaService;

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
				
//				"NP.mdb",
//				"Pymes.mdb",
//				"Operadoras.mdb",
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
			actualizarGerencias();
			actualizarSectores();
			actualizarSubSectores();
			actualizarRedDeVentas();
			actualizarTerritorios();
			
			// actualizarClientes();
			
			ToCSVFileWriter writer = new ToCSVFileWriter();
			
			writer.setDir(this.dir);
			
			writer.printToCSVFile(new ArrayList<Exportable>(oficinas.values()), "oficinas");
			writer.printToCSVFile(new ArrayList<Exportable>(gerencias), "gerencias");
			writer.printToCSVFile(new ArrayList<Exportable>(sectores.values()), "sectores");
			writer.printToCSVFile(new ArrayList<Exportable>(subSectores.values()), "subSectores");
			writer.printToCSVFile(new ArrayList<Exportable>(territorios.values()), "territorios");
			writer.printToCSVFile(new ArrayList<Exportable>(comerciales.values()), "comerciales");
			writer.printToCSVFile(new ArrayList<Exportable>(segmentaciones), "segmentaciones");
			writer.printToCSVFile(new ArrayList<Exportable>(clientes.values()), "clientes");


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
		for(Oficina oficina : oficinas.values()){
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

	private void actualizarGerencias() {
		for(Gerencia item : gerencias){
			Gerencia existente = gerenciaService.findByCodigo(item.getCodGerencia());
			if(existente == null){
				item.setId(0L);
				Gerencia ret = gerenciaService.create(item);
				logger.info("Gerencia guardada con Id: " + ret.getId());
			}else{
				existente.setNomGerencia(item.getNomGerencia());
				Gerencia ret = gerenciaService.create(existente);
				logger.info("Gerencia modificada: " + ret.toString() );
			}
		}
	}

	private void actualizarSectores() {
		for(Sector sector : sectores.values()){
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
		for(SubSector sector : subSectores.values()){
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
		for(Territorio item : territorios.values()){
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
		for(RedDeVentas elem : comerciales.values()){
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
		for (RedDeVentas comercial : comerciales.values()) {
			logger.info(String.format("RedDeVentas:[%s]",
					comercial.toString()));
		}
		logger.info(String.format("Número de comerciales:[%d]",
				comerciales.size()));
	}

	private void showInfoOficinas() {
		for (Oficina oficina : oficinas.values()) {
			logger.info(String.format("Oficina:[%s]",
					oficina.toString()));
		}
		logger.info(String.format("Número de oficinas:[%d]",
				oficinas.size()));
	}

	private void showInfoTerritorios() {
		for (Territorio territorio : territorios.values()) {
			logger.info(String.format("Territorio:[%s]",
					territorio.toString()));
		}
		logger.info(String.format("Número de territorios:[%d]",
				territorios.size()));
	}

	private void showInfoSubSectores() {
		for (SubSector sector : subSectores.values()) {
			logger.info(String.format("SubSector:[%s]",
					sector.toString()));
		}
		logger.info(String.format("Número de subsectores:[%d]",
				subSectores.size()));
	}

	private void showInfoSectores() {
		for (Sector sector : sectores.values()) {
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
		
		// Segmentación: la relación entre todos
		captureSegmentacion(row);
	}

	private void captureCliente(Row row) {
		
		String codCliente = (String) row.get(COD_CLIENTE);
		String tipoDocCliente = (String) row.get(TIPO_DOC);
		String cifCliente = (String) row.get(CIF);
		String nomCliente = (String) row.get(CLIENTE);
		
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
		
		// Corregimos para que no aparezca "null"
		codClienteG = (codClienteG == null ? "" : codClienteG);

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
		if (mat != null && !comerciales.containsKey(mat)) {
			RedDeVentas rdv = new RedDeVentas.Builder()
					.matricula(mat)
					.nombre(nom.replace("#", "Ñ")
							.replace("|", "Ñ")
							.replace("š", "a")
							.replace("•", "Í")
							.replace("Æ", "Í")
							.replace("‘", "É"))
					.build();

			comerciales.put(mat, rdv);
		}
	}

	private void captureOficina(Row row) {
		// Oficina
		String codOficina = (String) row.get(COD_OFICINA);
		String nomOficina = (String) row.get(NOM_OFICINA);
		if (codOficina != null && !oficinas.containsKey(codOficina)) {
			Oficina oficina = new Oficina.Builder()
					.codOficina(codOficina)
					.nomOficina(nomOficina)
					.build();
			//oficinas.add(oficina);
			oficinas.put(codOficina, oficina);
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
		if (codTerritorio != null && !territorios.containsKey(codTerritorio)) {
			Territorio territorio = new Territorio.Builder()
				.codTerritorio(codTerritorio)
				.nomTerritorio(nomTerritorio)
				.build();
			territorios.put(codTerritorio, territorio);
		}
	}

	private void captureSubSector(Row row) {
		// SubSector
		String codSubSector = (String) row.get(COD_SUB_SECTOR);
		String nomSubSector = (String) row.get(NOM_SUB_SECTOR);
		if (codSubSector != null && !subSectores.containsKey(codSubSector)) {
			SubSector subSector = new SubSector.Builder()
				.codSubSector(codSubSector)
				.nomSubSector(nomSubSector)
				.build();
			subSectores.put(codSubSector,subSector);
		}
	}

	private void captureSector(Row row) {
		// Sector
		String codSector = (String) row.get(COD_SECTOR);
		String nomSector = (String) row.get(NOM_SECTOR);
		if (codSector != null && !sectores.containsKey(codSector)) {
			Sector sector = new Sector.Builder()
				.codSector(codSector)
				.nomSector(nomSector)
				.build();
			sectores.put(codSector,sector);
		}
	}

	private void captureGerencia(Row row) {
		// Gerencia
		String codGerencia = (String) row.get(COD_GERENCIA);
		String nomGerencia = (String) row.get(NOM_GERENCIA);
		if (codGerencia != null) {
			Gerencia gerencia = new Gerencia.Builder()
				.codGerencia(codGerencia)
				.nomGerencia(nomGerencia)
				.build();
			gerencias.add(gerencia);
		}
	}

	private void captureSegmentacion(Row row) {

		// Cliente : CUC
		String codCliente = (String) row.get(COD_CLIENTE);
		// Territorio
		String codTerritorio = (String) row.get(COD_TERRITORIO);
		codTerritorio = (codTerritorio == null ? "" : codTerritorio);
		// Gerencia
		String codGerencia = (String) row.get(COD_GERENCIA);
		codGerencia = (codGerencia == null ? "" : codGerencia);
		// Sector
		String codSector = (String) row.get(COD_SECTOR);
		codSector = (codSector == null ? "" : codSector);
		// SubSector
		String codSubSector = (String) row.get(COD_SUB_SECTOR);
		codSubSector = (codSubSector == null ? "" : codSubSector);
		// Oficina
		String codOficina = (String) row.get(COD_OFICINA);
		codOficina = (codOficina == null ? "" : codOficina);
		// Segmento
		String segmento = (String) row.get(SEGMENTO);
//		segmento = (segmento == null ? "" : segmento);
		String subSegmento = (String) row.get(SUBSEGMENTO);
//		subSegmento = (subSegmento == null ? "" : subSegmento);
		String nivelDeAtencion = (String) row.get(NIVEL_DE_ATENCION);
//		nivelDeAtencion = (nivelDeAtencion == null ? "" : nivelDeAtencion);
		// RedDeVentas
		String matVendedor 		= (String) row.get(MAT_VENDEDOR);
		matVendedor = (matVendedor == null ? "" : matVendedor);
		String matDesarrollador = (String) row.get(MAT_DESARROLLADOR);
		matDesarrollador = (matDesarrollador == null ? "" : matDesarrollador);
		String matJVentas		= (String) row.get(MAT_J_VENTAS);
		matJVentas = (matJVentas == null ? "" : matJVentas);
		String matJArea			= (String) row.get(MAT_J_AREA);
		matJArea = (matJArea == null ? "" : matJArea);
		String matGerente		= (String) row.get(MAT_GERENTE);
		matGerente = (matGerente == null ? "" : matGerente);
		
		Segmentacion seg;
		if (segmento != null) {
			seg = new Segmentacion.Builder()
				.cucCliente(codCliente)
				.codTerritorio(codTerritorio)
				.codGerencia(codGerencia)
				.codOficina(codOficina)
				.codSector(codSector)
				.codSubSector(codSubSector)
				.matVendedor(matVendedor)
				.matDesarrollador(matDesarrollador)
				.matJVentas(matJVentas)
				.matJArea(matJArea)
				.matGerente(matGerente)
				.build();
			segmentaciones.add(seg);
		}
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
