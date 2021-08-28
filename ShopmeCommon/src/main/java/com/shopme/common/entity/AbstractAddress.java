package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractAddress  extends IdBasedEntity{
	
	@Column(name = "first_name", length = 45, nullable = false)
	protected String firstName;
	
	@Column(name = "last_name", length = 45, nullable = false)
	protected String lastName;
	
	@Column(name = "phone_Number", length = 15, nullable = false)
	protected String phoneNumber;
	
	@Column(length = 64, nullable = false)
	protected String addressLine1;
	
	@Column(length = 64, name="address_line_2")
	protected String addressLine2;
	
	@Column(length = 45, nullable = false)
	protected String city;
	
	@Column(length = 45, nullable = false)
	protected String state;
	
	@Column(length = 10, nullable = false, name="postal_code")
	protected String postalCode;
	
	
}
