package com.shopme.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOauth2User implements OAuth2User {
	
	private String clinetName;
	private OAuth2User oauth2User;
    private String fullName;
	public CustomerOauth2User(OAuth2User oauth2User, String clinetName) {
		this.oauth2User = oauth2User;
		this.clinetName=clinetName;
	}

	@Override
	public Map<String, Object> getAttributes() {

		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oauth2User.getAttribute("name");
	}

	public String getEmail() {
		return oauth2User.getAttribute("email");
	}

	public String getFullName() {
		return fullName != null ? fullName : oauth2User.getAttribute("name");
	}

	public String getClinetName() {
		return clinetName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
