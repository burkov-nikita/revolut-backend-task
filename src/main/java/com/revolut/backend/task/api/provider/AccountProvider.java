package com.revolut.backend.task.api.provider;

import com.revolut.backend.task.api.annotation.AccountNumberToUUID;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.UUID;

import static com.revolut.backend.task.util.AccountGenerator.getUUID;
import static java.lang.String.valueOf;
import static java.util.stream.Stream.of;

@Provider
public class AccountProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        return of(annotations).noneMatch(a -> a instanceof AccountNumberToUUID) ? null : (ParamConverter<T>) new UuidParamConverter();
    }

    class UuidParamConverter implements ParamConverter<UUID> {

        @Override
        public UUID fromString(String value) {
            return getUUID(value);
        }

        @Override
        public String toString(UUID value) {
            return valueOf(value);
        }
    }
}
