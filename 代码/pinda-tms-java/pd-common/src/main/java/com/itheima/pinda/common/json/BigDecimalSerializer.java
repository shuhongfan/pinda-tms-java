package com.itheima.pinda.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

@Slf4j
@JsonComponent
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {
    private String format = "##0.00";

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(new DecimalFormat(format).format(bigDecimal));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), BigDecimal.class)) {
                BigDecimalFormat bigDecimalFormat = beanProperty.getAnnotation((BigDecimalFormat.class));
                if (bigDecimalFormat == null) {
                    bigDecimalFormat = beanProperty.getContextAnnotation(BigDecimalFormat.class);
                }
                BigDecimalSerializer bigDecimalSerializer = new BigDecimalSerializer();
                if (bigDecimalFormat != null) {
                    bigDecimalSerializer.format = bigDecimalFormat.value();
                }
                return bigDecimalSerializer;
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}
