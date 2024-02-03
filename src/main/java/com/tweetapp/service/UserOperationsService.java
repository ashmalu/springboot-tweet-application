package com.tweetapp.service;

import org.springframework.http.ResponseEntity;

import com.tweetapp.model.UserRegistrationDetails;

public interface UserOperationsService {
	
	ResponseEntity<?> registerUser(UserRegistrationDetails userRegistrationDetails);
	
	ResponseEntity<?> resetPassword(String loginId, String newPassword, String confirmPassword);

	ResponseEntity<?> getAllUsers();
	
	ResponseEntity<?> searchUser(String loginId);
}
