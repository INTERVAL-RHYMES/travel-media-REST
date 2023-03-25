package com.example.travelmediarest.controller;

import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.model.Post;
import com.example.travelmediarest.model.User;
import com.example.travelmediarest.repository.LocationRepository;
import com.example.travelmediarest.repository.PostRepository;
import com.example.travelmediarest.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WithMockUser
//@MockMvcWithUser
//@WebMvcTest(HomeController.class)
class HomeControllerTest {

    //    @Autowired
    public static MockMvc mockMvc;

    @BeforeAll
    public static void init(@Autowired WebApplicationContext context, @Autowired LocationRepository locationRepository, @Autowired PasswordEncoder passwordEncoder,
                            @Autowired PostRepository postRepository, @Autowired UserRepository userRepository) {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        User user = new User("a@a.a", passwordEncoder.encode("a"), "Himon");
        Location location = new Location("Saudi");
        Post post = new Post(user, "enjoy private", location, "private");

        locationRepository.save(location);
        userRepository.save(user);
        postRepository.save(post);
    }

    @WithMockUser("a@a.a")
    @Test
//    @Disabled
    public void getHomePageTest() throws Exception {
        mockMvc.perform(get("/home")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("enjoy private")));

        mockMvc.perform(MockMvcRequestBuilders.get("/home").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].user").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].user.role").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].user.authorities[*].authority").value("ROLE_USER"));
    }

    @WithMockUser("a@a.a")
    @Test
    public void GoProfilePageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/home/profile").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].user").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].user.role").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].user.authorities[*].authority").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].privacy").value("private"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDtoList[*].user.mail").value("a@a.a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username.username").value("a@a.a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username.authorities[*].authority").value("ROLE_USER"));
    }


    @WithMockUser("a@a.a")
    @Test
    public void testCreateRetrieveWithMockMVC() throws Exception {
//        this.mockMvc.perform(get("/home")).andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/post/edit/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsStringIgnoringCase("postDto")));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/post/pined/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.post").exists());
    }

}

//@WebMvcTest - for testing the controller layer
//@JsonTest - for testing the JSON marshalling and unmarshalling
//@DataJpaTest - for testing the repository layer
//@RestClientTests - for testing REST clients