package com.shopme.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);
	
	@Modifying
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	public void enable (Integer id);
	
	@Modifying
	@Query("UPDATE Customer c SET c.authenticationType= ?2 WHERE c.id = ?1")
	public void updateAuthenticationType(Integer id ,AuthenticationType type);
	
	public Customer findByResetPasswordToken(String token);
}
