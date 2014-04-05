package telefonica.aaee.informes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import telefonica.aaee.informes.model.condiciones.Acuerdo;

public interface AcuerdoRepository extends JpaRepository<Acuerdo, Long> {
	
}
