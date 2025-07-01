package org.project.social_account_business.utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.project.social_account_business.exception.UnregisteredCurrencyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class ConvertUtils {
    public static Long convertStringToLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (Exception e) {
            return 0L;
        }
    }

    public static int convertToCent(double b) {
        int i = (int) b;
        double k = b - i;
        if (k > 0.5 && k < 1) {
            i += 1;
        }
        return i;
    }
}
