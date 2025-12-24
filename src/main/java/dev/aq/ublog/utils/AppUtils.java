package dev.aq.ublog.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AppUtils {

  private AppUtils() {
    throw new IllegalStateException("Util class can't be instantiated.");
  }


  /**
   * @param obj
   * @return true if value of any obj property is null, otherwise false
   */

  public static Boolean anyEmpty(Object obj) {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> mappedObj = objectMapper.convertValue(obj, Map.class);
    boolean result = false;

    List<String> idList = Arrays.asList("userId", "categoryId");

    for (Map.Entry<String, Object> entry : mappedObj.entrySet()) {
      if (idList.contains(entry.getKey()) && mappedObj.get(entry.getKey()) == null) {
        continue;
      }
      if (mappedObj.get(entry.getKey()) == null) {
        result = true;
        break;
      }
    }

    return result;
  }

  /**
   * @param newObj - new data
   * @param oldObj - existing data
   * @return an Object of type oldObj
   */

  public static Object saveNewToOld(Object newObj, Object oldObj) {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> mappedNewObj = objectMapper.convertValue(newObj, Map.class);
    Map<String, Object> mappedOldObj = objectMapper.convertValue(oldObj, Map.class);

    mappedNewObj.forEach((key, value) -> {
      if (!Objects.equals(key, "id") && mappedNewObj.get(key) != null) {
        mappedOldObj.put(key, mappedNewObj.get(key));
      }
    });

    /**
     for (Object key : mappedNewObj.keySet()) {
     if (key != "id" && mappedNewObj.get(key) != null) {
     mappedOldObj.put((String) key, mappedNewObj.get(key));
     }
     }
     */
    return objectMapper.convertValue(mappedOldObj, new TypeReference<Object>() {
    });
  }

  public static String dateFormatter(LocalDateTime localDateTime) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    return localDateTime.format(dateTimeFormatter);
  }

}
