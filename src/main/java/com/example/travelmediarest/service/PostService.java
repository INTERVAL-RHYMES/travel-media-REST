package com.example.travelmediarest.service;

import com.example.travelmediarest.dto.PostDto;
import com.example.travelmediarest.mapper.PostDtoMapper;
import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.model.Post;
import com.example.travelmediarest.model.User;
import com.example.travelmediarest.repository.LocationRepository;
import com.example.travelmediarest.repository.PostRepository;
import com.example.travelmediarest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PostService {
    @Autowired
    @Qualifier("postRepository")
    private PostRepository postRepository;
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    @Autowired
    @Qualifier("locationRepository")
    private LocationRepository locationRepository;


    public void deletePostById(Long id) {
        postRepository.deleteById(id);
        Optional<Post> post = postRepository.findById(id);
        log.info("delete??? "+(post.isPresent() ?"delay delete":"successfully deleted"));
    }

    public Post updatePostByPin(Long id, String mail) {
        resetPinPost(mail);
        log.info("test update: " + id + " " + mail);

        Optional<Post> postOptional = postRepository.findById(id);
//        Post post1 = postRepository.findById(id).orElseThrow(()->new IllegalStateException("not found"));

        Post post = postOptional.get();
        post.setPined(1L);
        postRepository.save(post);
        log.info("pinned successfully");
        return post;
    }

    public void resetPinPost(String mail) {
        List<Post> posts = postRepository.findAll();
        Optional<com.example.travelmediarest.model.User> userOptional = userRepository.findByMail(mail);
        com.example.travelmediarest.model.User user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            return;
        }
        log.info("user find?: " + user);
        Long id = user.getId();
        for (Post post : posts) {
            if (post.getUser().getId().equals(id)) {
                post.setPined(0L);
            }
        }
        postRepository.saveAll(posts);
    }

    public PostDto updateThePost(PostDto postDto) {
        Optional<Post> postOptional = postRepository.findById(postDto.getId());
        Post post = postOptional.get();
        post.setStatus(postDto.getStatus());
        post.setLocation(getLocation(postDto.getLocation()));
        post.setPrivacy(postDto.getPrivacy());
        postRepository.save(post);
        postDto = PostDtoMapper.postToPostDtoMapper(post);
        return postDto;
    }

    public PostDto fetchPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) return null;
        Post post = postOptional.get();
        return new PostDto(post.getId(), post.getUser(), post.getStatus(), post.getLocation().getName(), post.getPrivacy(), post.getPined());
    }

    public Post saveThisPost(PostDto postDto, String mail) {
        log.info("test phase:  postdto : " + postDto + "\nand user " + mail);
        Post post = new Post(toUser(mail), postDto.getStatus(), getLocation(postDto.getLocation()), postDto.getPrivacy());
        post = postRepository.saveAndFlush(post);
        log.info("test phase:  post: " + post);

        log.info("post save successfully");
        return post;
    }

    public com.example.travelmediarest.model.User toUser(String mail) {
        List<User> users = userRepository.findAll();
        log.info("checking users: " + users);
        Optional<com.example.travelmediarest.model.User> userOptional = userRepository.findByMail(mail);
        com.example.travelmediarest.model.User user1 = null;
        log.info("userOptional :: " + userOptional);
        if (userOptional.isPresent()) {
            user1 = userOptional.get();
        }

        return user1;
    }

    public Location getLocation(String loc) {

//        Optional<Location> location = locationRepository.findByName(loc);
//        Location location = locationRepository.findByName(loc).orElse(null);
        Location location = locationRepository.findByName(loc).orElseThrow(() -> new IllegalStateException("Location not found"));
        log.info("get location!!!!!!!");
        return location;
    }


    public List<PostDto> fetchPostByUser(String mail) {
        List<PostDto> postDtoList = new ArrayList<>();

        List<Post> posts = postRepository.findAllByUser_Mail(mail);

        Collections.sort(posts, new customSort());

        for (Post post : posts) {
            postDtoList.add(new PostDto(post.getId(), post.getUser(), post.getStatus(), post.getLocation().getName(), post.getPrivacy(), post.getPined()));
        }

        return postDtoList;
    }

    public List<PostDto> fetchForHomePage(String mail) {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtoList = new ArrayList<>();

        Optional<com.example.travelmediarest.model.User> userOptional = userRepository.findByMail(mail);
        com.example.travelmediarest.model.User user = null;

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            return postDtoList;
        }

        Long id = user.getId();

        Collections.sort(posts, new customSort());

        for (Post post : posts) {
            if (post.getUser().getId() == id || post.getPrivacy().charAt(1) == 'u') {
                postDtoList.add(new PostDto(post.getId(), post.getUser(), post.getStatus(), post.getLocation().getName(), post.getPrivacy(), post.getPined()));
            }
        }

        return postDtoList;
    }

    static class customSort implements Comparator<Post> {

        @Override
        public int compare(Post o1, Post o2) {
            int ret = o2.getPined().compareTo(o1.getPined());

            if (ret != 0) {
                return ret;
            } else {
                return o2.getPlacedAt().compareTo(o1.getPlacedAt());
            }
        }
    }
}
