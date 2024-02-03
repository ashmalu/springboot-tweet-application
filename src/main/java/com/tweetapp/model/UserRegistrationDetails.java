package com.tweetapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor 
@Data
public class UserRegistrationDetails {
	@NotBlank(message="First Name field should not be empty")
	private String firstName;
	@NotBlank(message="Last Name field should not be empty")
	private String lastName;
	@NotBlank(message="Email Id field should not be empty")
	@Email
	private String emailId;
	@NotBlank(message="Login Id field should not be empty")
	private String loginId;
	@NotBlank(message="Password field should not be empty")
	private String password;
	@NotBlank(message="Confirm Password field should not be empty")
	private String confirmPassword;
	@NotBlank(message="Contact Nummber field should not be empty")
	private String contactNumber;
}
