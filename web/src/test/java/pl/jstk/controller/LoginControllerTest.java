package pl.jstk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class LoginControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	LoginController loginController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new LoginController()).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@WithMockUser(username = "Sroka", roles = { "ADMIN" })
	public void shouldLoginCorrect() throws Exception {
		// given when
		ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.get("/login").param("error", "false"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.LOGIN));
	}

	@Test
	@WithMockUser(username = "Sroka", roles = { "ADMIN" })
	public void shouldShowInfoAboutBadCredentials() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/login").param("error", "true"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.LOGIN))
				.andExpect(model().attribute(ModelConstants.ERROR, "true"));
	}

	@Test
	public void shouldLoginAsAdmin() throws Exception {
		// when
		ResultActions resultActions = mockMvc
				.perform(get("/login").param("username", "admin").param("password", "admin"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(model().attribute(ModelConstants.ERROR, "false"));
	}

	@Test
	public void shouldLoginAsUser() throws Exception {
		// when
		ResultActions resultActions = mockMvc
				.perform(get("/login").param("username", "user").param("password", "user"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(model().attribute(ModelConstants.ERROR, "false"));
	}
}
