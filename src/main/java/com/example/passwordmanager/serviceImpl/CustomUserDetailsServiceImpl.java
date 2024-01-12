package com.example.passwordmanager.serviceImpl;

import com.example.passwordmanager.model.VaultUser;
import com.example.passwordmanager.repository.VaultUserRepository;
import com.example.passwordmanager.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private VaultUserRepository vaultUserRepository;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        VaultUser vaultUser = vaultUserRepository.findByUsername(username);

        if(vaultUser!=null){
            return new User(
                    vaultUser.getUsername(),
                    vaultUser.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE"))
            );
        }

        throw new UsernameNotFoundException("User not found with username:" + username);
    }
}
