package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetails {
	private String firstName;
	private String lastName;
	private String emailId;
	@Id
	private String loginId;
	private String password;
	private String contactNumber;
}
