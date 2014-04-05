package telefonica.aaee.informes.init;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * 
 * Replaces dispatcher-servlet.xml
 * 
 * @author aaeeadmin
 *
 */
//Marks this class as configuration
@Configuration
// Specifies which package to scan
@ComponentScan("telefonica.aaee.informes")
// Enables Spring's annotations
@EnableWebMvc
public class InformesConfig extends WebMvcConfigurationSupport {

	@Bean
	public UrlBasedViewResolver setupViewResolver() {
		
		System.out.println("setupViewResolver...");
		
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		return resolver;
	}
	
	/**
	 * 
	 * Bean necesaria para subida de archivos.
	 */
	@Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

	
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    messageSource.setUseCodeAsDefaultMessage(true);
	    return messageSource;
	}
	
	
	/**
	 * i18n
	 * 
	 * @return
	 */
	@Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
 
        LocaleChangeInterceptor result = new LocaleChangeInterceptor();
        result.setParamName("lang");
 
        return result;
 
    }
	
	@Bean
    public LocaleResolver localeResolver() {
 
        SessionLocaleResolver result = new SessionLocaleResolver();
        result.setDefaultLocale(new Locale("es", "ES"));
 
        return result;
 
    }
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    
    @Override
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    	RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
		handlerMapping.setOrder(0);
		handlerMapping.setInterceptors(getInterceptors());
		return handlerMapping;
	}
 
}