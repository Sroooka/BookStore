package pl.jstk.controller;

import java.security.Principal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@Controller
public class UserController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", defaultValue = "false") String error, Model model) {

		model.addAttribute(ModelConstants.ERROR, error);
		return ViewNames.LOGIN;
	}

	@RequestMapping(value = "/403", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE })
	public ModelAndView accesssDenied(Principal user) {
		ModelAndView model = new ModelAndView();
		if (user != null) {
			model.addObject("msg", "Dear " + user.getName() + ", you have not enough power to do this!");
		} else {
			model.addObject("msg", "You have not enough power to do this!");
		}
		model.setViewName("403");
		return model;
	}
}
