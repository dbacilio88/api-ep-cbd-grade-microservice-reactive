package pe.mil.microservices.repositories.contracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.mil.microservices.repositories.entities.GradesEntity;

@Repository
public interface IGradeRepository extends JpaRepository<GradesEntity, Long> {
    boolean existsByGradeId(Long gradeId);


}
