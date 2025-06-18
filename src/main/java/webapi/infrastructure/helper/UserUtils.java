package webapi.infrastructure.helper;


import java.util.Optional;

public class UserUtils {
    public static Integer getUserId() {
        return Optional.ofNullable(SecurityUtils.getCurrentUserDetails())
                .map(userDetails -> {
                    if (userDetails instanceof webapi.infrastructure.config.UserDetails) {
                        return ((webapi.infrastructure.config.UserDetails) userDetails).getUserId();
                    }
                    return null;
                })
                .map(userId -> userId != null ? userId.intValue() : null)
                .orElse(null);
    }

    // Alternative cleaner approach
    public static Integer getUserIdClean() {
        webapi.infrastructure.config.UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
        if (userDetails != null && userDetails.getUserId() != null) {
            return userDetails.getUserId().intValue();
        }
        return null;
    }

    // If you want to keep it as Optional chain
    public static Integer getUserIdOptional() {
        return Optional.ofNullable(SecurityUtils.getCurrentUserDetails())
                .filter(userDetails -> userDetails.getUserId() != null)
                .map(userDetails -> userDetails.getUserId().intValue())
                .orElse(null);
    }
}
