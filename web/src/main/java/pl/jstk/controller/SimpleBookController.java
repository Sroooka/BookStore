package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
public class SimpleBookController {
	private BookService bookService;

	@Autowired
	SimpleBookController(BookService bookService) {
		this.bookService = bookService;
	}

	@Bean
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}

	@RequestMapping(value = "/books/book", method = RequestMethod.GET)
	public String showDetails(@RequestParam(value = "id") long id, Model model) {
		BookTo book = bookService.findBookByID(id);
		model.addAttribute(ModelConstants.BOOKINFO, book);
		return ViewNames.BOOK;
	}
}
