package pl.jstk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.BookStatus;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
public class BookController {

	private BookService bookService;

	@Autowired
	BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String welcome(Model model) {
		List<BookTo> bookList = bookService.findAllBooks();
		model.addAttribute(ModelConstants.BOOKLIST, bookList);
		// model.addAttribute(ModelConstants.INFO, INFO_TEXT);
		return ViewNames.BOOKS;
	}

	@RequestMapping(value = "/books/book", method = RequestMethod.GET)
	public String showDetails(@RequestParam(value = "id") long id, Model model) {
		BookTo book = bookService.findBookByID(id);
		model.addAttribute(ModelConstants.BOOKINFO, book);
		return ViewNames.BOOK;
	}

	@RequestMapping(value = "/books/add", method = RequestMethod.GET)
	public String addBook(Model model) {
		BookTo newBook = new BookTo();
		model.addAttribute(ModelConstants.NEWBOOK, newBook);

		return ViewNames.ADDBOOK;
	}

	@RequestMapping(value = "/greeting", method = RequestMethod.POST)
	public String showDetails(@ModelAttribute BookTo book, Model model) {
		String emptyFields = "";
		boolean canAddBook = true;
		if (book.getAuthors().isEmpty()) {
			emptyFields += "Author ";
			canAddBook = false;
		}
		if (book.getTitle().isEmpty()) {
			emptyFields += "Title ";
			canAddBook = false;
		}
		if (book.getStatus() == null) {
			emptyFields += "Status ";
			canAddBook = false;
		}
		if (canAddBook) {
			bookService.saveBook(book);
			BookTo newBook = new BookTo();
			model.addAttribute(ModelConstants.NEWBOOK, newBook);
			String successMessage = "Successfully added Book: " + book.getAuthors() + " - " + book.getTitle()
					+ " [Status:" + book.getStatus().toString() + "].";
			model.addAttribute(ModelConstants.ADDBOOKINFOPOSITIVE, successMessage);
		} else {
			BookTo newBook = new BookTo();
			model.addAttribute(ModelConstants.NEWBOOK, newBook);
			String errorMessage = "Error adding book! Please fill fields: " + emptyFields;
			model.addAttribute(ModelConstants.ADDBOOKINFONEGATIVE, errorMessage);
		}

		return ViewNames.ADDBOOK;
	}

	@RequestMapping(value = "/books/search", method = RequestMethod.GET)
	public String searchBooks(Model model) {
		BookTo newBook = new BookTo();
		model.addAttribute(ModelConstants.NEWBOOK, newBook);
		return ViewNames.SEARCH;
	}

	@RequestMapping(value = "/searching", method = RequestMethod.POST)
	public String showFoundBooks(@ModelAttribute BookTo criteria, Model model) {

		if (criteria.getAuthors().isEmpty() && criteria.getTitle().isEmpty()) {
			List<BookTo> bookList = bookService.findAllBooks();
			model.addAttribute(ModelConstants.ADDBOOKINFONEGATIVE, "No criteria - showing all books!");
			model.addAttribute(ModelConstants.BOOKLIST, bookList);
			return ViewNames.BOOKS;
		}

		// TODO PRZERZUC DO SERWISU
		List<BookTo> presentationList = new ArrayList<>();
		List<BookTo> foundByAuthor = new ArrayList<>();
		List<BookTo> foundByTitle = new ArrayList<>();
		if (!criteria.getAuthors().isEmpty()) {
			foundByAuthor = bookService.findBooksByAuthor(criteria.getAuthors());
		}
		if (!criteria.getTitle().isEmpty()) {
			foundByTitle = bookService.findBooksByTitle(criteria.getTitle());
		}

		for (BookTo book : foundByAuthor) {
			if (!presentationList.contains(book)) {
				presentationList.add(book);
			}
		}
		for (BookTo book : foundByTitle) {
			if (!presentationList.contains(book)) {
				presentationList.add(book);
			}
		}

		if (presentationList.isEmpty()) {
			model.addAttribute(ModelConstants.ADDBOOKINFONEGATIVE, "No books found!");
			model.addAttribute(ModelConstants.BOOKLIST, null);
		} else {
			int foundBooksAmount = presentationList.size();
			String message = (foundBooksAmount == 1) ? "1 book" : foundBooksAmount + " books";
			message += " found by your criteria!";
			model.addAttribute(ModelConstants.ADDBOOKINFOPOSITIVE, message);
			model.addAttribute(ModelConstants.BOOKLIST, presentationList);
		}
		return ViewNames.BOOKS;
	}

	@RequestMapping(value = "/books/delete", method = RequestMethod.GET)
	public String deleteBook(@RequestParam(value = "id") long id, Model model) {
		String bookTitle = bookService.findBookByID(id).getTitle();
		bookService.deleteBook(id);
		List<BookTo> bookList = bookService.findAllBooks();
		model.addAttribute(ModelConstants.BOOKLIST, bookList);
		model.addAttribute(ModelConstants.ADDBOOKINFOPOSITIVE, "Successfully deleted book [" + bookTitle + "]!");
		return ViewNames.BOOKS;
	}
}
