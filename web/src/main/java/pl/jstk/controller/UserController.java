package pl.jstk.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@Controller
public class UserController {

	

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", defaultValue = "false") String error, Model model) {
		// model.addAttribute(ModelConstants.ERROR, error);
		
		model.addAttribute(ModelConstants.ERROR, error);
		return ViewNames.LOGIN;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		// model.addAttribute(ModelConstants.ERROR, error);
		//model.addAttribute(ModelConstants.PARAMLOGOUT, logout);
		SecurityContextHolder.getContext().setAuthentication(null);
		return ViewNames.LOGIN;
	}
}
