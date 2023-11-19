package com.banu.controller;

import com.banu.dto.request.LoginRequestDto;
import com.banu.dto.request.RegisterRequestDto;
import com.banu.dto.request.UpdateStatusRequestDto;
import com.banu.dto.request.UpdateUsernameOrEmailRequestDto;
import com.banu.dto.response.RegisterResponseDto;
import com.banu.repository.entity.Auth;
import com.banu.service.AuthService;
import com.banu.utility.JwtTokenManager;
import com.banu.utility.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.banu.constants.RestApi.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {

    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PutMapping("/update-status")
    public ResponseEntity<Boolean> updateStatus(@RequestBody UpdateStatusRequestDto dto) {
        return ResponseEntity.ok(authService.updateStatus(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping("create-token")
    public ResponseEntity<String> createToken(Long authId) {
        return ResponseEntity.ok(jwtTokenManager.createToken(authId).get());
    }

    @GetMapping("create-token2")
    public ResponseEntity<String> createToken2(Long authId, Role role) {
        return ResponseEntity.ok(jwtTokenManager.createToken(authId, role).get());
    }

    @GetMapping("/get-authId-from-token")
    public ResponseEntity<Long> getAuthIdFromToken(String token){
        return ResponseEntity.ok(jwtTokenManager.getIdFromToken(token).get());
    }

    @GetMapping("/get-role-from-token")
    public ResponseEntity<String> getRoleFromToken(String token){
        return ResponseEntity.ok(jwtTokenManager.getRoleFromToken(token).get());
    }

    @PutMapping("/update-email-username")
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestBody UpdateUsernameOrEmailRequestDto dto){
        return ResponseEntity.ok(authService.updateEmailOrUsername(dto));
    }

    @PutMapping(DELETEBYID + "/{id}")
    public ResponseEntity<Boolean> deleteAuth(@PathVariable  Long id){
        return ResponseEntity.ok(authService.deleteAuth(id));
    }


}
