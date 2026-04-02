package com.keshe.server.data.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
