package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCardMake;

@Repository
public interface CreditCardMakeRepository extends JpaRepository<CreditCardMake, Integer>{

}
