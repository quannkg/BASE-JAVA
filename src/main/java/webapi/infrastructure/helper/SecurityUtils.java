package webapi.infrastructure.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static webapi.infrastructure.config.UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String)) {

            Object principal = authentication.getPrincipal();

            // Check if the principal is the correct UserDetails type
            if (principal instanceof webapi.infrastructure.config.UserDetails) {
                return (webapi.infrastructure.config.UserDetails) principal;
            }

            // Log for debugging if needed
            System.out.println("Principal type: " + principal.getClass().getName());
        }
        return null;
    }

    // Alternative method if you need the helper UserDetails
    public static webapi.infrastructure.helper.UserDetails getCurrentUserDetailsAsHelper() {
        webapi.infrastructure.config.UserDetails configUserDetails = getCurrentUserDetails();
        if (configUserDetails != null) {
            // Convert or map from config.UserDetails to helper.UserDetails
            // You'll need to implement this conversion based on your classes
            return convertToHelperUserDetails(configUserDetails);
        }
        return null;
    }

    private static webapi.infrastructure.helper.UserDetails convertToHelperUserDetails(
            webapi.infrastructure.config.UserDetails configUserDetails) {
        // Implement conversion logic here based on your UserDetails classes
        // This is just a placeholder - you'll need to adapt this to your actual classes
        return new webapi.infrastructure.helper.UserDetails(
                configUserDetails.getUsername(),
                configUserDetails.getPassword(),
                configUserDetails.getAuthorities()
        );
    }
}