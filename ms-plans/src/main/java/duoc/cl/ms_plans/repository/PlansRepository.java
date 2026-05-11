package duoc.cl.ms_plans.repository;

import duoc.cl.ms_plans.model.Plans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlansRepository extends JpaRepository<Plans, Long> {
}
