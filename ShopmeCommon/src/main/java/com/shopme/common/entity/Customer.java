package com.shopme.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 45, nullable = false, unique = true)
	private String email;
	
	@Column(length = 64, nullable = false)
	private String password;
	
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	
	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;
	
	@Column(name = "phone_Number", length = 15, nullable = false)
	private String phoneNumber;
	
	@Column(length = 64, nullable = false)
	private String addressLine1;
	
	@Column(length = 64, name="address_line_2")
	private String addressLine2;
	
	@Column(length = 45, nullable = false)
	private String city;
	
	@Column(length = 45, nullable = false)
	private String state;
	
	@Column(length = 10, nullable = false, name="postal_code")
	private String postalCode;
	
	@Column(length = 64, name="verification_code")
	private String verificationCode;
	
	private boolean enabled;
	
	@Column(name="created_time")
	private Date createdTime;
	
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country country;
	
	public String getFullName() {
		return firstName+" "+lastName;
	}
}
