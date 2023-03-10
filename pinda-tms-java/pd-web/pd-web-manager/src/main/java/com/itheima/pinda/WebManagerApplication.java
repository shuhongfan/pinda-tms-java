package com.itheima.pinda;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.itheima.pinda.common.converter.EnumDeserializer;
import com.itheima.pinda.utils.DateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@EnableFeignClients
public class WebManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebManagerApplication.class, args);
    }
    /**
     * serializerByType 解决json中返回的 LocalDateTime 格式问题
     * deserializerByType 解决string类型入参转为 LocalDateTime 格式问题
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_FORMAT)))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_FORMAT)));
    }
    /**
     * 解决序列化和反序列化时，参数转换问题
     * addSerializer：序列化 （Controller 返回 给前端的json）
     * Long -> string
     * BigInteger -> string
     * BigDecimal -> string
     * date -> string
     * <p>
     * addDeserializer: 反序列化 （前端调用接口时，传递到后台的json）
     * 枚举类型: {"code": "xxx"} -> Enum
     *
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.simpleDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMAT);
        ObjectMapper objectMapper = builder.createXmlMapper(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .timeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                .build();

        objectMapper
                .setLocale(Locale.CHINA)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //忽略未知字段
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                // 忽略不能转移的字符
                .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                //单引号处理
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                //日期格式
                .setDateFormat(new SimpleDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMAT));

        //反序列化时，属性不存在的兼容处理
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(BigDecimal.class, ToStringSerializer.instance)
                .addDeserializer(Enum.class, EnumDeserializer.INSTANCE);

        return objectMapper.registerModule(simpleModule);
    }
}
