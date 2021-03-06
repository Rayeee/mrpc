package com.kongzhong.mrpc.serialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kongzhong.mrpc.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Jackson JSON序列化实现
 *
 * @author biezhi
 *         2017/5/11
 */
@Slf4j
public class JacksonSerialize {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(initModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    private static SimpleModule initModule() {
        return new SimpleModule().
//                addSerializer(BigDecimal.class, new BigDecimalSerializer()).
        addSerializer(LocalTime.class, new LocalTimeSerializer()).
                        addDeserializer(LocalTime.class, new LocalTimeDeserializer()).
                        addSerializer(LocalDate.class, new LocalDateSerializer()).
                        addDeserializer(LocalDate.class, new LocalDateDeserializer()).
                        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer()).
                        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    }

    public static JavaType getJavaType(Type type) {
        return mapper.getTypeFactory().constructType(type);
    }

    /**
     * Java对象转JSON字符串
     *
     * @param object
     * @return
     */
    public String toJSONString(Object object) throws SerializeException {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Object to json stirng error", e);
            throw new SerializeException(e);
        }
    }

    public String toJSONString(Object object, boolean pretty) throws SerializeException {
        if (!pretty) {
            return toJSONString(object);
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            log.error("Object to json stirng error", e);
            throw new SerializeException(e);
        }
    }

    /**
     * json字符串转Java对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public <T> T parseObject(String json, Class<T> type) throws SerializeException {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            log.error("Json parse to object error", e);
            throw new SerializeException(e);
        }
    }

    /**
     * json转obj
     */
    public <T> T parseObject(String json, Type type) {
        try {
            return mapper.readValue(json, genJavaType(type));
        } catch (Exception e) {
            log.error("Json parse to object error", e);
        }
        return null;
    }

    private JavaType genJavaType(Type type) {
        return mapper.getTypeFactory().constructType(type);
    }
}
