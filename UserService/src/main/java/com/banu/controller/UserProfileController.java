package com.banu.controller;

import com.banu.dto.request.CreateUserProfileRequestDto;
import com.banu.dto.request.DeleteUserProfileRequestDto;
import com.banu.dto.request.UpdateUserProfileRequestDto;
import com.banu.dto.request.UserProfileActivateStatusRequestDto;
import com.banu.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.banu.constants.RestApi.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {

    private final UserProfileService userProfileService;


    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody CreateUserProfileRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @PutMapping("/update-status")
    public ResponseEntity<Boolean> activeStatus(@RequestBody UserProfileActivateStatusRequestDto dto){
        return ResponseEntity.ok(userProfileService.activeStatus(dto));
    }

    @PutMapping("/update-user-profile")
    public ResponseEntity<Boolean> updateUserProfile(@RequestBody UpdateUserProfileRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateUserProfile(dto));
    }

    @PutMapping(DELETEBYID)
    public ResponseEntity<Boolean> deleteUserProfile(@RequestBody DeleteUserProfileRequestDto dto){
        return ResponseEntity.ok(userProfileService.deleteUserProfile(dto));
    }

}
