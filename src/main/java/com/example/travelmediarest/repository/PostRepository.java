package com.example.travelmediarest.repository;

import com.example.travelmediarest.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser_Mail(String mail);
}
