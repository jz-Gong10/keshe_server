package com.keshe.server.data.dto;

import lombok.Data;

@Data
public class AddCommentDTO {
    private Integer productId;
    private String content;
}
