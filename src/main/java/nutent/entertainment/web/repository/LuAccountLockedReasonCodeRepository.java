package nutent.entertainment.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nutent.entertainment.web.entity.LuAccountLockedReasonCode;

@Repository
@Transactional(readOnly = true)
public interface LuAccountLockedReasonCodeRepository extends JpaRepository<LuAccountLockedReasonCode, Integer> {
	
	LuAccountLockedReasonCode findByCode(String reasonCode);
	
}
