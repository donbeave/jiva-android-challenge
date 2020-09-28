package com.zhokhov.jiva.challenge.server.http;

import com.zhokhov.jiva.challenge.server.domain.User;
import com.zhokhov.jiva.challenge.server.repository.UserRepository;
import com.zhokhov.jiva.challenge.server.service.TokenService;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller("/sessions")
public class SessionsController {

    private static final Logger LOG = LoggerFactory.getLogger(SessionsController.class);

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public SessionsController(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Post("/new")
    public HttpResponse<NewSessionResponse> newSession(@NonNull NewSessionRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            LOG.debug("User not found: {}", request.getEmail());
            return HttpResponse.badRequest();
        }

        if (!user.get().getPassword().equals(request.getPassword())) {
            LOG.debug("Password is not correct: {}", request.getEmail());
            return HttpResponse.unauthorized();
        }

        LOG.debug("Authenticated successfully: {}", request.getEmail());

        return HttpResponse.ok().body(
                new NewSessionResponse(user.get().getId(), tokenService.generateTokenByUser(user.get()))
        );
    }

}
