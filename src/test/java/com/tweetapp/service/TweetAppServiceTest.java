package com.tweetapp.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.TweetDetailsRepository;
import com.tweetapp.repository.UserDetailsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TweetAppServiceTest {
	
	 private MockMvc mockMvc;
	
	@MockBean
	private TweetDetailsRepository tweetDetailsRepository;

	@MockBean
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private TweetOperationsService tweetService;
	
	@Autowired
	private UserOperationsService userService;
	
	
	@Test
	public void addTweetTest() {
		UserDetails user = new UserDetails("Ashish","Malviya","am@gmail.com","am@gmail.com","abc","1234567890");
		String loginId = "abc@gmail.com";
		String tweet = "ABC tweets";
	    Mockito.when(userDetailsRepository.findByLoginId(loginId)).thenReturn(user);
	   ResponseEntity<?> userDetails = (ResponseEntity<?>) userService.searchUser(loginId);
	   assertEquals(ResponseEntity.internalServerError(),userDetails.internalServerError());
	}
	
}
