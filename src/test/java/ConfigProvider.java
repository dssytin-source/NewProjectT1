import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigProvider.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Конфигурационный файл 'config.properties' не найден в resources");
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

    public static String getLoggingLevel() {
        return properties.getProperty("logging.level");
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
        return Integer.parseInt(properties.getProperty("product.price", "100"));
    }

    public static void printConfig() {
        System.out.println("========== ТЕКУЩАЯ КОНФИГУРАЦИЯ ЗАПУСКА ==========");
        System.out.println("URL стенда:          " + getAppUrl());
        System.out.println("URL API:             " + getApiUrl());
        System.out.println("Тайм-аут элементов:  " + getTimeout() + " ms");
        System.out.println("Режим логирования:   " + getLoggingLevel());
        System.out.println("Стартовый товар:     " + getProductName());
        System.out.println("Цена товара:         " + getProductPrice() + " руб.");
        System.out.println("Credentials:         [СКРЫТО В ЦЕЛЯХ БЕЗОПАСНОСТИ]");
        System.out.println("==================================================");
    }
}