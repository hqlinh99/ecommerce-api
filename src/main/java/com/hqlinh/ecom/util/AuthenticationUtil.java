package com.hqlinh.ecom.util;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class AuthenticationUtil {

    public static void attachAccountId(Authentication authentication, String accountId) {
        Object originalDetails = authentication.getDetails();
        if (originalDetails instanceof Details details) {
            details.setAccountId(accountId);
        } else {
            Details details = new Details()
                    .setOriginal(originalDetails)
                    .setAccountId(accountId);
            ((OAuth2AuthenticationToken) authentication).setDetails(details);
        }
    }

    public static String retrieveAccountId(Authentication authentication) {
        Details details = (Details) authentication.getDetails();
        return details.getAccountId();
    }

    @Data
    @Accessors(chain = true)
    private static class Details {

        private Object original;
        private String accountId;

    }

}