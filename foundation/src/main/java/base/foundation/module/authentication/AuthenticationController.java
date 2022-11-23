package base.foundation.module.authentication;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

@Controller
public abstract class AuthenticationController{

    @Post("/login")
    abstract BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);
}