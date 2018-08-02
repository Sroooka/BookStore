package pl.jstk.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.BookStatus;
import pl.jstk.enumerations.Pair;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class AddBookControllerTest {
	@Mock
	BookService bookService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	AddBookController addBookController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(bookService);
		Mockito.reset(bookService);
		mockMvc = MockMvcBuilders.standaloneSetup(new AddBookController(bookService)).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		ReflectionTestUtils.setField(addBookController, "bookService", bookService);
	}

	@Test
	public void testAddBookPage() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/add"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.ADDBOOK))
				.andDo(print())
				.andExpect(content().string(containsString("")));
	}

	@Test
	public void shouldAddBook() throws Exception {
		// given
		BookTo book = new BookTo((long) 1, "Some title", "Some author", BookStatus.FREE);
		Mockito.when(bookService.saveBook(Mockito.any())).thenReturn(book);
		Pair<Boolean, String> messageWithStatement = new Pair<>(true, "Message");
		Mockito.when(bookService.canAddBook(Mockito.any())).thenReturn(messageWithStatement);
		// when
		ResultActions resultActions = mockMvc.perform(post("/greeting"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.ADDBOOK))
				.andDo(print())
				.andExpect(model().attribute(ModelConstants.ADDBOOKINFOPOSITIVE, "Message"))
				.andExpect(content().string(containsString("")));
	}
}