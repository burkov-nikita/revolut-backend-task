package com.revolut.backend.task.dao.util;


import com.revolut.backend.task.entity.Account;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

import static java.lang.String.valueOf;
import static java.util.UUID.nameUUIDFromBytes;
import static java.util.UUID.randomUUID;

public class AccountCustomUuidGenerator implements IdentifierGenerator, Configurable {

    private String accountNumber;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        Optional.ofNullable(params.get("accountNumber")).ifPresent(str -> accountNumber = valueOf(str));
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return randomUUID().toString();
        } else {
            return nameUUIDFromBytes(getSourceString(object).getBytes());
        }
    }

    private static String getSourceString(Object src) {
        return ((Account) src).getNum();
    }
}
