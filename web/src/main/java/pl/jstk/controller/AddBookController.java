package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.Pair;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
public class AddBookController {
	private BookService bookService;

	@Autowired
	AddBookController(BookService bookService) {
		this.bookService = bookService;
	}

	@Bean
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}

	@RequestMapping(value = "/books/add", method = RequestMethod.GET)
	public String addBook(Model model) {
		BookTo newBook = new BookTo();
		model.addAttribute(ModelConstants.NEWBOOK, newBook);
		return ViewNames.ADDBOOK;
	}

	@RequestMapping(value = "/greeting", method = RequestMethod.POST)
	public String showDetails(@ModelAttribute BookTo book, Model model) {
		Pair<Boolean, String> messageWithStatement = bookService.canAddBook(book);
		String message = messageWithStatement.getF2();
		Boolean canAddBook = messageWithStatement.getF1();
		if (canAddBook) {
			bookService.saveBook(book);
			BookTo newBook = new BookTo();
			model.addAttribute(ModelConstants.NEWBOOK, newBook);
			model.addAttribute(ModelConstants.ADDBOOKINFOPOSITIVE, message);
		} else {
			BookTo newBook = new BookTo();
			model.addAttribute(ModelConstants.NEWBOOK, newBook);
			model.addAttribute(ModelConstants.ADDBOOKINFONEGATIVE, message);
		}
		return ViewNames.ADDBOOK;
	}
}
