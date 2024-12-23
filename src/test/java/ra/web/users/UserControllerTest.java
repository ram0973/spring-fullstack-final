package ra.web.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ra.web.page.users.User;
import ra.web.page.users.UserMapper;
import ra.web.page.users.UserRepository;
import ra.web.page.users.dto.UsersResponse;
import ra.web.util.ModelGenerator;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @BeforeEach
    public void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void testGetUsers() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk())
            .andReturn().getResponse();
        String body = response.getContentAsString();
        //UsersResponse userDTOS = om.readValue(body, new TypeReference<>() {});
        //List<User> actual = userDTOS.users().stream().map(userMapper::map).toList();
        //var expected = userRepository.findAll();
        //Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testCreate() throws Exception {
        //User user = modelGenerator.getUserModel();
//        MockHttpServletRequestBuilder request = post("/api/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(om.writeValueAsString(user));
//        mockMvc
//            .perform(request)
//            .andExpect(status().isCreated());
//        User savedUser = userRepository.findByEmailIgnoreCase(user.getEmail()).orElseThrow();
//        assertThat(savedUser.getFirstName()).isEqualTo(user.getFirstName());
//        assertThat(savedUser.getLastName()).isEqualTo(user.getLastName());
    }
}
