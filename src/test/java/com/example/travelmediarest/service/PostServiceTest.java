package com.example.travelmediarest.service;

import com.example.travelmediarest.dto.PostDto;
import com.example.travelmediarest.dto.RegistrationDto;
import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.model.User;
import com.example.travelmediarest.repository.LocationRepository;
import com.example.travelmediarest.repository.PostRepository;
import com.example.travelmediarest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class PostServiceTest {
    private static User user;
    private static PostDto postDto;
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;


    @BeforeAll
    public static void initBeforeAll(@Autowired LocationRepository locationRepository, @Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder, @Autowired PostService postService) {
//        log.info("call ****");
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setMail("h");
        registrationDto.setUsername("h");
        registrationDto.setPassword("h");
        registrationDto.setConfirm("h");

        user = registrationDto.toUser(passwordEncoder);
        userRepository.saveAndFlush(user);
        log.info("register successfully");


        postDto = new PostDto(null, user, "hello1", "Dinajpur", "public", 0L);
        postService.saveThisPost(postDto, user.getMail());
//        postDto.setId(postService.);
    }

    @BeforeEach
    public void initEach() {
        log.info("BeforeEach create");
    }

    @Test
    void updatePostByPin() {
        postService.updatePostByPin(1L,user.getMail());

        List<PostDto> postDtoList = postService.fetchPostByUser(user.getMail());

        postDtoList.forEach((post) -> {
            if(post.getId()==1){
                assertEquals(post.getPined(),1L);
            }
        });
    }

    @Test
    void resetPinPostTest() {
        postService.resetPinPost(user.getMail());

        log.info("mail?? " + user.getMail());
        List<PostDto> postDtoList = postService.fetchPostByUser(user.getMail());

        postDtoList.forEach((post) -> {
            assertEquals(post.getPined(),0L);
        });
    }

    @Test
    void updateThePost() {
        PostDto postDto1 = new PostDto(postDto.getId()==null?1L:postDto.getId(), user, "hello1 to update", "Dinajpur", "private", 1L);
        assertDoesNotThrow(()->{
            postService.updateThePost(postDto1);
        });
        log.info("update the post successfully");
    }

    @Test
    void fetchPostById() {

        PostDto postDto = postService.fetchPostById(1L);
        assertNotNull(postDto);

        postDto = postService.fetchPostById(0L);
        assertNull(postDto);

        postDto = postService.fetchPostById(10L);
        assertNull(postDto);
    }

    @Test
    void saveThisPostTest() {

        String mail = "h";
        PostDto postDto = new PostDto(null, user, "hello2", "Dhaka", "private", 0L);

        assertDoesNotThrow(() -> {
            postService.saveThisPost(postDto, mail);
        });
    }

    @Test
    void toUserTest() {

        String mail = "h";
        User user = postService.toUser(mail);
        assertNotNull(user);

        mail = "a";
        user = postService.toUser(mail);
        assertNull(user);
    }

    @Test
    void getLocation() {

        String loc = "Khulna";
        Location location = postService.getLocation(loc);
        assertNotNull(location);

        assertThrows(RuntimeException.class, () -> {
            postService.getLocation("dhakka");
        });
        assertThrows(RuntimeException.class, () -> {
            postService.getLocation("");
        });
        assertThrows(RuntimeException.class, () -> {
            postService.getLocation("gfbdfgndf");
        });
        assertThrows(RuntimeException.class, () -> {
            postService.getLocation(null);
        });
    }

    @Test
    void fetchPostByUser() {

        log.info("mail?? " + user.getMail());
        List<PostDto> postDtoList = postService.fetchPostByUser(user.getMail());
//        assertEquals(postDtoList.size(), 1);
//
//        assertNotEquals(postDtoList.size(), 2);

        postDtoList.forEach((post) -> {
            assertNotNull(post.getId());
            assertNotNull(post.getUser());
            assertEquals(post.getUser().getMail(), user.getMail());
            assertNotNull(post.getLocation());
            assertNotNull(post.getPrivacy());
            assertNotNull(post.getStatus());
        });
    }

    @Test
    void fetchForHomePage() {

        List<PostDto> postDtoList = postService.fetchForHomePage(user.getMail());

        postDtoList.forEach(postDto -> {
            assertNotNull(postDto.getId());
            assertNotNull(postDto.getUser());
            assertNotNull(postDto.getLocation());
            assertNotNull(postDto.getPrivacy());
            assertNotNull(postDto.getStatus());
        });

    }

    @Test
    void deletePostById() {

//        assertThrows()
        assertDoesNotThrow(() -> {
            postService.deletePostById(2L);
        });
        assertThrows(RuntimeException.class, () -> {
            postService.deletePostById(2L);
        });
    }
}