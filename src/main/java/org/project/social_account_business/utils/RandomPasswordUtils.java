package org.project.social_account_business.utils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomPasswordUtils {
    private String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&?{}*";
    private Integer POSTFIX_LENGTH = 8;

    private static final Set<String> passwordSet = new HashSet<>();

    public String createPassword() {
        String password;
        do {
            password = generateRandomPassword();
        } while (passwordSet.contains(password));
        synchronized (passwordSet) {
            passwordSet.add(password);
        }
        return password;
    }

    private String generateRandomPassword() {
        StringBuilder builder = new StringBuilder();
        builder.append("PMS-P@ssw0rd-");
        for (int i = 0; i < POSTFIX_LENGTH; i++) {
            int character = ThreadLocalRandom.current().nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}
