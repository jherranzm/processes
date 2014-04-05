package telefonica.aaee.informes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import telefonica.aaee.informes.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
	
}
