package com.shopme.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.CountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CountryRepository countryRepo;
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		
		return customer == null;
	}
	
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		String ramdomCode = RandomString.make(64);
		customer.setVerificationCode(ramdomCode);
		customerRepo.save(customer);
	}
	
	private void encodePassword(Customer customer) {
		String password = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(password);
	}
	
	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);
		if(customer == null || customer.isEnabled()) {
			return false;
		}else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}
	
	public void updateAuthentication(Customer customer, AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}
	
	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType authenticationType) {
		Customer customer =  new Customer();
		setName(name, customer);
		customer.setEmail(email);
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setAddressLine2("");
		customer.setCity("");
		customer.setCountry(countryRepo.findByCode(countryCode));
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		
		customerRepo.save(customer);
	}
	
	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if(nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);
			customer.setLastName(name.replaceFirst(firstName + " ",""));
		}
	}
	
	public void update(Customer customer) {
		 Customer customerInDB = customerRepo.findById(customer.getId()).get();
		
		 if(customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			 if (customer.getPassword().isEmpty()) {
				customer.setPassword(customerInDB.getPassword());
			} else {
				encodePassword(customer);
			}
		}else {
			customer.setPassword(customerInDB.getPassword());
		}
	    customer.setEnabled(customerInDB.isEnabled());
	    customer.setCreatedTime(customerInDB.getCreatedTime());
	    customer.setVerificationCode(customerInDB.getVerificationCode());
	    customer.setAuthenticationType(customerInDB.getAuthenticationType());
	    customer.setResetPasswordToken(customerInDB.getResetPasswordToken());
	    customerRepo.save(customer);
	}

	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByEmail(email);
		if(customer != null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepo.save(customer);
			return token;
		}else {
			throw new CustomerNotFoundException("Could not find any customer with the email "+ email);
		}
	}

	public Customer getByResetPasswordToken(String token) {
		return customerRepo.findByResetPasswordToken(token);
	}
	
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByResetPasswordToken(token);
		if(customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}
		customer.setPassword(newPassword);
		encodePassword(customer);
		customer.setResetPasswordToken("");
		customerRepo.save(customer);
	}

}
