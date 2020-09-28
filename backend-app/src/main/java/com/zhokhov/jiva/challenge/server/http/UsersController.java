package com.zhokhov.jiva.challenge.server.http;

import com.zhokhov.jiva.challenge.server.domain.User;
import com.zhokhov.jiva.challenge.server.service.TokenService;
import com.zhokhov.jiva.challenge.server.util.Md5Utils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.runtime.server.EmbeddedServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Controller("/users")
public class UsersController {

    private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);

    private final TokenService tokenService;

    public UsersController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Get("/{userId}")
    public HttpResponse<EmailAndAvatarResponse> getUserProfile(@Header("Authorization") String authHeader,
                                                               @PathVariable String userId) {
        Optional<User> user = tokenService.getUserByToken(getToken(authHeader));

        if (user.isEmpty()) {
            return HttpResponse.unauthorized();
        }

        if (!user.get().getId().equals(userId)) {
            return HttpResponse.unauthorized();
        }

        LOG.debug("Returning user's profile: {}", userId);

        return HttpResponse.ok().body(new EmailAndAvatarResponse(user.get().getEmail(), getAvatarUrl(user.get())));
    }

    @Post("/{userId}/avatar")
    public HttpResponse<AvatarResponse> uploadAvatar(@Header("Authorization") String authHeader,
                                                     @Body UploadAvatarRequest request,
                                                     @PathVariable String userId) throws IOException {
        Optional<User> user = tokenService.getUserByToken(getToken(authHeader));

        if (user.isEmpty()) {
            return HttpResponse.unauthorized();
        }

        if (!user.get().getId().equals(userId)) {
            return HttpResponse.unauthorized();
        }

        LOG.debug("Updating user's avatar: {}", userId);

        byte[] bytes = Base64.getDecoder().decode(request.getAvatar());

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            BufferedImage image = ImageIO.read(bis);

            File outputfile = File.createTempFile("tmp", ".jpg");
            ImageIO.write(image, "jpg", outputfile);

            String avatarPath = outputfile.getCanonicalPath();

            LOG.debug("Saved avatar: {}", avatarPath);

            user.get().setAvatarPath(avatarPath);
        }

        return HttpResponse.ok().body(new AvatarResponse(getAvatarUrl(user.get())));
    }

    private String getToken(String authHeader) {
        return authHeader.trim().toLowerCase().replace("bearer ", "");
    }

    private String getAvatarUrl(User user) {
        if (user.getAvatarPath() == null) {
            String hash = Md5Utils.md5Hex(user.getEmail());

            return "https://www.gravatar.com/avatar/" + hash + "?s=300";
        }

        // TODO hard-coded ip
        return "http://10.0.2.2:11001/images/" + user.getId();
    }

}
