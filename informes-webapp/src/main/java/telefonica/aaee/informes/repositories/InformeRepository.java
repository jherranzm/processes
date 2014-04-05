package telefonica.aaee.informes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import telefonica.aaee.informes.model.Informe;

public interface InformeRepository extends JpaRepository<Informe, Long> {
	
}
