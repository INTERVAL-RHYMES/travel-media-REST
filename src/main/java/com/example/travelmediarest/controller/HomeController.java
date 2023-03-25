package com.example.travelmediarest.controller;

import com.example.travelmediarest.dto.PostDto;
import com.example.travelmediarest.dto.PrivacyDto;
import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.service.LocationService;
import com.example.travelmediarest.service.PostService;
import com.example.travelmediarest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/home")
public class HomeController {
    @Autowired
    @Qualifier("postService")
    PostService postService;
    @Autowired
    @Qualifier("locationService")
    LocationService locationService;
    @Autowired
    @Qualifier("userService")
    UserService userService;

    @ModelAttribute(name = "postDto")
    public PostDto postDto() {
        return new PostDto();
    }

    @ModelAttribute("privacyDto")
    public PrivacyDto privacyDto() {
        return new PrivacyDto();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getHomePage(@AuthenticationPrincipal User user) {
        log.info("User info::: " + user);
        Map<String, Object> objects = new HashMap<>();

        List<PostDto> postDtoList = postService.fetchForHomePage(user.getUsername());
        com.example.travelmediarest.model.User user1 = userService.fetchUserByMail(user.getUsername());
        List<Location> locationList = locationService.fetchAllLocation();

//        postDtoList.add(new PostDto(1L,user1,"status done","dhaka","public",0L));

        objects.put("postDtoList", postDtoList);
        objects.put("user", user1);
        objects.put("locationList", locationList);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> GoProfilePage(@AuthenticationPrincipal User user) {
        log.info("user profile: " + user);
        Map<String, Object> objects = new HashMap<>();
        List<PostDto> postDtoList = postService.fetchPostByUser(user.getUsername());
        objects.put("username", user);
        objects.put("postDtoList", postDtoList);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }
//    @GetMapping("/friends")
//    public String GoFriendPage(@AuthenticationPrincipal User user){
//
//    }
//    @PostMapping("/search")
//    public String GoSearchPage(String search,@AuthenticationPrincipal User user){
//
//    }
}
