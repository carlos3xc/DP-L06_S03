package services;

import domain.Category;
import domain.FixUpTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.CategoryRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

import java.util.Collection;


@Service
@Transactional
public class CategoryService {

	//Managed Repository -----
	@Autowired
	private CategoryRepository categoryRepository;
	
	//Supporting Services -----
	
	@Autowired
	private FixUpTaskService taskService;
	
	//Simple CRUD methods -----
	
	public Category create(){
		Category res = new Category();
		
		return res;
	}
	
	public Collection<Category> findAll(){
		return categoryRepository.findAll();
	}
	
	public Category findOne(int Id){
		return categoryRepository.findOne(Id);
	}
	
	public Category save(Category a){
		UserAccount userAccount = LoginService.getPrincipal();
		Authority au = new Authority();
		au.setAuthority("ADMIN");
		Assert.isTrue(userAccount.getAuthorities().contains(au));
		
		return categoryRepository.save(a);
	}
	
	public void delete(Category a){
		UserAccount userAccount = LoginService.getPrincipal();
		Authority au = new Authority();
		au.setAuthority("ADMIN");
		Assert.isTrue(userAccount.getAuthorities().contains(au));
		
		for(FixUpTask f:categoryRepository.listTaskByCategory(a.getId())){ //Hacerlo mejor con una query
			f.setCategory(a.getParentCategory());
			taskService.saveAdmin(f);
		}
		
		categoryRepository.delete(a);
	}
	
	//Other business methods -----
	
	
}