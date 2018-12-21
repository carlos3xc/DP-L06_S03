package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.WelcomeMessage;

import java.util.Collection;

@Repository
public interface WelcomeMessageRepository extends JpaRepository<WelcomeMessage, Integer>{

	@Query("select wm from WelcomeMessage wm where wm.languageCode like ?1")
	Collection<WelcomeMessage> findByLanguageCode(String languageCode);
}
