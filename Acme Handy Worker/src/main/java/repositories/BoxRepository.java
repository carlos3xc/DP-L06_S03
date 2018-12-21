package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer>{

	//Bussines methods-----
	
	@Query("select b from Box b where b.actor.id = ?1")
	Collection<Box> findByActorId(Integer actorId);

	@Query("select b from Box b, Message m " +
			"join b.actor own join b.messages " +
				"where (m.id = ?1) " +
				"and (own member of m.recipients or own.id = m.sender.id)")
	Collection<Box> findByMessageId(int messageId);

	@Query("select b from Box b, Message m join b.actor own " +
			"join b.messages where own.id = ?1 and m.id = ?2")
	Collection<Box> findByActorIdAndMessageId(int actorId, int messageId);

	@Query("select b from Box b where b.actor.id = ?1 and b.name like ?2")
	Box findByActorIdAndName(int actorId, String boxName);
}
