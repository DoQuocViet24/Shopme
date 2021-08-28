package com.shopme.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends AbstractAddressWithCountry{
	
	@Column(length = 45, nullable = false, unique = true)
	private String email;
	
	@Column(length = 64, nullable = false)
	private String password;
		
	@Column(length = 64, name="verification_code")
	private String verificationCode;
	
	private boolean enabled;
	
	@Column(name="created_time")
	private Date createdTime;
	
	@Column(name="reset_password_token", length = 30)
	private String resetPasswordToken;
	
	@Enumerated(EnumType.STRING)
	@Column(name="authentication_type", length = 10)
	private AuthenticationType authenticationType;
	
	public String getFullName() {
		return firstName+" "+lastName;
	}
    
}
