package com.example.travelmediarest.controller;

import com.example.travelmediarest.dto.PostDto;
import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.model.Post;
import com.example.travelmediarest.service.LocationService;
import com.example.travelmediarest.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/post")
public class PostController {

    @Autowired
    @Qualifier("postService")
    private PostService postService;
    @Autowired
    @Qualifier("locationService")
    private LocationService locationService;

    @ModelAttribute(name = "postDto")
    public PostDto postDto() {
        return new PostDto();
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @AuthenticationPrincipal User user) {
        log.info("RequestBody " + postDto);

//        Map<String, Object> objects = new HashMap<>();

//        if (errors.hasErrors()) {
//            objects.put("postDto", postDto);
//            objects.put("errors", errors);
//
//            return new ResponseEntity<>(objects, HttpStatus.BAD_REQUEST);
//        }

        Post post = postService.saveThisPost(postDto, user.getUsername());
        postDto.setId(post.getId());
        postDto.setUser(post.getUser());
        postDto.setPined(post.getPined());
//        objects.put("successfully created ", postDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(postDto);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> createPost(@PathVariable Long id) {
        Map<String, Object> objects = new HashMap<>();

        PostDto postDto = postService.fetchPostById(id);
        List<Location> locationList = locationService.fetchAllLocation();

        objects.put("postDto", postDto);
        objects.put("locationList", locationList);

        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> editSubmitPost(@Valid @RequestBody PostDto postDto, Errors errors) {
        Map<String, Object> objects = new HashMap<>();
        if (errors.hasErrors()) {
            objects.put("Error", errors);
            return new ResponseEntity<>(objects, HttpStatus.BAD_REQUEST);
        }
        postDto = postService.updateThePost(postDto);
        objects.put("postDto", postDto);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

    @GetMapping("/pined/{id}")
    public ResponseEntity<Map<String, Object>> pinedPost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Map<String, Object> objects = new HashMap<>();
        log.info("test: " + user + " " + id);
        Post post = postService.updatePostByPin(id, user.getUsername());
        log.info("test pined: " + user + " " + id);
        objects.put("post", post);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }
    @GetMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id) {
        Map<String, Object> objects = new HashMap<>();
        log.info("delete this post :" + id);
        postService.deletePostById(id);
        objects.put("delete", "delete post Successfully " + id);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }
}
