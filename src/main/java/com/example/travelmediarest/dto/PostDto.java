package com.example.travelmediarest.dto;

import com.example.travelmediarest.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class PostDto {
    private Long id;
    private User user;
    @NotNull
    @Size(min = 1, message = "status at least one length")
    private String status;
    @NotNull
    private String location;
    @NotNull
    private String privacy;
    private Long pined;

    public PostDto(Long id, User user, String status, String location, String privacy, Long pined) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.location = location;
        this.privacy = privacy;
        this.pined = pined;
    }
}
