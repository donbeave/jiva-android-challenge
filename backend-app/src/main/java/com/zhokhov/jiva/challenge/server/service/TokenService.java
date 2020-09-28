package com.zhokhov.jiva.challenge.server.service;

import com.zhokhov.jiva.challenge.server.domain.User;

import javax.inject.Singleton;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Singleton
public class TokenService {

    public static final Map<String, User> ACTIVE_TOKENS = new HashMap<>();

    public String generateTokenByUser(User user) {
        requireNonNull(user, "user");

        String token = generateRandomToken().toLowerCase();

        ACTIVE_TOKENS.put(token, user);

        return token;
    }

    public Optional<User> getUserByToken(String token) {
        requireNonNull(token, "token");

        return Optional.ofNullable(ACTIVE_TOKENS.get(token));
    }

    private String generateRandomToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
