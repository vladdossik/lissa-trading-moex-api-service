package lissa.trading.moexapi.service.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import lissa.trading.moexapi.service.dto.moex.ContentDto;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ContentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public <T>T contentToPojo(ContentDto contentDto, Class<T> clazz) {
        log.debug("Mapping MoexContent: {} to {}", contentDto, clazz.getSimpleName());
        T instance = clazz.getDeclaredConstructor().newInstance();
        List<List<Object>> values = contentDto.getData();

        for (List<Object> value : values) {
            String fieldName = value.get(0).toString().toLowerCase();
            Object fieldValue = value.get(1);
            fieldAssign(fieldName, fieldValue, clazz, instance);
        }
        return instance;
    }

    public <T> T contentToPojoUsingColumns(ContentDto contentDto, Class<T> clazz) {

        log.debug("Mapping MoexContent using columns: {} to {}", contentDto, clazz.getSimpleName());

        List<Object> value = contentDto.getData().get(0);
        List<String> columns = contentDto.getColumns();

        return valuesAndColumnsToPojo(value, columns, clazz);
    }

    public <T> List<T> contentToPojoListUsingColumns(ContentDto contentDto, Class<T> clazz) {
        log.debug("Mapping MoexContent to pojo list using columns: {} to {}", contentDto, clazz.getSimpleName());

        List<String> columns = contentDto.getColumns();
        List<T> instances = new ArrayList<>();

        contentDto.getData().forEach(value -> {
            instances.add(valuesAndColumnsToPojo(value, columns, clazz));
        });
        return instances;
    }

    @SneakyThrows
    private <T>T valuesAndColumnsToPojo(List<Object> values, List<String> columns, Class<T> clazz) {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (int i = 0; i < columns.size(); i++) {
            String fieldName = columns.get(i).toLowerCase();
            Object fieldValue = values.get(i);
            fieldAssign(fieldName, fieldValue, clazz, instance);
        }
        return instance;
    }

    private <T> void fieldAssign(String fieldName, Object fieldValue, Class<T> clazz, T instance ) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, castToFieldValue(field, fieldValue));
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            log.debug("No suitable field for assign: {}", e.getMessage());
        }
    }

    private Object castToFieldValue(Field field, Object value) {
        Class<?> fieldType = field.getType();

        if (fieldType.isAssignableFrom(Long.class)) {
            return ((Number) value).longValue();
        }

        if (fieldType.isAssignableFrom(Double.class)) {
            return ((Number) value).doubleValue();
        }

        if (fieldType.isAssignableFrom(OffsetDateTime.class)) {
            LocalDateTime localDateTime = LocalDateTime.parse(value.toString(), FORMATTER);
            ZoneId zoneId = ZoneId.systemDefault();
            return localDateTime.atZone(zoneId).toOffsetDateTime();
        }

        return value;
    }
}
