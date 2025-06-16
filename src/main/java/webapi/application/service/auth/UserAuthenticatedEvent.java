package webapi.application.service.auth;


import org.springframework.context.ApplicationEvent;
import webapi.domain.AuthUser;

public class UserAuthenticatedEvent extends ApplicationEvent {
    private final AuthUser authUser;
    private final String userAgent;

    public UserAuthenticatedEvent(Object source, AuthUser authUser, String userAgent) {
        super(source);
        this.authUser = authUser;
        this.userAgent = userAgent;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public String getUserAgent() {
        return userAgent;
    }
}