package com.api.login.service;

import com.api.login.entity.User;
import com.api.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repository.getByName(s);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }
    @Transactional(readOnly = true)
    public User getById(Long id) {
       Optional<User> user = repository.findById(id);

       return user.get();
    }
    @Transactional
    public void postUser(User user) {
      String password = encoder.encode(user.getPassword());
      user.setPassword(password);
      repository.save(user);
    }
}
