package pl.jstk.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class DeleteBookControllerTest {
	@Mock
	BookService bookService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	DeleteBookController deleteBookController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(bookService);
		Mockito.reset(bookService);
		mockMvc = MockMvcBuilders.standaloneSetup(new AddBookController(bookService)).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		ReflectionTestUtils.setField(deleteBookController, "bookService", bookService);
	}

	@Test
	@WithMockUser(username = "Sroka", roles = { "ADMIN" })
	public void testDeleteBookPage() throws Exception {
		// given when
		BookTo book = new BookTo();
		book.setTitle("Title");
		Mockito.when(bookService.findBookByID(Mockito.anyLong())).thenReturn(book);
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/books/delete").param("id", "1"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.BOOKS))
				.andDo(print())
				.andExpect(content().string(containsString("")));
	}

	@Test
	@WithMockUser(username = "Sroka", roles = { "ADMIN" })
	public void shouldDeleteBook() throws Exception {
		// given when
		BookTo book = new BookTo();
		book.setTitle("Title");
		Mockito.when(bookService.findBookByID(Mockito.anyLong())).thenReturn(book);
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/books/delete").param("id", "1"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.BOOKS))
				.andDo(print())
				.andExpect(model().attribute(ModelConstants.ADDBOOKINFOPOSITIVE,
						"Successfully deleted book [" + book.getTitle() + "]!"))
				.andExpect(content().string(containsString("")));
	}

	@Test
	@WithMockUser(username = "Sroka", roles = { "USER" })
	public void shouldThrowExceptionWhenBadRoleOnDeleting() throws Exception {
		// given
		BookTo book = new BookTo();
		book.setTitle("Title");
		Mockito.when(bookService.findBookByID(Mockito.anyLong())).thenReturn(book);
		String asJsonString = new ObjectMapper().writeValueAsString(book);
		// when
		ResultActions resultActions = mockMvc
				.perform(delete("/books/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString));
		// then
		resultActions.andExpect(status().is4xxClientError());
	}
}
