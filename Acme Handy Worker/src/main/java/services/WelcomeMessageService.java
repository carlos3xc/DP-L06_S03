package services;

import domain.WelcomeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.WelcomeMessageRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

import java.util.Collection;

@Service
@Transactional
public class WelcomeMessageService {

	@Autowired
	private WelcomeMessageRepository welcomeMessageRepository;

	public WelcomeMessage create() {

		WelcomeMessage res = new WelcomeMessage();
		return res;
	}

	public Collection<WelcomeMessage> findAll() {
		return welcomeMessageRepository.findAll();
	}

	public WelcomeMessage findOne(int Id) {
		return welcomeMessageRepository.findOne(Id);
	}

	public WelcomeMessage save(WelcomeMessage welcomeMessage) {

		Authority authority = new Authority();
		authority.setAuthority("ADMIN");

		WelcomeMessage result;

		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains(authority));

		result = welcomeMessageRepository.save(welcomeMessage);
		return result;
	}

	public WelcomeMessage findByLanguageCode(String languageCode){
		return welcomeMessageRepository.findByLanguageCode(languageCode).iterator().next();
	}

	public WelcomeMessage findByLocaleContextHolder(){
		String languageCode = LocaleContextHolder.getLocale().getLanguage().toLowerCase();
		return findByLanguageCode(languageCode);
	}

}