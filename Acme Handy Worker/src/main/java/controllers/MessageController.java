package controllers;

import javax.validation.Valid;

import domain.Box;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BoxService;
import services.MessageService;
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

	//Create-----------------------------------------------------------
	@RequestMapping(value="/create" , method=RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView res;
		Message message = messageService.create();

		res = this.createEditModelAndView(message);
		return res;

	}

	//Send--------------------------------------------------------------
	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "save")
	public ModelAndView send(@Valid Message userMessage, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(userMessage);
		} else {
			try {
				messageService.save(userMessage);

				Box principalOutbox = boxService.findByActorAndName(actorService.findByPrincipal(),"Out Box");
				result = new ModelAndView("redirect:/box/display.do?boxId=" + principalOutbox.getId());
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = createEditModelAndView(userMessage, "message.commit.error");
			}
		}
		return result;
	}
	//Helper methods---------------------------------------------------
	protected ModelAndView createEditModelAndView(Message message){
		ModelAndView res;
		res = createEditModelAndView(message, null);
		return res;
	}
	protected ModelAndView createEditModelAndView(Message userMessage, String messageCode){
		ModelAndView res;

		res = new ModelAndView("message/edit");
		res.addObject("userMessage", userMessage);
		res.addObject("actors",actorService.findAll());
		res.addObject("message", messageCode);

		return res;
	}
}