package services;

import domain.Actor;
import domain.Box;
import domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.BoxRepository;
import security.LoginService;
import security.UserAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class BoxService {

	@Autowired
	private BoxRepository boxRepository;

	@Autowired
	private ActorService actorService;

	public Box create(Actor actor) {

		UserAccount userAccount = LoginService.getPrincipal();

		Assert.isTrue(actor.getUserAccount().equals(userAccount));

		Box box = new Box();

		box.setActor(actor);
		box.setSystemBox(false);
		box.setMessages(new ArrayList<Message>());

		return box;
	}

	public Collection<Box> findAll() {
		return boxRepository.findAll();
	}

	public Box findOne(int Id) {
		return boxRepository.findOne(Id);
	}

	public Collection<Box> findByActor(Actor actor) {
		return boxRepository.findByActorId(actor.getId());
	}

	public Collection<Box> findByPrincipal(){
		Actor princial = actorService.findByPrincipal();
		return findByActor(princial);
	}

	public Box findByPrincipalAndName(String boxName){
		Actor principal = actorService.findByPrincipal();
		return findByActorAndName(principal,boxName);
	}

	public Box save(Box box) {

		Box result;

		result = boxRepository.save(box);
		return result;
	}

	public void delete(Box box) {

		Assert.isTrue(box.getSystemBox().equals(false));

		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(box.getActor().getUserAccount().equals(userAccount));

		boxRepository.delete(box);
	}

	// Other business methods -----

	public Collection<Box> findByActorId(Integer actorId) {
		return boxRepository.findByActorId(actorId);
	}

	public Box createUserBox(Actor actor) {

		Box box = this.create(actor);

		box.setName(box.getActor().getName());
		this.save(box);

		return box;
	}

	public void addMessageToBox(Box box, Message message) {
		System.out.println("se recive el box y el mensaje.");

		List<Message> aux = new ArrayList<>(box.getMessages());

		aux.add(0, message); // los mensajes nuevos siempre se ponen primero.
		box.setMessages(aux);

		this.save(box);
		System.out.println("se ejecuta el save de box");
	}

	public Collection<Box> findByMessage(Message message){
		return boxRepository.findByMessageId(message.getId());
	}

	public Collection<Box> findByActorAndMessage(Actor actor, Message message){
		return boxRepository.findByActorIdAndMessageId(actor.getId(),message.getId());
	}

	public Box findByActorAndName(Actor actor, String boxName){
		Assert.notNull(actor);
		return boxRepository.findByActorIdAndName(actor.getId(), boxName);
	}
}