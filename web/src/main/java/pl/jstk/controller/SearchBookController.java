package pl.jstk.controller;

import java.util.List;

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
public class SearchBookController {
	private BookService bookService;
	private BooksController booksController;

	@Autowired
	SearchBookController(BookService bookService, BooksController booksController) {
		this.bookService = bookService;
		this.booksController = booksController;
	}

	@Bean
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}

	@RequestMapping(value = "/books/search", method = RequestMethod.GET)
	public String searchBooks(Model model) {
		BookTo newBook = new BookTo();
		model.addAttribute(ModelConstants.NEWBOOK, newBook);
		return ViewNames.SEARCH;
	}

	@RequestMapping(value = "/searching", method = RequestMethod.POST)
	public String showFoundBooks(@ModelAttribute("criteria") BookTo criteria, Model model) {
		if (criteria.getAuthors().isEmpty() && criteria.getTitle().isEmpty()) {
			model.addAttribute(ModelConstants.ADDBOOKINFONEGATIVE, "No criteria - showing all books!");
			return booksController.welcome(model);
		}
		Pair<List<BookTo>, String> listWithMessage = bookService.findByCriteria(criteria);
		List<BookTo> presentationList = listWithMessage.getF1();
		String message = listWithMessage.getF2();
		if (presentationList.isEmpty()) {
			model.addAttribute(ModelConstants.ADDBOOKINFONEGATIVE, message);
			model.addAttribute(ModelConstants.BOOKLIST, null);
		} else {
			model.addAttribute(ModelConstants.ADDBOOKINFOPOSITIVE, message);
			model.addAttribute(ModelConstants.BOOKLIST, presentationList);
		}
		return ViewNames.BOOKS;
	}
}
