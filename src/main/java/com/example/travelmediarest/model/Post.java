package com.example.travelmediarest.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
//@RequiredArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @NotNull
    @Size(min = 2, message = "status size minimum 2")
    private String status;
    @ManyToOne
    private Location location;
    @NotNull
    private String privacy;
    @NotNull
    private Date placedAt;
    private Long pined = 0L;

    public Post(User user, String status, Location location, String privacy) {
        this.user = user;
        this.status = status;
        this.location = location;
        this.privacy = privacy;
        this.placedAt = new Date();
    }
}
