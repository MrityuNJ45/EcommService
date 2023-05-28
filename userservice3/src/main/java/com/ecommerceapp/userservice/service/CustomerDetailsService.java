package com.ecommerceapp.userservice.service;

import com.ecommerceapp.userservice.models.Customer;
import com.ecommerceapp.userservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> opt= customerRepository.findByEmail(username);

        if(opt.isPresent()) {

            Customer customer= opt.get();

            List<GrantedAuthority> authorities= new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(customer.getRole()));


            return new User(customer.getEmail(), customer.getPassword(), authorities);



        }else
            throw new BadCredentialsException("User Details not found with this username: "+username);
    }
}
