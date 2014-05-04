package telefonica.aaee.informes.controllers;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	
	@Resource
    private Environment environment;
	
	
	@PersistenceContext(unitName = "JPAInformesWebApp")
	private EntityManager entityManager;	
	

	@RequestMapping(value="/index")
	public ModelAndView home() {
		return new ModelAndView("index");
	}

	@RequestMapping("/hello")
	public String hello(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		return "helloworld";
	}

}
