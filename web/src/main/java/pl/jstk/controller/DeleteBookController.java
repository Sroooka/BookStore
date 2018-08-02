package pl.jstk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.annotation.Secured;
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
public class DeleteBookController {
	private BookService bookService;

	@Autowired
	DeleteBookController(BookService bookService) {
		this.bookService = bookService;
	}

	@Bean
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/books/delete", method = RequestMethod.DELETE)
	public String deleteBook(@RequestParam(value = "id") long id, Model model) {
		String bookTitle = bookService.findBookByID(id).getTitle();
		bookService.deleteBook(id);
		List<BookTo> bookList = bookService.findAllBooks();
		model.addAttribute(ModelConstants.BOOKLIST, bookList);
		model.addAttribute(ModelConstants.ADDBOOKINFOPOSITIVE, 
				"Successfully deleted book [" + bookTitle + "]!");
		return ViewNames.BOOKS;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/books/clear", method = RequestMethod.DELETE)
	public String deleteAllBooks(Model model) {
		bookService.deleteAllBooks();
		List<BookTo> bookList = bookService.findAllBooks();
		model.addAttribute(ModelConstants.BOOKLIST, bookList);
		model.addAttribute(ModelConstants.ADDBOOKINFOPOSITIVE, 
				"Successfully deleted all books!");
		return ViewNames.BOOKS;
	}
}
