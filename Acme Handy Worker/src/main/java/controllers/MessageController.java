package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.BoxService;
import services.MessageService;
import domain.Box;
import domain.Message;

@Controller
@RequestMapping("message/")
public class MessageController extends AbstractController {
	
	//Services
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public MessageController() {
		super();
	}
	
	//Listing-----------------------------------------------------------


	@RequestMapping(value="/list" , method=RequestMethod.GET)
	public ModelAndView list(@RequestParam int boxId) {
		ModelAndView res;
		boxService.findOne(boxId).getMessages();
		

		res = new ModelAndView("message/list");
		res.addObject("messages", boxService.findOne(boxId).getMessages());

		return res;
	}
	
	
	//Create-----------------------------------------------------------
	@RequestMapping(value="/create" , method=RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView res;
		Message message = messageService.create(actorService.getByUserAccountId(LoginService.getPrincipal()));
		
		res = this.createEditModelAndView(message);
		return res;
		
	}
	
	//Save-------------------------------------------------------------
	
	//TODO: no va a recibir un message si no un messageform que luego se convertira en message.
	
	@RequestMapping(value="/edit", method=RequestMethod.POST, params="save")
	public ModelAndView save(@Valid Message message, BindingResult binding){
		ModelAndView res;
		
		System.out.println(message.getBody()+" "+message.getSubject()+" "+ 
		message.getRecipient().getUserAccount().getUsername()
		+message.getSender().getUserAccount().getUsername());
		
		
		
		
		if(binding.hasErrors()){
			res = createEditModelAndView(message);
		}else{
			try {
				Message saved = messageService.save(message);
				System.out.println("se guarda  el message");
				
				messageService.addMesageToBoxes(saved);
				System.out.println("se a�ade a las boxes");
				
				int boxId = 0;
				Collection<Box> boxes = boxService.findAll();
				for (Box b : boxes) {
					if(b.getMessages().contains(saved))
					boxId=b.getId();
					System.out.println("se ha encontrado el box pertinente"+ boxId);
				}
				
				res = new ModelAndView("redirect:list.do?boxId="+boxId);
			} catch (Throwable e) {
				res = createEditModelAndView(message, "message.commit.error");
				System.out.println(e);
				
			}
		}
		return res;
	}
	
	
	//Delete-----------------------------------------------------------
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public ModelAndView delete(@RequestParam int messageId){
		ModelAndView res;
		Message message = messageService.findOne(messageId);
		
		int boxId = 0;
		Collection<Box> boxes = boxService.findAll();
		for (Box b : boxes) {
			if(b.getMessages().contains(message))
			boxId=b.getId();
		}
		
		
			try {
				messageService.delete(message);
				res = new ModelAndView("redirect:list.do?boxId="+boxId);
			} catch (Throwable e) {
				res = createEditModelAndView(message, "message.commit.error");
			}
		return res;
	}
	
	
	//Helper methods---------------------------------------------------
	protected ModelAndView createEditModelAndView(Message message){
		ModelAndView res;
		res = createEditModelAndView(message, null);
		return res;
	}
	protected ModelAndView createEditModelAndView(Message message, String messageCode){
		ModelAndView res;
		
		//aqui habria que contemplar si el mensaje esta siendo editado o creado
		// y a�adir metodos en consecuencia, pero como los mensajes no pueden ser 
		//editados no es necesario
		
		res = new ModelAndView("message/edit");
		res.addObject("userMessage", message);
		res.addObject("actors",actorService.findAll());
		res.addObject("message", messageCode);
		
		return res;
	}
	
}