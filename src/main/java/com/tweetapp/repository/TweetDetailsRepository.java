package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.TweetDetails;

@Repository
public interface TweetDetailsRepository extends MongoRepository<TweetDetails, Integer> {

	TweetDetails findByLoginId(String loginId);

}
