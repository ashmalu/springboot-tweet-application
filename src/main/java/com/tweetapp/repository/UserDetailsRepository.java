package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.UserDetails;

@Repository
public interface UserDetailsRepository extends MongoRepository<UserDetails, String>{

	UserDetails findByLoginId(String loginId);
	
}
