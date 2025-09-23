package com.slow.care.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slow.care.data.LoginRequest;
import com.slow.care.data.LoginResponse;
import com.slow.care.service.LoginService;
import com.slow.care.data.Person;
 
@RestController
@RequestMapping("/slow")
public class LoginController {
	@Autowired
    private LoginService firebaseService;
	private Person person;
    
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("=== 로그인 요청 ===");
            System.out.println("사용자명: " + request.getId());
            
            // 입력 검증
            if (request.getId() == null || request.getId().trim().isEmpty()) {
                return ResponseEntity.ok(new LoginResponse(false, "아이디를 입력해주세요", person));
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.ok(new LoginResponse(false, "비밀번호를 입력해주세요"));
            }
            
            // 1. 사용자 확인
            Map<String, Object> userData = firebaseService.getPerson("person", request.getId());
            
            if (userData == null) {
                System.out.println("사용자를 찾을 수 없음: " + request.getId());
                return ResponseEntity.ok(new LoginResponse(false, "사용자를 찾을 수 없습니다"));
            }
            System.out.println("비번 : " + request.getPassword());
            
            // 2. 비밀번호 확인
            String storedPassword = (String) userData.get("password");
            if (storedPassword == null || !request.getPassword().equals(storedPassword)) {
            	System.out.println("찾은비번 : " + storedPassword);
                System.out.println("비밀번호 불일치");
                return ResponseEntity.ok(new LoginResponse(false, "비밀번호가 틀렸습니다"));
            }
            
            System.out.println("✅ 로그인 성공: " + request.getId());
            return ResponseEntity.ok(new LoginResponse(true, "로그인 성공", person));
            
        } catch (Exception e) {
            System.err.println("❌ 로그인 처리 중 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(new LoginResponse(false, "서버 오류가 발생했습니다"));
        }
    }
    
}

