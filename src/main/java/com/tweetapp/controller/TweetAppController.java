package com.tweetapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.exception.AuthenticationException;
import com.tweetapp.jwtutility.JwtUtil;
import com.tweetapp.model.AuthRequest;
import com.tweetapp.model.UserRegistrationDetails;
import com.tweetapp.model.ValidateToken;
import com.tweetapp.service.CustomAuthUserDetailsService;
import com.tweetapp.service.TweetOperationsService;
import com.tweetapp.service.UserOperationsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1.0/tweets")
@Slf4j
@CrossOrigin(origins="*")
public class TweetAppController {
	
	@Autowired
	private UserOperationsService userOperationsService;
	
	@Autowired
	private TweetOperationsService tweetOperationsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired 
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomAuthUserDetailsService customAuthUserDetailsService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistrationDetails userRegistrationDetails){
		 return userOperationsService.registerUser(userRegistrationDetails);
	}
	
	@PostMapping("/{loginId}/forgot")
	public ResponseEntity<?> resetPassword(@PathVariable("loginId") String loginId, @RequestParam String newPassword, @RequestParam String confirmPassword){
		return userOperationsService.resetPassword(loginId, newPassword, confirmPassword);
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to the new wolrd";
	}
	
	@PostMapping("/authenticate")
	public String generateToken(@RequestBody AuthRequest authRequest) throws AuthenticationException{
		try {
			System.out.println(authRequest.getUserName()+" "+authRequest.getPassword());
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
			);
		} catch(Exception e) {
			log.error("Invalid loginId/password");
			return "Invalid loginId/password";
		//	throw new AuthenticationException("Invalid loginId/passwprd");
		}
		return jwtUtil.generateToken(authRequest.getUserName());
	}
	
	@PostMapping("/validateToken")
	public boolean validateToken(@RequestBody ValidateToken token) {
		try {
			String userName = jwtUtil.extractUsername(token.getToken());
			return userName.equals(token.getLoginId()) && !jwtUtil.isTokenExpired(token.getToken());
		}catch(Exception e) {
			log.error("token expired");
			return false;
		}
		
	}
	
	@GetMapping("/users/all")
	public ResponseEntity<?> getAllUsers(){
		return userOperationsService.getAllUsers();
	}
	
	@GetMapping("/user/search/{loginId}")
	public ResponseEntity<?> searchUser(@PathVariable("loginId") String loginId){
		return userOperationsService.searchUser(loginId);
	}
	
	@PostMapping("/{loginId}/add")
	public ResponseEntity<?> addTweet(@PathVariable("loginId") String loginId, @RequestBody String tweet){
		return tweetOperationsService.addTweet(loginId, tweet);
	}
	
	@PutMapping("/{loginId}/update/{id}")
	public ResponseEntity<?> updateTweet(@PathVariable("loginId") String loginId, @PathVariable("id") int id,@RequestBody String updatedTweet){
		return tweetOperationsService.updateTweet(loginId, id, updatedTweet);
	}
	
	@DeleteMapping("/{loginId}/delete/{id}")
	public ResponseEntity<?> deleteTweet(@PathVariable("loginId") String loginId, @PathVariable("id") int id){
		return tweetOperationsService.deleteTweet(loginId, id);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllTweets(){
		return tweetOperationsService.getAllTweets();
	}
	
	@GetMapping("/{loginId}")
	public ResponseEntity<?> getAllTweetsByUserName(@PathVariable("loginId") String loginId){
		return tweetOperationsService.getAllTweetsByUserName(loginId);
	}
	
	@PutMapping("/{loginId}/like/{id}")
	public ResponseEntity<?> likeTweet(@PathVariable("loginId") String loginId,@PathVariable("id") int id){
		return tweetOperationsService.likeTweet(loginId,id);
	}
	
	@GetMapping("/{loginId}/viewLikesOnTweet/{id}")
	public ResponseEntity<?> viewLikesOnTweet(@PathVariable("loginId") String loginId,@PathVariable("id") int id){
		return tweetOperationsService.viewLikesOnTweet(loginId,id);
	}
	
	@PutMapping("/{loginId}/reply/{id}")
	public ResponseEntity<?> replyTweet(@PathVariable("loginId") String loginId,@PathVariable("id") int id, @RequestBody String reply){
		return tweetOperationsService.replyTweet(loginId,id,reply);
	}
	
	@GetMapping("/{loginId}/viewReply/{id}")
	public ResponseEntity<?> viewReplyTweet(@PathVariable("loginId") String loginId,@PathVariable("id") int id){
		return tweetOperationsService.viewReplyTweet(loginId,id);
	}
}
