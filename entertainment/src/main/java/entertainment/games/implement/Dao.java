package entertainment.games.implement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Dao {
	@PersistenceContext
	protected EntityManager em;
}
