package org.nh.artha.elastic.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchSimpleTypes;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ElasticSearchCustomConverter extends CustomConversions {
    private static final StoreConversions STORE_CONVERSIONS;
    private static final List<Converter<?, ?>> STORE_CONVERTERS;
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchCustomConverter.class);
    static {

        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(LocalDateTimeToStringConverter.INSTANCE);
        converters.add(LocalDateToStringConverter.INSTANCE);
        converters.add(DateToStringConverter.INSTANCE);
        converters.add(ZonedDateTimeToStringConverter.INSTANCE);
        converters.add(DoubleToBigDecimalConverter.INSTANCE);
        converters.add(BigDecimalToDoubleConverter.INSTANCE);
        converters.add(LocalTimeToStringConverter.INSTANCE);
        converters.add(InstantToStringConverter.INSTANCE);
        converters.add(StringToDateConverter.INSTANCE);
        STORE_CONVERTERS = Collections.unmodifiableList(converters);
        STORE_CONVERSIONS = StoreConversions.of(ElasticsearchSimpleTypes.HOLDER, STORE_CONVERTERS);
    }

    /**
     * Creates a new {@link CustomConversions} instance registering the given converters.
     *
     * @param converters must not be {@literal null}.
     */
    public ElasticSearchCustomConverter(Collection<?> converters) {
        super(STORE_CONVERSIONS, converters);
    }

    @ReadingConverter
    public enum StringToDateConverter implements Converter<String, Date> {
        INSTANCE;

        @Override
        @NonNull
        public Date convert(String source) {
            return Date.from(LocalDateTime.parse(source, DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")).atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @WritingConverter
    public enum DateToStringConverter implements Converter<Date, String> {
        INSTANCE;

        @Override
        @NonNull
        public String convert(Date source) {
            return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()));
        }
    }

    @WritingConverter
    public enum LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {

        INSTANCE;

        @Override
        public String convert(LocalDateTime source) {
            return source.toString();
        }
    }

    @WritingConverter
    public enum LocalTimeToStringConverter implements Converter<LocalTime, String> {

        INSTANCE;

        @Override
        public String convert(LocalTime source) {
            return source.toString();
        }
    }

    @WritingConverter
    public enum LocalDateToStringConverter implements Converter<LocalDate, String> {

        INSTANCE;

        @Override
        public String convert(LocalDate source) {
            return source.toString();
        }
    }

    @WritingConverter
    public enum ZonedDateTimeToStringConverter implements Converter<ZonedDateTime, String> {

        INSTANCE;

        @Override
        public String convert(ZonedDateTime source) {
            return source.toString();
        }
    }

    @WritingConverter
    public enum InstantToStringConverter implements Converter<Instant, String> {

        INSTANCE;

        @Override
        public String convert(Instant source) {
            return source.toString();
        }
    }

    @WritingConverter
    public enum BigDecimalToDoubleConverter implements Converter<BigDecimal, Double> {

        INSTANCE;

        @Override
        public Double convert(BigDecimal source) {
            return source.doubleValue();
        }
    }

    @ReadingConverter
    public enum DoubleToBigDecimalConverter implements Converter<Double,BigDecimal> {

        INSTANCE;

        @Override
        public BigDecimal convert(Double source) {
            if(null == source)
                return null;
            return BigDecimal.valueOf(source);
        }
    }


    public static void addElasticConverters(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        ConverterRegistry conversionService = (ConverterRegistry) (elasticsearchRestTemplate.getElasticsearchConverter()).getConversionService();
        conversionService.addConverter(ElasticSearchCustomConverter.LocalDateTimeToStringConverter.INSTANCE);
        conversionService.addConverter(ElasticSearchCustomConverter.LocalDateToStringConverter.INSTANCE);
        conversionService.addConverter(ElasticSearchCustomConverter.ZonedDateTimeToStringConverter.INSTANCE);
        conversionService.addConverter(ElasticSearchCustomConverter.DateToStringConverter.INSTANCE);
        conversionService.addConverter(DoubleToBigDecimalConverter.INSTANCE);
        conversionService.addConverter(BigDecimalToDoubleConverter.INSTANCE);
        conversionService.addConverter(LocalTimeToStringConverter.INSTANCE);
        conversionService.addConverter(InstantToStringConverter.INSTANCE);
        conversionService.addConverter(StringToDateConverter.INSTANCE);
        ((MappingElasticsearchConverter)elasticsearchRestTemplate.getElasticsearchConverter()).setConversions(new ElasticSearchCustomConverter(Collections.EMPTY_LIST));
    }

}
