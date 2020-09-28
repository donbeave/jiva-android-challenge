package com.zhokhov.jiva.challenge.server;

import com.zhokhov.jiva.challenge.server.domain.User;
import com.zhokhov.jiva.challenge.server.repository.UserRepository;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;

import java.time.ZoneOffset;
import java.util.TimeZone;

public class JivaServerApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));

        ApplicationContext applicationContext = Micronaut.run(JivaServerApplication.class, args);

        UserRepository userRepository = applicationContext.getBean(UserRepository.class);

        userRepository.save(new User("1", "test@test.com", "test", null));
        userRepository.save(new User("2", "admin@admin.com", "admin", null));
    }

}
