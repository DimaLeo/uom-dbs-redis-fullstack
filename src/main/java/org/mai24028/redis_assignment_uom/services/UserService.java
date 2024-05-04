package org.mai24028.redis_assignment_uom.services;

import org.mai24028.redis_assignment_uom.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean userExists(String username) throws Exception {

        return userRepository.userNameExists(username);

    }

    public void storeUser(String username) throws Exception {
        userRepository.storeUser(username);

    }
}
