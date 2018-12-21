package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Service
@Transactional
public class MessageService {

	// Managed Repository -----
	@Autowired
	private MessageRepository messageRepository;

	// Supporting Services -----

	@Autowired
	private BoxService boxService;

	@Autowired
	private WordService wordService;

	@Autowired
	private ActorService actorService;

	// Simple CRUD methods -----
	public Message create() {

		Message message = new Message();
		Actor sender = actorService.findByPrincipal();

		message.setMoment(new Date (System.currentTimeMillis()-1000));
		message.setPriority("NEUTRAL");
		message.setSender(sender);
		message.setTags(new ArrayList<String>());
		message.setRecipients(new ArrayList<Actor>());

		return message;
	}

	public Collection<Message> findAll() {
		return messageRepository.findAll();
	}

	public Message findOne(int Id) {
		return messageRepository.findOne(Id);
	}

	public Message save(Message message) {
		Message result;
		Actor principal = actorService.findByPrincipal();
		Date moment = new Date(System.currentTimeMillis() - 1000);

		message.setMoment(moment);
		Assert.isTrue(principal.equals(message.getSender()));

		result = messageRepository.save(message);
		addToBoxes(result);
		return result;
	}

	public void delete(Message message){
		Assert.isTrue(boxService.findByMessage(message).isEmpty());
		messageRepository.delete(message.getId());
	}

	public void copy(Message message, Box box){
		Collection<Box> principalBoxes = boxService.findByPrincipal();
		Box boxToAddMessage = boxService.findOne(box.getId());

		Assert.isTrue(principalBoxes.contains(boxToAddMessage));
		Assert.isTrue(!boxToAddMessage.getMessages().contains(message));
		Collection<Message> messages = new HashSet<Message>(box.getMessages());
		messages.add(message);
		boxToAddMessage.setMessages(messages);
	}

	public void move(Message message, Box oldBox, Box newBox){
		Collection<Box> principalBoxes = boxService.findByPrincipal();
		Box boxToRemoveMessage = boxService.findOne(oldBox.getId());

		Assert.isTrue(principalBoxes.contains(boxToRemoveMessage));
		copy(message,newBox);

		Collection<Message> messages = new HashSet<Message>(boxToRemoveMessage.getMessages());
		messages.remove(message);
		boxToRemoveMessage.setMessages(messages);
	}

	public void delete(Message message, Box box){
		Actor principal = actorService.findByPrincipal();
		Collection<Box> principalBoxes = boxService.findByPrincipal();
		Box boxToRemoveMessage = boxService.findOne(box.getId());

		Assert.isTrue(principalBoxes.contains(boxToRemoveMessage));
		Box principalTrashBox = boxService.findByPrincipalAndName("Trash Box");
		/*The message is in the principal's trash box*/
		if(principalTrashBox.equals(boxToRemoveMessage)){
			Collection<Box> boxesToRemove = new HashSet<Box>(principalBoxes);
			/*The message is ONLY in principal's boxes*/
			if(boxService.findByMessage(message).size()
					==boxService.findByActorAndMessage(principal,message).size()){
				removeFromBoxes(message, boxesToRemove);
				delete(message);
				/*The message is other actors' boxes and in principal's boxes*/
			}else{
				boxesToRemove.remove(principalTrashBox);
				removeFromBoxes(message,boxesToRemove);
			}
		/*The message was not in the principal's trash box so we move it to the trash box*/
		}else{
			move(message,boxToRemoveMessage,principalTrashBox);
		}
	}

	private Boolean isSpam(Message message){
		Boolean result = false;
		Collection<Word> spamWords = wordService.findSpamWords();
		for(Word spamWord: spamWords){
			result = result
					|| message.getSubject().contains(spamWord.getContent())
					|| message.getBody().contains(spamWord.getContent());
			if (result){
				break;
			}
		}
		return result;
	}

	private void addToBoxes(Message message){
		Collection<Box> boxes = new HashSet<Box>();
		Box outBox = boxService
				.findByActorAndName(message.getSender(),"Out Box");

		boxes.add(outBox);
		if (isSpam(message)){
			for (Actor actor: message.getRecipients()){
				Box spamBox = boxService.findByActorAndName(actor,"Spam Box");
				boxes.add(spamBox);
			}
		}else{
			for (Actor actor: message.getRecipients()){
				Box inBox = boxService.findByActorAndName(actor,"In Box");
				boxes.add(inBox);
		}
	}

		for (Box box: boxes){
			Collection<Message> messages = new HashSet<Message>(box.getMessages());
			messages.add(message);
			box.setMessages(messages);
		}
	}

	private void removeFromBoxes(Message message, Collection<Box> boxes){
		for (Box box: boxes){
			Collection<Message> messages = new HashSet<Message>(box.getMessages());
			messages.remove(message);
			box.setMessages(messages);
		}
	}
}