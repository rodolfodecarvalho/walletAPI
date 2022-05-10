package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.entity.User;
import com.wallet.util.enums.RoleEnum;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    private static final String EMAIL = "email@teste.com";

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setUp() {
	User user = new User();
	user.setName("Set user");
	user.setPassword("Senha123");
	user.setEmail(EMAIL);
	user.setRole(RoleEnum.ROLE_ADMIN);

	repository.save(user);
    }

    @AfterEach
    public void tearDown() {
	repository.deleteAll();
    }

    @Test
    void testSave() {
	User user = new User();
	user.setName("Teste");
	user.setPassword("123456");
	user.setEmail("teste@teste.com");
	user.setRole(RoleEnum.ROLE_ADMIN);

	User response = repository.save(user);

	assertNotNull(response);
    }

    @Test
    public void testFindByEmail() {
	Optional<User> response = repository.findByEmailEquals(EMAIL);

	assertTrue(response.isPresent());
	assertEquals(response.get().getEmail(), EMAIL);
    }
}