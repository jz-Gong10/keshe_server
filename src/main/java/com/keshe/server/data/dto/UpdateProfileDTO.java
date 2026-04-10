package com.keshe.server.data.dto;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private String gender;
    private String bio;
}
