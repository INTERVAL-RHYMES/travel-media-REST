package com.example.travelmediarest.mapper;

import com.example.travelmediarest.dto.PostDto;
import com.example.travelmediarest.model.Post;

public class PostDtoMapper {
    public static PostDto postToPostDtoMapper(Post post){
        return new PostDto(post.getId(),post.getUser(),post.getStatus(),post.getLocation().getName(),post.getPrivacy(),post.getPined());
    }
}
