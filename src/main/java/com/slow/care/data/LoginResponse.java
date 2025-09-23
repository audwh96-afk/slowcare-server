package com.slow.care.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
	private boolean success;
    private String message;
    private Person person;
    
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.person = null;
    }
    
    public LoginResponse(boolean success, String message, Person person) {
        this.success = success;
        this.message = message;
        this.person = person;
    }
}


