package pl.jstk.service;

import java.util.List;

import pl.jstk.enumerations.Pair;
import pl.jstk.to.BookTo;

public interface BookService {

    List<BookTo> findAllBooks();
    List<BookTo> findBooksByTitle(String title);
    List<BookTo> findBooksByAuthor(String author);
    Pair<List<BookTo>, String> findByCriteria(BookTo criteria);
    BookTo findBookByID(long id);
    Pair<Boolean, String> canAddBook(BookTo book);

    BookTo saveBook(BookTo book);
    void deleteBook(Long id);
}
