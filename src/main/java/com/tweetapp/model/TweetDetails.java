package com.tweetapp.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetDetails {
	
	@Id
	private String loginId;
	private List<String> tweet;
	private List<Map<Integer,Integer>> likes;
	private List<Map<Integer,List<String>>> reply;
}
