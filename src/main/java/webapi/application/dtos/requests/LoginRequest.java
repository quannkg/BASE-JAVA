package webapi.application.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webapi.infrastructure.common.Constants;
import webapi.infrastructure.common.enumm.LoginIdentifierEnum;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.core.io.buffer.DataBufferUtils.matcher;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    private String poolSource;
    @JsonIgnore
    public Boolean isEmail() {
        return this.username.contains("@");
    }
    @JsonIgnore
    public LoginIdentifierEnum getLoginType(String source) {
        if (matcher(this.getUsername(), Constants.MoocRegex.EMAIL_REGEX)) {
            return LoginIdentifierEnum.EMAIL;
        }else if (matcher(this.getUsername(), Constants.MoocRegex.PHONE_REGEX)
                && Objects.nonNull(source)
                && source.equalsIgnoreCase("qt")) {
            return LoginIdentifierEnum.PHONE_NUMBER;
        }
        return LoginIdentifierEnum.USERNAME;
    }
    private boolean matcher(String username, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
