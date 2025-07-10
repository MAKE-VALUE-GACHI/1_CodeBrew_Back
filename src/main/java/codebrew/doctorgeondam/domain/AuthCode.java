package codebrew.doctorgeondam.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthCode {

    private AuthCodeId id;
    private String code;

    @AllArgsConstructor
    public static class AuthCodeId {

        public Long id;
    }

}
