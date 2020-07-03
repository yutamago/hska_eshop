package hska.iwi.eShopMaster.model.sessionFactory.util;

import com.fasterxml.jackson.databind.annotation.JsonAppend;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static PropertiesLoader INSTANCE = new PropertiesLoader();
    public static PropertiesLoader getINSTANCE() {
        return INSTANCE;
    }

    private Properties properties;

    private PropertiesLoader(){
        properties = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD application.properties");
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public static String get(String propertyName) {
        return INSTANCE.properties.getProperty(propertyName);
    }
}
