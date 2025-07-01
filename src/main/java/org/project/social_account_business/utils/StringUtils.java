package org.project.social_account_business.utils;

import org.apache.commons.lang.RandomStringUtils;

public class StringUtils {
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
