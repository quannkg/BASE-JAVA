package webapi.infrastructure.helper;


import java.util.Optional;

public class UserUtils {
    public static Integer getUserId(){
        return Optional.ofNullable(SecurityUtils.getCurrentUserDetails())
                .map(UserDetails::getUserId)
                .map(Long::intValue)
                .orElse(null);
    }
}
