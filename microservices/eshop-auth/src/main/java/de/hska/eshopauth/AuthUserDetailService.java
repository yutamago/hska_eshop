package de.hska.eshopauth;

import de.hska.eshopauth.dao.UserDAO;
import de.hska.eshopauth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailService implements UserDetailsService {

    private final UserDAO userRepository;

    @Autowired
    public AuthUserDetailService(UserDAO userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User[] userOpt = userRepository.findByUsername(username).toArray(new User[]{});
        if(userOpt.length == 0)
            throw new UsernameNotFoundException(username + "' not found");
        else
            return userOpt[0];
    }

}
