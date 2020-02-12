package com.revolut.backend.task.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.revolut.backend.task.dao.AccountDao;
import com.revolut.backend.task.dao.impl.AccountDaoImpl;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountDao.class).to(AccountDaoImpl.class).in(Singleton.class);
    }
}
