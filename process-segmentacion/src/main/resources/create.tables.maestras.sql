USE maestras;

DROP VIEW IF EXISTS maestras.v_cliente;

DROP TABLE IF EXISTS maestras.tbl_gerencia;
DROP TABLE IF EXISTS maestras.tbl_oficina;
DROP TABLE IF EXISTS maestras.tbl_sector;
DROP TABLE IF EXISTS maestras.tbl_subsector;
DROP TABLE IF EXISTS maestras.tbl_territorio;
DROP TABLE IF EXISTS maestras.tbl_reddeventas;
DROP TABLE IF EXISTS maestras.tbl_segmento;
DROP TABLE IF EXISTS maestras.tbl_subsegmento;
DROP TABLE IF EXISTS maestras.tbl_niveldeatencion;

DROP TABLE IF EXISTS maestras.tbl_cliente;
DROP TABLE IF EXISTS maestras.tbl_segmentacion;

CREATE TABLE IF NOT EXISTS maestras.tbl_gerencia (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Gerencia varchar(10) NOT NULL,
  Nom_Gerencia varchar(255) NOT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_Gerencia_UNIQUE (Cod_Gerencia),
  KEY idxCodGerencia (Cod_Gerencia),
  KEY idxNomGerencia (Nom_Gerencia)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_oficina (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Oficina varchar(10) NOT NULL,
  Nom_Oficina varchar(45) NOT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_Oficina_UNIQUE (Cod_Oficina),
  KEY idxCodOficina (Cod_Oficina),
  KEY idxNomOficina (Nom_Oficina)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_sector (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Sector varchar(10) NOT NULL,
  Nom_Sector varchar(45) NOT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_Sector_UNIQUE (Cod_Sector),
  KEY idxCodSector (Cod_Sector),
  KEY idxNomSector (Nom_Sector)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_subsector (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_SubSector varchar(10) NOT NULL,
  Nom_SubSector varchar(45) NOT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_SubSector_UNIQUE (Cod_SubSector),
  KEY idxCodSubSector (Cod_SubSector),
  KEY idxNomSubSector (Nom_SubSector)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_territorio (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Territorio varchar(10) NOT NULL,
  Nom_Territorio varchar(45) NOT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_Territorio_UNIQUE (Cod_Territorio),
  KEY idxCodTerritorio (Cod_Territorio),
  KEY idxNomTerritorio (Nom_Territorio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_segmento (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Segmento varchar(10) NOT NULL,
  Nom_Segmento varchar(45) DEFAULT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_Segmento_UNIQUE (Cod_Segmento),
  KEY idxCodSegmento (Cod_Segmento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_subsegmento (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_SubSegmento varchar(10) NOT NULL,
  Nom_SubSegmento varchar(45) DEFAULT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_SubSegmento_UNIQUE (Cod_SubSegmento),
  KEY idxCodSubSegmento (Cod_SubSegmento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_niveldeatencion (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Nivel_De_Atencion varchar(50) NOT NULL,
  Nom_Nivel_De_Atencion varchar(255) DEFAULT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Cod_Nivel_De_Atencion_UNIQUE (Cod_Nivel_De_Atencion),
  KEY idxCodNivelDeAtencion (Cod_Nivel_De_Atencion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS maestras.tbl_reddeventas (
  id int(11) NOT NULL AUTO_INCREMENT,
  Matricula varchar(10) NOT NULL,
  Nombre varchar(255) NOT NULL,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY Matricula_UNIQUE (Matricula),
  KEY idxMatricula (Matricula),
  KEY idxNombre (Nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
CREATE TABLE IF NOT EXISTS maestras.tbl_cliente (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Cliente varchar(10) NOT NULL,
  Tipo_Doc varchar(1) NOT NULL,
  CIF_Cliente varchar(16) NOT NULL,
  NOM_Cliente varchar(255) NOT NULL,
  Cod_Cliente_G varchar(10),
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  -- UNIQUE KEY Cod_Cliente_UNIQUE (Cod_Cliente),
  KEY idxCodCliente (Cod_Cliente),
  KEY idxCIFCliente (CIF_Cliente)
) 
ENGINE=InnoDB DEFAULT CHARSET=utf8
-- PARTITION BY HASH(id)
-- PARTITIONS 50
;

CREATE TABLE IF NOT EXISTS maestras.tbl_segmentacion (
  id int(11) NOT NULL AUTO_INCREMENT,
  Cod_Cliente varchar(10) NOT NULL,
  Id_Territorio int(11) NOT NULL,
  Id_Gerencia int(11) DEFAULT -1,
  Id_Oficina int(11) DEFAULT -1,
  Id_Sector int(11) DEFAULT -1,
  Id_SubSector int(11) DEFAULT -1,
  Id_Segmento int(11) DEFAULT -1,
  Id_SubSegmento int(11) DEFAULT -1,
  Id_NivelDeAtencion int(11) DEFAULT -1,
  Mat_Vendedor int(11) DEFAULT -1,
  Mat_Desarrollador int(11) DEFAULT -1,
  Mat_JVentas int(11) DEFAULT -1,
  Mat_JArea int(11) DEFAULT -1,
  Mat_Gerente int(11) DEFAULT -1,
  fecha_creacion timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idxCodCliente (Cod_Cliente)
) 
ENGINE=InnoDB DEFAULT CHARSET=utf8
-- PARTITION BY HASH(id)
-- PARTITIONS 50
;

truncate table maestras.tbl_oficina;
truncate table maestras.tbl_territorio;
truncate table maestras.tbl_sector;
truncate table maestras.tbl_subsector;
truncate table maestras.tbl_reddeventas;
truncate table maestras.tbl_gerencia;
truncate table maestras.tbl_segmento;
truncate table maestras.tbl_subsegmento;
truncate table maestras.tbl_niveldeatencion;

DROP VIEW IF EXISTS maestras.v_cliente;
CREATE VIEW maestras.v_cliente AS 
select 
	c.id AS id,
	c.Cod_Cliente AS Cod_Cliente,
	c.Tipo_Doc AS Tipo_Doc,
	c.CIF_Cliente AS CIF_Cliente,
	c.NOM_Cliente AS NOM_Cliente,

	c.Cod_Cliente_G AS Cod_Cliente_G,
	g.Tipo_Doc AS Tipo_Doc_G,
	g.CIF_Cliente AS CIF_Cliente_G,
	g.NOM_Cliente AS NOM_Cliente_G,

	o.Cod_Oficina AS Cod_Oficina,
	o.NOM_Oficina AS Nom_Oficina,

	sec.Cod_Sector AS Cod_Sector,
	sec.NOM_Sector AS Nom_Sector,

	ssec.Cod_SubSector AS Cod_SubSector,
	ssec.NOM_SubSector AS Nom_SubSector,

	ger.Cod_Gerencia AS Cod_Gerencia,
	ger.NOM_Gerencia AS Nom_Gerencia,

	seg.Cod_Segmento AS Cod_Segmento,

	sseg.Cod_SubSegmento AS Cod_SubSegmento,

	na.Cod_Nivel_De_Atencion AS Cod_Nivel_De_Atencion,

	v.Matricula AS Mat_Vendedor,
	v.Nombre AS Nom_Vendedor,

	d.Matricula AS Mat_Desarrollador,
	d.Nombre AS Nom_Desarrollador,

	jv.Matricula AS Mat_JVentas,
	jv.Nombre AS Nom_JVentas,

	ja.Matricula AS Mat_JArea,
	ja.Nombre AS Nom_JArea,

	ge.Matricula AS Mat_Gerente,
	ge.Nombre AS Nom_Gerente

from maestras.tbl_cliente c 

left join maestras.tbl_cliente g 
	on c.Cod_Cliente_G = g.Cod_Cliente 

left join maestras.tbl_segmentacion s 
	on c.Cod_Cliente = s.Cod_Cliente

left join maestras.tbl_oficina o 
	on s.id_Oficina = o.id

left join maestras.tbl_sector sec 
	on s.id_Sector = sec.id
left join maestras.tbl_subsector ssec 
	on s.id_SubSector = ssec.id
left join maestras.tbl_gerencia ger 
	on s.id_Gerencia = ger.id

left join maestras.tbl_segmento seg
	on s.id_Segmento = seg.id
left join maestras.tbl_subsegmento sseg
	on s.id_SubSegmento = sseg.id
left join maestras.tbl_niveldeatencion na
	on s.id_NivelDeAtencion = na.id

left join maestras.tbl_reddeventas v
	on s.Mat_Vendedor = v.id

left join maestras.tbl_reddeventas d
	on s.Mat_Desarrollador = d.id

left join maestras.tbl_reddeventas jv
	on s.Mat_JVentas = jv.id

left join maestras.tbl_reddeventas ja
	on s.Mat_JArea = ja.id

left join maestras.tbl_reddeventas ge
	on s.Mat_Gerente = ge.id
;


