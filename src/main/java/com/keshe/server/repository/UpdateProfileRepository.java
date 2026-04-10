package com.keshe.server.repository;

import com.keshe.server.data.dto.UpdateProfileDTO;
import com.keshe.server.data.vo.Result;
import org.springframework.http.ResponseEntity;

public interface UpdateProfileRepository {
    ResponseEntity<Result> getProfile(Long userId);
    ResponseEntity<Result> updateProfile(Long userId, UpdateProfileDTO updateProfileDTO);
}
