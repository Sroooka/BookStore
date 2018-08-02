package pl.jstk.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.Pair;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class SearchBookControllerTest {

	@Mock
	BookService bookService;

	@Mock
	BooksController booksController;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	SearchBookController searchBookController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(bookService);
		Mockito.reset(bookService);
		mockMvc = MockMvcBuilders.standaloneSetup(new SearchBookController(bookService, booksController)).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		ReflectionTestUtils.setField(booksController, "bookService", bookService);
	}

	@Test
	public void testSearchBooksPage() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/search"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.SEARCH))
				.andDo(print())
				.andExpect(content().string(containsString("")));
	}

	@Test
	public void shouldSearchByCriteria() throws Exception {
		// given
		BookTo criteria = new BookTo(1L, "First book", "", null);
		Mockito.when(bookService.findBookByID(Mockito.anyLong())).thenReturn(criteria);
		List<BookTo> books = new ArrayList<>();
		books.add(new BookTo());
		books.add(new BookTo());
		books.add(new BookTo());
		Pair<List<BookTo>, String> listWithMessage = new Pair<List<BookTo>, String>(books, "message");
		Mockito.when(bookService.findByCriteria(Mockito.any())).thenReturn(listWithMessage);
		// when
		ResultActions resultActions = mockMvc
						.perform(post("/searching")
						.contentType(MediaType.APPLICATION_JSON)
						.flashAttr("criteria", criteria));
		// when
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.BOOKS))
				.andDo(print())
				.andExpect(model().attribute(ModelConstants.ADDBOOKINFOPOSITIVE, "3 books found by your criteria!"))
				.andExpect(content().string(containsString("")));
	}
}