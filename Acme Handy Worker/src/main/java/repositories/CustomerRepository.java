package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	// no es necesario viene por defecto esta como referencia
	@Query("select a from Customer a where a.id = ?1") 
	Customer findOne(Integer Id);
	
	@Query("select c from Customer c where c.userAccount.id = ?1") 
	Customer findByUserAccountId(Integer Id);
	
	@Query("select c1 from Customer c1 where c1.fixUpTasks.size >= (select avg(c2.fixUpTasks.size)*1.1 from Customer c2) order by c1.fixUpTasks.size desc")
	Collection<Customer> getCustomersWMoreTasksThanAvg();
}
