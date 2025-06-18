package webapi.infrastructure.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import webapi.application.service.auth.UserService;
import webapi.infrastructure.common.Constants;
import webapi.infrastructure.exception.ForbiddenException;
import webapi.infrastructure.helper.UserUtils;

import java.util.List;

@Aspect
@Component

public class PermissionCheckAspect {
    private final UserService userService;

    public PermissionCheckAspect(UserService userService) {
        this.userService = userService;
    }

    @Pointcut("@annotation(PermissionsAllowed)")
    public void permissionAllowedAnnotationPointcut() {}

    @Before("permissionAllowedAnnotationPointcut() && @annotation(permissionsAllowed)")
    public void checkRoles(PermissionsAllowed permissionsAllowed) {
        if (permissionsAllowed.value().length == 0) {
            return;
        }
        List<String> userPermissions = userService.getPermissionOfUsers(UserUtils.getUserId());
        if(userPermissions.contains(Constants.MoocPosition.ADMIN)) return;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Access Denied");
        }
        boolean hasPermission = false;
        var currentUserPermissions = userService.getCurrentPermissionOfUser();
        currentUserPermissions.add("any");
        for (String permission : permissionsAllowed.value()) {
            if (currentUserPermissions.contains(permission)) {
                hasPermission = true;
                break;
            }
        }
        if (!hasPermission) {
            throw new ForbiddenException("Không có quyền truy cập");
        }
    }
}
