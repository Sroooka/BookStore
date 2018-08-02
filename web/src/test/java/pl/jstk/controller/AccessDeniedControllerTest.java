package pl.jstk.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class AccessDeniedControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new AccessDeniedController()).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@WithMockUser
	public void testAccessDeniedPage() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(delete("/403"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.FOURHUNDREDTHREE))
				.andDo(print())
				.andExpect(model().attributeExists(ModelConstants.MESSAGE403))
				.andExpect(content().string(containsString("")));
	}

	@Test
	@WithMockUser(username = "Heniek", roles = { "USER" })
	public void shouldReturnCustomUserMessage() throws Exception {
		// given
		String message = "You have not enough power to do this!";
		// when
		ResultActions resultActions = mockMvc.perform(delete("/403"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(model().attribute(ModelConstants.MESSAGE403, message));
	}
}
