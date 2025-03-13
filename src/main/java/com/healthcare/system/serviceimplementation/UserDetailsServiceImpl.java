package com.healthcare.system.serviceimplementation;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.healthcare.system.models.User;
import com.healthcare.system.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepo.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Not found"));
    }
}