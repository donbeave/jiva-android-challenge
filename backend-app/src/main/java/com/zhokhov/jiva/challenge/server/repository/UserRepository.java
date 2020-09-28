package com.zhokhov.jiva.challenge.server.repository;

import com.zhokhov.jiva.challenge.server.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    private static final Map<String, User> USERS = new HashMap<>();

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(USERS.get(email));
    }

    public Optional<User> findByUserId(String userId) {
        return USERS.values().stream()
                .filter(it -> it.getId().equals(userId))
                .findFirst();
    }

    public void save(User user) {
        LOG.debug("Saving user: {}", user.getEmail());

        USERS.put(user.getEmail(), user);
    }

}
