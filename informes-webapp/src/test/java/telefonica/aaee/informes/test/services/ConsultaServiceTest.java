package telefonica.aaee.informes.test.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.init.InformesConfig;
import telefonica.aaee.informes.init.JPA977Config;
import telefonica.aaee.informes.init.StaticConfig;
import telefonica.aaee.informes.model.Consulta;
import telefonica.aaee.informes.services.ConsultaService;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={InformesConfig.class, JPA977Config.class, StaticConfig.class})
@Transactional
public class ConsultaServiceTest {
	
	@Autowired
	private ConsultaService consultaService;

	@Test
	public void testSearch_TRF() {

		Page<Consulta> ret =consultaService.getPage("TRF", 1);

		assertTrue("El número de elementos será máximo 5.", ret.getNumberOfElements() == 5);
		
		Page<Consulta> ret2 =consultaService.getPage("TRF");

		assertTrue("El número de elementos será 9.", ret2.getNumberOfElements() == 9);
	}

}
