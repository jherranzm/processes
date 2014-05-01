package telefonica.aaee.informes.services.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import telefonica.aaee.informes.model.Consulta;

public abstract class ConsultaSpecifications {
	public static Specification<Consulta> searchByNombre(final String name) {
        return new Specification<Consulta>() {

           @Override
           public Predicate toPredicate(
        		   Root<Consulta> root
        		   , CriteriaQuery<?> criteriaQuery
        		   , CriteriaBuilder criteriaBuilder)
           {
               return criteriaBuilder.like(
            		   root.<String>get("nombre")
            		   	, "%" + name + "%");
           }
       };
     }
}
