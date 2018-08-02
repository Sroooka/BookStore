package pl.jstk.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
public class BooksController {
	private BookService bookService;

	@Autowired
	BooksController(BookService bookService) {
		this.bookService = bookService;
	}

	@Bean
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String welcome(Model model) {
		List<BookTo> bookList = bookService.findAllBooks();
		model.addAttribute(ModelConstants.BOOKLIST, bookList);
		return ViewNames.BOOKS;
	}
}
