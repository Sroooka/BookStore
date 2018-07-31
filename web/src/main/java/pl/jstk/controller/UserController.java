package pl.jstk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jstk.constants.ViewNames;

@Controller
public class UserController {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		//model.addAttribute(ModelConstants.BOOKLIST, bookList);
		// model.addAttribute(ModelConstants.INFO, INFO_TEXT);
		return ViewNames.LOGIN;
	}
}
