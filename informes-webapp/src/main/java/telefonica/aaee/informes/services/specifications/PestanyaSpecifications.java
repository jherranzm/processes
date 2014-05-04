package telefonica.aaee.informes.services.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import telefonica.aaee.informes.model.Pestanya;

public abstract class PestanyaSpecifications {
	public static Specification<Pestanya> searchByNombre(final String name) {
        return new Specification<Pestanya>() {

           @Override
           public Predicate toPredicate(
        		   Root<Pestanya> root
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
