package telefonica.aaee.informes.services.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import telefonica.aaee.informes.model.Informe;

public abstract class InformeSpecifications {
	public static Specification<Informe> searchByNombre(final String name) {
        return new Specification<Informe>() {

           @Override
           public Predicate toPredicate(
        		   Root<Informe> root
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
