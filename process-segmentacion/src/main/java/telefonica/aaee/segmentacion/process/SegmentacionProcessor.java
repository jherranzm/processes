package telefonica.aaee.segmentacion.process;


import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telefonica.aaee.segmentacion.exceptions.ClienteNotFoundException;
import telefonica.aaee.segmentacion.exceptions.RedDeVentasNotFoundException;
import telefonica.aaee.segmentacion.exceptions.SectorNotFoundException;
import telefonica.aaee.segmentacion.exceptions.SubSectorNotFoundException;
import telefonica.aaee.segmentacion.exceptions.TerritorioNotFoundException;
import telefonica.aaee.segmentacion.model.Cliente;
import telefonica.aaee.segmentacion.model.Exportable;
import telefonica.aaee.segmentacion.model.Gerencia;
import telefonica.aaee.segmentacion.model.NivelDeAtencion;
import telefonica.aaee.segmentacion.model.Oficina;
import telefonica.aaee.segmentacion.model.RedDeVentas;
import telefonica.aaee.segmentacion.model.Sector;
import telefonica.aaee.segmentacion.model.Segmentacion;
import telefonica.aaee.segmentacion.model.Segmento;
import telefonica.aaee.segmentacion.model.SubSector;
import telefonica.aaee.segmentacion.model.SubSegmento;
import telefonica.aaee.segmentacion.model.Territorio;
import telefonica.aaee.segmentacion.services.ClienteService;
import telefonica.aaee.segmentacion.services.GerenciaService;
import telefonica.aaee.segmentacion.services.NivelDeAtencionService;
import telefonica.aaee.segmentacion.services.OficinaService;
import telefonica.aaee.segmentacion.services.RedDeVentasService;
import telefonica.aaee.segmentacion.services.SectorService;
import telefonica.aaee.segmentacion.services.SegmentoService;
import telefonica.aaee.segmentacion.services.SubSectorService;
import telefonica.aaee.segmentacion.services.SubSegmentoService;
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
	
	private SortedMap<String, Oficina> oficinas = new TreeMap<String, Oficina>();
	private SortedMap<String, Sector> sectores = new TreeMap<String, Sector>();
	private SortedMap<String, SubSector> subSectores = new TreeMap<String, SubSector>();
	private SortedMap<String, Territorio> territorios = new TreeMap<String, Territorio>();
	private SortedMap<String, RedDeVentas> comerciales = new TreeMap<String, RedDeVentas>();
	private SortedMap<String, Gerencia> gerencias = new TreeMap<String, Gerencia>();
	private SortedMap<String, Segmento> segmentos = new TreeMap<String, Segmento>();
	private SortedMap<String, SubSegmento> subSegmentos = new TreeMap<String, SubSegmento>();
	private SortedMap<String, NivelDeAtencion> nivelesDeAtencion = new TreeMap<String, NivelDeAtencion>();

	private Set<Segmentacion> segmentaciones = new HashSet<Segmentacion>();

	private Map<String, Cliente> clientes = new HashMap<String, Cliente>();
	private Map<String, Cliente> duplicados = new HashMap<String, Cliente>();

	private Map<String, Cliente> cifs = new HashMap<String, Cliente>();
	private Map<String, Cliente> cifsDuplicados = new HashMap<String, Cliente>();
	
	private List<String> mensajes = new ArrayList<String>();
	
	private String dir = "/Users/jherranzm/dev/testFiles/";
	
	@Autowired
	private OficinaService oficinaService;

	@Autowired
	private SegmentoService segmentoService;

	@Autowired
	private SubSegmentoService subSegmentoService;

	@Autowired
	private NivelDeAtencionService nivelDeAtencionService;

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
				
				"NP.mdb",
				"Pymes.mdb",
				"Operadoras.mdb",
				"Empresas.mdb"
				};

		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

		try {
			
			loadDataFromDB();
			
			for(String mdb : mdbs){
				Database db = DatabaseBuilder.open(new File(this.dir + mdb));
				
				listTables(db);

				for (String tabla : db.getTableNames()) {
					if(tabla.contains("Clientes")){
						procesaTabla(db, tabla);
					}
				}
			}
			
			
			updateDataInDB();
			
			saveDataInCSVFile();


			logger.info(String.format("Número de clientes:[%d]",
					clientes.size()));

			showInfoCucsDuplicados();

			showInfoCifsDuplicados();
			
			for(String mensaje : mensajes){
				logger.info(mensaje);
			}

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

	private void updateDataInDB() {
		actualizarOficinas();
		actualizarGerencias();
		actualizarSectores();
		actualizarSubSectores();
		actualizarRedDeVentas();
		actualizarTerritorios();
	}

	private void saveDataInCSVFile() {
		ToCSVFileWriter writer = new ToCSVFileWriter();
		
		writer.setDir(this.dir);
		
		writer.printToCSVFile(new ArrayList<Exportable>(oficinas.values()), "oficinas");
		writer.printToCSVFile(new ArrayList<Exportable>(gerencias.values()), "gerencias");
		writer.printToCSVFile(new ArrayList<Exportable>(sectores.values()), "sectores");
		writer.printToCSVFile(new ArrayList<Exportable>(subSectores.values()), "subSectores");
		writer.printToCSVFile(new ArrayList<Exportable>(territorios.values()), "territorios");
		writer.printToCSVFile(new ArrayList<Exportable>(comerciales.values()), "comerciales");
		writer.printToCSVFile(new ArrayList<Exportable>(segmentaciones), "segmentaciones");
		writer.printToCSVFile(new ArrayList<Exportable>(clientes.values()), "clientes");

		writer.printToTXTFile(new ArrayList<String>(mensajes), "mensajes");
	}

	private void loadDataFromDB() {
		for(Oficina oficina : oficinaService.findAll()){
			oficinas.put(oficina.getCodOficina(), oficina);
		}
		
		for(Territorio territorio : territorioService.findAll()){
			territorios.put(territorio.getCodTerritorio(), territorio);
		}
		
		for(Sector sector : sectorService.findAll()){
			sectores.put(sector.getCodSector(), sector);
		}
		
		for(SubSector sector : subSectorService.findAll()){
			subSectores.put(sector.getCodSubSector(), sector);
		}
		
		for(Gerencia gerencia : gerenciaService.findAll()){
			gerencias.put(gerencia.getCodGerencia(), gerencia);
		}
		
		for(RedDeVentas rdv : redDeVentasService.findAll()){
			comerciales.put(rdv.getMatricula(), rdv);
		}
		
		for(Segmento rdv : segmentoService.findAll()){
			segmentos.put(rdv.getCodSegmento(), rdv);
		}
		
		for(SubSegmento rdv : subSegmentoService.findAll()){
			subSegmentos.put(rdv.getCodSubSegmento(), rdv);
		}
		
		for(NivelDeAtencion rdv : nivelDeAtencionService.findAll()){
			nivelesDeAtencion.put(rdv.getCodNivelDeAtencion(), rdv);
		}
		
	}

	private void actualizarOficinas() {
		for(Oficina elem : oficinas.values()){
			Oficina existente = oficinaService.findByCodigo(elem.getCodOficina());
			if(existente == null){
				elem.setId(0L);
				Oficina ret = oficinaService.create(elem);
				logger.info("Oficina cread@ con Id: " + ret.getId());
			}else{
				existente.setNomOficina(elem.getNomOficina());
				try {
					Oficina ret = oficinaService.update(existente);
					logger.info("Oficina modificada: " + ret.toString() );
				} catch (Exception e) {
					logger.error("No se ha encontrado el elemento [Oficina:"+elem.getCodOficina()+"]");
				}
			}
		}
	}

	private void actualizarGerencias() {
		for(Gerencia elem : gerencias.values()){
			Gerencia existente = gerenciaService.findByCodigo(elem.getCodGerencia());
			if(existente == null){
				elem.setId(0L);
				Gerencia ret = gerenciaService.create(elem);
				logger.info("Gerencia cread@ con Id: " + ret.getId());
			}else{
				existente.setNomGerencia(elem.getNomGerencia());
				try {
					Gerencia ret = gerenciaService.create(existente);
					logger.info("Gerencia modificada: " + ret.toString() );
				} catch (Exception e) {
					logger.error("No se ha encontrado el elemento [Gerencia:"+elem.getCodGerencia()+"]");
				}
			}
		}
	}

	private void actualizarSectores() {
		for(Sector elem : sectores.values()){
			Sector existente = sectorService.findByCodigo(elem.getCodSector());
			if(existente == null){
				elem.setId(0L);
				Sector ret = sectorService.create(elem);
				logger.info("Sector cread@ con Id: " + ret.getId());
			}else{
				existente.setNomSector(elem.getNomSector());
				try {
					Sector ret = sectorService.update(existente);
					logger.info("Sector modificado: " + ret.toString() );
				} catch (SectorNotFoundException e) {
					logger.error("No se ha encontrado el elemento [Sector:"+elem.getCodSector()+"]");
				}
			}
		}
	}

	private void actualizarSubSectores() {
		for(SubSector elem : subSectores.values()){
			SubSector existente = subSectorService.findByCodigo(elem.getCodSubSector());
			if(existente == null){
				elem.setId(0L);
				SubSector ret = subSectorService.create(elem);
				logger.info("SubSector cread@ con Id: " + ret.getId());
			}else{
				existente.setNomSubSector(elem.getNomSubSector());
				try {
					SubSector ret = subSectorService.update(existente);
					logger.info("SubSector modificado: " + ret.toString() );
				} catch (SubSectorNotFoundException e) {
					logger.error("No se ha encontrado el elemento [SubSector:"+elem.getCodSubSector()+"]");
				}
			}
		}
	}

	private void actualizarTerritorios() {
		for(Territorio elem : territorios.values()){
			Territorio existente = territorioService.findByCodigo(elem.getCodTerritorio());
			if(existente == null){
				elem.setId(0L);
				Territorio ret = territorioService.create(elem);
				logger.info("Territorio cread@ con Id: " + ret.getId());
			}else{
				existente.setNomTerritorio(elem.getNomTerritorio());
				try {
					Territorio ret = territorioService.update(existente);
					logger.info("Territorio modificado: " + ret.toString() );
				} catch (TerritorioNotFoundException e) {
					logger.error("No se ha encontrado el elemento [Cliente:"+elem.getCodTerritorio()+"]");
				}
			}
		}
	}

	private void actualizarClientes() {
		for(Cliente elem : clientes.values()){
			Cliente existente = clienteService.findByCuc(elem.getCucCliente());
			if(existente == null){
				elem.setId(0L);
				Cliente ret = clienteService.create(elem);
				logger.info("Cliente cread@ con Id: " + ret.getId());
			}else{
				existente.setNomCliente(elem.getNomCliente());
				existente.setCucClienteG(elem.getCucClienteG());
				try {
					Cliente ret = clienteService.update(existente);
					logger.info("Cliente modificado: " + ret.toString() );
				} catch (ClienteNotFoundException e) {
					logger.error("No se ha encontrado el elemento [Cliente:"+elem.getCucCliente()+"]");
				}
			}
		}
	}

	private void actualizarRedDeVentas() {
		for(RedDeVentas elem : comerciales.values()){
			RedDeVentas existente = redDeVentasService.findByMatricula(elem.getMatricula());
			if(existente == null){
				elem.setId(0L);
				RedDeVentas ret = redDeVentasService.create(elem);
				logger.info("RedDeVentas cread@ con Id: " + ret.getId());
			}else{
				existente.setNombre(elem.getNombre());
				RedDeVentas ret;
				try {
					ret = redDeVentasService.update(existente);
					logger.info("RedDeVentas modificado: " + ret.toString() );
				} catch (RedDeVentasNotFoundException e) {
					logger.error("No se ha encontrado el elemento [RedDeVentas:"+elem.getMatricula()+"]");
				}
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
		for (Segmento segmento : segmentos.values()) {
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
		for (Gerencia gerencia : gerencias.values()) {
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
					System.err.println("ProcesaFila:RuntimeIOException!!");
					System.err.println("Número registro: " + index);
					System.err.println("Total registros: " + numRows);
					e.printStackTrace();
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
		long idGerencia = captureGerencia(row);
		long idSector = captureSector(row);
		long idSubSector = captureSubSector(row);
		long idTerritorio = captureTerritorio(row);
		long idOficina = captureOficina(row);
		long idSegmento = captureSegmento(row);
		long idSubSegmento = captureSubSegmento(row);
		long idNivelDeAtencion = captureNivelDeAtencion(row);

		// RedDeVentas
		long idMatVendedor = captureRedDeVentas(row, MAT_VENDEDOR, NOM_VENDEDOR);
		long idMatDesarrollador = captureRedDeVentas(row, MAT_DESARROLLADOR, NOM_DESARROLLADOR);
		long idMatJVentas = captureRedDeVentas(row, MAT_J_VENTAS, NOM_J_VENTAS);
		long idMatJArea = captureRedDeVentas(row, MAT_J_AREA, NOM_J_AREA);
		long idMatGerente = captureRedDeVentas(row, MAT_GERENTE, NOM_GERENTE);

		// cliente
		String codCliente = captureCliente(row);
		
		// Segmentación: la relación entre todos
		Segmentacion seg = new Segmentacion.Builder()
			.cucCliente(codCliente)
			
			.idVendedor(idMatVendedor)
			.idDesarrollador(idMatDesarrollador)
			.idJVentas(idMatJVentas)
			.idJArea(idMatJArea)
			.idGerente(idMatGerente)
			
			.idTerritorio(idTerritorio)
			
			.idOficina(idOficina)
			.idGerencia(idGerencia)
			
			.idSector(idSector)
			.idSubSector(idSubSector)
			
			.idSegmento(idSegmento)
			.idSubSegmento(idSubSegmento)
			.idNivelDeAtencion(idNivelDeAtencion)
			
			.build();
		segmentaciones.add(seg);

	}

	private String captureCliente(Row row) {
		
		String codCliente = null;
		
		codCliente = (String) row.get(COD_CLIENTE);
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

		return codCliente;
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

	private long captureRedDeVentas(Row row, String matricula, String nombre) {
		// Vendedor
		String mat = (String) row.get(matricula);
		String nom = (String) row.get(nombre);
		if (mat == null){
			return -1;
		}else if(!comerciales.containsKey(mat)) {
			RedDeVentas rdv = new RedDeVentas.Builder()
					.matricula(mat)
					.nombre(nom.replace("#", "Ñ")
							.replace("|", "Ñ")
							.replace("š", "a")
							.replace("•", "Í")
							.replace("Æ", "Í")
							.replace("‘", "É"))
					.build();
			RedDeVentas nuevo = redDeVentasService.create(rdv);
			comerciales.put(mat, nuevo);
			mensajes.add("Se ha creado un nuevo RedDeVentas: " + nuevo.toString());
		}
		return comerciales.get(mat).getId();
	}

	private long captureOficina(Row row) {
		// Oficina
		String codOficina = (String) row.get(COD_OFICINA);
		String nomOficina = (String) row.get(NOM_OFICINA);
		if (codOficina == null){
			return -1;
		}else if(!oficinas.containsKey(codOficina)) {
			Oficina oficina = new Oficina.Builder()
					.codOficina(codOficina)
					.nomOficina(nomOficina)
					.build();
			Oficina nuevo = oficinaService.create(oficina);
			oficinas.put(codOficina, nuevo);
			mensajes.add("Se ha creado una nueva Oficina: " + nuevo.toString());
			
		}
		return oficinas.get(codOficina).getId();
	}

	private long captureSegmento(Row row) {
		// Oficina
		String codSegmento = (String) row.get(SEGMENTO);
		if (codSegmento == null){
			return -1;
		}else if(!segmentos.containsKey(codSegmento)) {
			Segmento segmento = new Segmento.Builder()
					.codSegmento(codSegmento)
					.build();
			Segmento nuevo = segmentoService.create(segmento);
			segmentos.put(codSegmento, nuevo);
			mensajes.add("Se ha creado un nuevo Segmento: " + nuevo.toString());
		}
		return segmentos.get(codSegmento).getId();
	}

	private long captureSubSegmento(Row row) {
		// Oficina
		String codSubSegmento = (String) row.get(SUBSEGMENTO);
		if (codSubSegmento == null){
			return -1;
		}else if(!subSegmentos.containsKey(codSubSegmento)) {
			SubSegmento segmento = new SubSegmento.Builder()
					.codSubSegmento(codSubSegmento)
					.build();
			SubSegmento nuevo = subSegmentoService.create(segmento);
			subSegmentos.put(codSubSegmento, nuevo);
			mensajes.add("Se ha creado un nuevo SubSegmento: " + nuevo.toString());
			
		}
		return subSegmentos.get(codSubSegmento).getId();
	}

	private long captureNivelDeAtencion(Row row) {
		// NivelDeAtencion
		String codNivelDeAtencion = (String) row.get(NIVEL_DE_ATENCION);
		if (codNivelDeAtencion == null){
			return -1;
		}else if(!nivelesDeAtencion.containsKey(codNivelDeAtencion)) {
			NivelDeAtencion segmento = new NivelDeAtencion.Builder()
					.codNivelDeAtencion(codNivelDeAtencion)
					.build();
			NivelDeAtencion nuevo = nivelDeAtencionService.create(segmento);
			nivelesDeAtencion.put(codNivelDeAtencion, nuevo);
			mensajes.add("Se ha creado un nuevo NivelDeAtencion: " + nuevo.toString());
			
		}
		return nivelesDeAtencion.get(codNivelDeAtencion).getId();
	}

	private long captureTerritorio(Row row) {
		// Territorio
		String codTerritorio = (String) row.get(COD_TERRITORIO);
		String nomTerritorio = (String) row.get(NOM_TERRITORIO);
		if (codTerritorio == null) {
				return -1;
		}else if(!territorios.containsKey(codTerritorio)){
			Territorio territorio = new Territorio.Builder()
				.codTerritorio(codTerritorio)
				.nomTerritorio(nomTerritorio)
				.build();
			Territorio nuevo = territorioService.create(territorio);
			territorios.put(codTerritorio, nuevo);
			mensajes.add("Se ha creado un nuevo Territorio: " + nuevo.toString());
		}
		return territorios.get(codTerritorio).getId();
		
	}

	private long captureSubSector(Row row) {
		// SubSector
		String codSubSector = (String) row.get(COD_SUB_SECTOR);
		String nomSubSector = (String) row.get(NOM_SUB_SECTOR);
		if (codSubSector == null) {
			return -1;
		}else if(!subSectores.containsKey(codSubSector)){
			SubSector subSector = new SubSector.Builder()
				.codSubSector(codSubSector)
				.nomSubSector(nomSubSector)
				.build();
			SubSector nuevo = subSectorService.create(subSector);
			subSectores.put(codSubSector, nuevo);
			mensajes.add("Se ha creado un nuevo SubSector: " + nuevo.toString());
		}
		return subSectores.get(codSubSector).getId();
		
	}

	private long captureSector(Row row) {
		// Sector
		String codSector = (String) row.get(COD_SECTOR);
		String nomSector = (String) row.get(NOM_SECTOR);
		if (codSector == null) {
			return -1;
		}else if(!sectores.containsKey(codSector)){
			Sector sector = new Sector.Builder()
				.codSector(codSector)
				.nomSector(nomSector)
				.build();
			Sector nuevo = sectorService.create(sector);
			sectores.put(codSector, nuevo);
			mensajes.add("Se ha creado un nuevo Sector: " + nuevo.toString());
		}
		return sectores.get(codSector).getId();
		
		
	}

	private long captureGerencia(Row row) {
		// Gerencia
		String codGerencia = (String) row.get(COD_GERENCIA);
		String nomGerencia = (String) row.get(NOM_GERENCIA);
		if (codGerencia == null) {
			return -1;
		}else if(!gerencias.containsKey(codGerencia)){
			Gerencia gerencia = new Gerencia.Builder()
				.codGerencia(codGerencia)
				.nomGerencia(nomGerencia)
				.build();
			Gerencia nuevo = gerenciaService.create(gerencia);
			gerencias.put(codGerencia, nuevo);
			mensajes.add("Se ha creado una nueva Gerencia: " + nuevo.toString());
		}
		return gerencias.get(codGerencia).getId();
	}

	private void captureSegmentacion(Row row) {

		
	}
	
	
	/**
	 * 
	 * 
	 * Getters & Setters
	 * 
	 * 
	 */

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public List<String> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<String> mensajes) {
		this.mensajes = mensajes;
	}

}
