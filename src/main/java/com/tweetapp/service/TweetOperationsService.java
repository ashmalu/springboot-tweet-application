package com.tweetapp.service;

import org.springframework.http.ResponseEntity;

public interface TweetOperationsService {
	
	ResponseEntity<?> addTweet(String loginId,String tweet);
	
	ResponseEntity<?> updateTweet(String loginId,int id,String updatedTweet);
	
	ResponseEntity<?> deleteTweet(String loginId,int id);
	
	ResponseEntity<?> getAllTweets();

	ResponseEntity<?> getAllTweetsByUserName(String loginId);

	ResponseEntity<?> likeTweet(String loginId, int id);

	ResponseEntity<?> replyTweet(String loginId, int id, String reply);

	ResponseEntity<?> viewReplyTweet(String loginId, int id);

	ResponseEntity<?> viewLikesOnTweet(String loginId, int id);
}
