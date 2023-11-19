package com.banu.manager;

import com.banu.dto.request.CreateUserProfileRequestDto;
import com.banu.dto.request.DeleteUserProfileRequestDto;
import com.banu.dto.request.UserProfileActivateStatusRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.banu.constants.RestApi.DELETEBYID;


@FeignClient(url = "http://localhost:7071/api/v1/user", name = "auth-userProfile")
public interface UserProfileManager {

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody CreateUserProfileRequestDto dto);

    @PutMapping("/update-status")
    public ResponseEntity<Boolean> activeStatus(@RequestBody UserProfileActivateStatusRequestDto dto);

    @PutMapping(DELETEBYID)
    public ResponseEntity<Boolean> deleteUserProfile(@RequestBody DeleteUserProfileRequestDto dto);

}
