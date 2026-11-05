package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigProvider.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Конфигурационный файл 'config.properties' не найден в resources модуля common");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурационного файла", e);
        }
    }

    public static String getAppUrl() {
        return properties.getProperty("app.url");
    }

    public static String getApiUrl() {
        return properties.getProperty("api.url");
    }

    public static long getTimeout() {
        return Long.parseLong(properties.getProperty("timeout", "6000"));
    }

    public static String getAdminUsername() {
        return properties.getProperty("admin.username");
    }

    public static String getAdminPassword() {
        return properties.getProperty("admin.password");
    }

    public static String getProductName() {
        return properties.getProperty("product.name");
    }

    public static int getProductPrice() {
        return Integer.parseInt(properties.getProperty("product.price", "150"));
    }
}