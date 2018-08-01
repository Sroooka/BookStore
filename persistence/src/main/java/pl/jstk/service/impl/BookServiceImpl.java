package pl.jstk.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.jstk.entity.BookEntity;
import pl.jstk.enumerations.Pair;
import pl.jstk.mapper.BookMapper;
import pl.jstk.repository.BookRepository;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public List<BookTo> findAllBooks() {
		return BookMapper.map2To(bookRepository.findAll());
	}

	@Override
	public List<BookTo> findBooksByTitle(String title) {
		return BookMapper.map2To(bookRepository.findBookByTitle(title));
	}

	@Override
	public List<BookTo> findBooksByAuthor(String author) {
		return BookMapper.map2To(bookRepository.findBookByAuthor(author));
	}

	@Override
	@Transactional
	public BookTo saveBook(BookTo book) {
		BookEntity entity = BookMapper.map(book);
		entity = bookRepository.save(entity);
		return BookMapper.map(entity);
	}

	@Override
	@Transactional
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);

	}

	@Override
	public BookTo findBookByID(long id) {
		return BookMapper.map(bookRepository.getOne(id));

	}

	@Override
	public Pair<List<BookTo>, String> findByCriteria(BookTo criteria) {
		String message = "";

		List<BookTo> presentationList = new ArrayList<>();
		List<BookEntity> foundList = new ArrayList<>();
		List<BookEntity> foundByAuthor = new ArrayList<>();
		List<BookEntity> foundByTitle = new ArrayList<>();
		if (!criteria.getAuthors().isEmpty()) {
			foundByAuthor = bookRepository.findBookByAuthor(criteria.getAuthors());
		} else {
			foundByAuthor = bookRepository.findAll();
		}
		if (!criteria.getTitle().isEmpty()) {
			foundByTitle = bookRepository.findBookByTitle(criteria.getTitle());
		} else {
			foundByTitle = bookRepository.findAll();
		}

		foundList = foundByAuthor;
		foundList.retainAll(foundByTitle);
		presentationList = foundList.stream().map(BookMapper::map).collect(Collectors.toList());

		if (presentationList.isEmpty()) {
			message = "No books found!";
		} else {
			int foundBooksAmount = presentationList.size();
			message = (foundBooksAmount == 1) ? "1 book" : foundBooksAmount + " books";
			message += " found by your criteria!";
		}
		return new Pair<List<BookTo>, String>(presentationList, message);
	}
}
