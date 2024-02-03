package com.tweetapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.model.UserDetails;
import com.tweetapp.model.UserRegistrationDetails;
import com.tweetapp.repository.UserDetailsRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserOperationsServiceImpl implements UserOperationsService {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	//error message
	private final static String ERROR_MSG = "Some error occured while connecting to DB";

	@Override
	public ResponseEntity<?> registerUser(UserRegistrationDetails userRegistrationDetails) {

		try {
			Optional<UserDetails> userDetails = userDetailsRepository.findById(userRegistrationDetails.getLoginId());
			if(userDetails.isPresent()) {
				UserDetails u = userDetails.get();
				if(u.getEmailId().equals(userRegistrationDetails.getEmailId())) {
					String msg = "This email id and login id is already present, Try with some different email id and login id";
					return ResponseEntity.ok(msg);
				}else {
					String msg = "This login id is already present, Try with some different login id";
					return ResponseEntity.ok(msg);
				}
			} else {
				if(userRegistrationDetails.getPassword().equals(userRegistrationDetails.getConfirmPassword())) {
					UserDetails userData = new UserDetails(userRegistrationDetails.getFirstName(),userRegistrationDetails.getLastName(),userRegistrationDetails.getEmailId(),userRegistrationDetails.getLoginId(),userRegistrationDetails.getPassword(),userRegistrationDetails.getContactNumber());
					userDetailsRepository.save(userData);
				}else {
					String msg = "Password and Confirm Password must be same";
					return ResponseEntity.ok(msg);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
		return ResponseEntity.ok("User Registered Successfully!!");
	}

	@Override
	public ResponseEntity<?> resetPassword(String loginId, String newPassword, String confirmPassword) {
		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if(userDetails==null) {
				return ResponseEntity.ok("No details with the given loginId found, please register first");
			}else {
				if(newPassword.equals(confirmPassword)) {
					//UserDetails user = userDetails.get();
					userDetails.setPassword(newPassword);
					userDetailsRepository.save(userDetails);
					return ResponseEntity.ok("Your Password have been changed successfully");
				}
				return ResponseEntity.ok("New Password and confirm password must be same");
			}
		} catch(Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> getAllUsers() {
		try {
			ResponseEntity<List<UserDetails>> allUsers = new ResponseEntity<>(userDetailsRepository.findAll(),HttpStatus.OK);
			return allUsers;
		}catch(Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> searchUser(String loginId) {
		try {
			ResponseEntity<UserDetails> user = new ResponseEntity<UserDetails>(userDetailsRepository.findByLoginId(loginId),HttpStatus.OK);
			if(user.getBody().getLoginId().equals(loginId)) {
				return user;
			}
			return (ResponseEntity<UserDetails>) ResponseEntity.status(404);
		}catch(NullPointerException e) {
			String msg = "This login id is not present, Try with some valid login id";
			log.error(msg);
			return (ResponseEntity<?>) ResponseEntity.ok(msg);
		}catch(Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<UserDetails>) ResponseEntity.internalServerError();
		}
	}
	
}
