package com.example.travelmediarest.controller;

import com.example.travelmediarest.dto.PostDto;
import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.model.Post;
import com.example.travelmediarest.model.User;
import com.example.travelmediarest.repository.LocationRepository;
import com.example.travelmediarest.repository.PostRepository;
import com.example.travelmediarest.repository.UserRepository;
import com.example.travelmediarest.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    public static MockMvc mockMvc;
    public static PostDto postDto;

    @Autowired
    public PostRepository postRepository;
    @Autowired
    public PostService postService;

    @BeforeAll
    public static void init(@Autowired WebApplicationContext context, @Autowired LocationRepository locationRepository,
                            @Autowired PasswordEncoder passwordEncoder, @Autowired PostRepository postRepository, @Autowired UserRepository userRepository) {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        User user = new User("a@a.a", passwordEncoder.encode("a"), "Himon");
        Location location = new Location("Dhaka");
        postDto = new PostDto(null, null, "enjoy private", location.getName(), "private", null);

//        locationRepository.save(location);
        userRepository.save(user);
//        postRepository.save(post);
    }

    private static String asJsonString(PostDto postDto) {
        try {
            log.info("new ObjectMapper().writeValueAsString(post): : " + new ObjectMapper().writeValueAsString(postDto));
            return new ObjectMapper().writeValueAsString(postDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @WithMockUser("a@a.a")
    @Test
    public void createPostTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/post/create")
                        .content(asJsonString(postDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.authorities[*].authority").value("ROLE_USER"));
    }

    @WithMockUser("a@a.a")
    @Test
    public void editPostPageTest() throws Exception {
        Post post = postService.saveThisPost(postDto, "a@a.a");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/post/edit/{id}",post.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.user").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.user.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.status").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.location").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.privacy").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.locationList").exists());
    }

    @WithMockUser("a@a.a")
    @Test
    public void editSubmitPostTest() throws Exception {
        Post post = postService.saveThisPost(postDto, "a@a.a");

        postDto.setId(post.getId());
        postDto.setStatus("updated");
        postDto.setPrivacy("private");
        postDto.setLocation("Dhaka");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/post/edit")
                        .content(asJsonString(postDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.user").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.user.authorities[*].authority").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto.user.mail").exists());
    }

    @WithMockUser("a@a.a")
    @Test
    public void pinedPostTest() throws Exception {
        Post post = postService.saveThisPost(postDto, "a@a.a");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/post/pined/{id}", post.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.post").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.post.user").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.post.pined").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.post.pined").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.post.pined").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.post.user.authorities[*].authority").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.post.user.mail").exists());
    }

    @WithMockUser("a@a.a")
    @Test
    public void deletePostTest() throws Exception {
        Post post = postService.saveThisPost(postDto, "a@a.a");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/post/delete/{id}", post.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.delete").exists());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/post/edit/{id}",post.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postDto").isEmpty());
    }
}