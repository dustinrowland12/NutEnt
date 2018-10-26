package entertainment.games.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entertainment.games.entity.LuAccountLockedReasonCode;

@Repository
@Transactional(readOnly = true)
public interface LuAccountLockedReasonCodeRepository extends JpaRepository<LuAccountLockedReasonCode, Integer> {
	
	LuAccountLockedReasonCode findByCode(String reasonCode);
	
}
