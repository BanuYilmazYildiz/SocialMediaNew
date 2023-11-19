package com.banu.manager;

import com.banu.dto.request.UpdateUsernameOrEmailRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(url = "http://localhost:7070/api/v1/auth", name = "userProfile-auth")
public interface AuthManager {

    @PutMapping("/update-email-username")
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestBody UpdateUsernameOrEmailRequestDto dto);


}
