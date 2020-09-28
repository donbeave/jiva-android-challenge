package com.zhokhov.jiva.challenge.server.http;

import com.zhokhov.jiva.challenge.server.domain.User;
import com.zhokhov.jiva.challenge.server.repository.UserRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Controller("/images")
public class ImagesController {

    private static final Logger LOG = LoggerFactory.getLogger(ImagesController.class);

    private final UserRepository userRepository;

    public ImagesController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Get("/{userId}")
    @ExecuteOn(TaskExecutors.IO)
    public StreamedFile retrieve(@PathVariable String userId) throws IOException {
        LOG.debug("Retrieving image: {}", userId);

        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty()) {
            throw new FileNotFoundException();
        }

        Path path = Path.of(user.get().getAvatarPath());

        InputStream convertedInputStream = Files.newInputStream(path, StandardOpenOption.READ);
        return new StreamedFile(convertedInputStream, MediaType.IMAGE_JPEG_TYPE);
    }

}
