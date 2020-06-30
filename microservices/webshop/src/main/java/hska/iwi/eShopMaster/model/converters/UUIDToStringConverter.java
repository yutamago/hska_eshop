package hska.iwi.eShopMaster.model.converters;

import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;
import java.util.UUID;

public class UUIDToStringConverter extends StrutsTypeConverter {

    @Override
    public Object convertFromString(Map map, String[] strings, Class aClass) {
        if(aClass != UUID.class)
            return null;

        UUID[] converted = new UUID[strings.length];
        for (int i = 0; i < strings.length; i++) {
            converted[i] = UUID.fromString(strings[i]);
        }
        return converted;
    }

    @Override
    public String convertToString(Map map, Object o) {
        return o.toString();
    }
}
