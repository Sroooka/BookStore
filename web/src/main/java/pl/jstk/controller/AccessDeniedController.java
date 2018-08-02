package pl.jstk.controller;

import java.security.Principal;

import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@Controller
public class AccessDeniedController {
	@Bean
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}

	@RequestMapping(value = "/403", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE })
	public String accesssDenied(Principal user, Model model) {
		if (user != null) {
			model.addAttribute(ModelConstants.MESSAGE403,
					"Dear " + user.getName() + ", you have not enough power to do this!");
		} else {
			model.addAttribute(ModelConstants.MESSAGE403, 
					"You have not enough power to do this!");
		}
		return ViewNames.FOURHUNDREDTHREE;
	}
}
