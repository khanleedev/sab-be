package org.project.social_account_business.utils;

public class TenantUtils {
    public static String parseDatabaseNameFromConnectionString(String url, String driverClassName) {
        // Example jdbc:postgresql://127.0.0.1:5432/tenant_service_1?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
        // driverClassName = org.postgresql.Driver or com.mysql.cj.jdbc.Driver
        // Return tenant_service_1
        String driverName = driverClassName.split("\\.")[1].toLowerCase();
        String dataSourceUrl = "jdbc:" + driverName + "://";
        String cleanString = url.substring(dataSourceUrl.length(), url.indexOf("?"));
        return cleanString.substring(cleanString.indexOf("/") + 1);
    }
}
