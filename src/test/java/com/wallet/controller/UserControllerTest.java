package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class UserControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "User Test";
    private static final String PASSWORD = "123456";
    private static final String EMAIL = "email@teste.com";
    private static final String URL = "/user";

    @MockBean
    UserService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testSave() throws Exception {

	BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());

	mockMvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload(ID, NAME, PASSWORD, EMAIL)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated()).andExpect(jsonPath("$.data.id").value(ID)).andExpect(jsonPath("$.data.name").value(NAME)).andExpect(jsonPath("$.data.password").doesNotExist())
		.andExpect(jsonPath("$.data.email").value(EMAIL));
    }

    @Test
    void testSaveInvalidUser() throws Exception {

	BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());

	mockMvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload(ID, NAME, PASSWORD, "email")).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors[0]").value("Email inválido."));
    }

    User getMockUser() {
	User user = new User();

	user.setId(ID);
	user.setName(NAME);
	user.setPassword(PASSWORD);
	user.setEmail(EMAIL);

	return user;
    }

    String getJsonPayload(Long id, String name, String password, String email) throws JsonProcessingException {
	UserDTO dto = new UserDTO();

	dto.setId(id);
	dto.setName(name);
	dto.setPassword(password);
	dto.setEmail(email);

	ObjectMapper mapper = new ObjectMapper();
	return mapper.writeValueAsString(dto);
    }
}