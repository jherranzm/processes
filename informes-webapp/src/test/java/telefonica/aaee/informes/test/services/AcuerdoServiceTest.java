package telefonica.aaee.informes.test.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import telefonica.aaee.informes.init.InformesConfig;
import telefonica.aaee.informes.init.JPA977Config;
import telefonica.aaee.informes.init.StaticConfig;
import telefonica.aaee.informes.services.condiciones.AcuerdoService;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={InformesConfig.class, JPA977Config.class, StaticConfig.class})
@Transactional
public class AcuerdoServiceTest {
	
	@Autowired
	private AcuerdoService acuerdoService;

	@Test
	public void testApplyCondiciones() {

		boolean ret = false;
		
		ret = acuerdoService.applyCondiciones("LHOSP_2012_1T");

		assertTrue(ret);
		
	}

	@Test
	public void testAplicarPlantaPreciosEspeciales() {

		boolean ret = false;
		
		ret = acuerdoService.applyBusinessRulesAplicarPlantaPreciosEspeciales("AJMOLLET_2013");

		assertTrue(ret);
		
	}

	@Test
	public void testLineaSoporteVPNIPSinCoste() {

		boolean ret = false;
		
		ret = acuerdoService.applyBusinessRulesLineaSoporteVPNIPSinCoste("AJMOLLET_2013");

		assertTrue(ret);
		
	}
	
	@Test
	public void testCompensacionAumentoCuotaLineaBOE() {

		boolean ret = false;
		
		ret = acuerdoService.applyBusinessRulesCompensacionAumentoCuotaLineaBOE("AJMOLLET_2013");

		assertTrue(ret);
		
	}
}
