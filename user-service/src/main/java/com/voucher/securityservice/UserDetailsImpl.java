package com.voucher.securityservice;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.voucher.entity.User;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String username;

	private String password;
	
    private String name;
    
    private String imagePath;
    
    private String mentorEmail;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String username, String password, String name, String imagePath, String mentorEmail,Collection<? extends GrantedAuthority> authorities) {

		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.name=name;
		this.imagePath=imagePath;
		this.mentorEmail=mentorEmail;
	}

	public static UserDetailsImpl getUser(User user) {

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
		return new UserDetailsImpl(user.getUserEmail(), user.getPassword(), user.getUserName(), user.getImagePath(), user.getMentorEmail(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	
	public String getMentorEmail() {
		return mentorEmail;
	}

	public void setMentorEmail(String mentorEmail) {
		this.mentorEmail = mentorEmail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
