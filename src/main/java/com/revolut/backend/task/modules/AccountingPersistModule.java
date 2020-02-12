package com.revolut.backend.task.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class AccountingPersistModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new JpaPersistModule("accountingJpaUnit"));
        bind(JPAInitializer.class).asEagerSingleton();
    }

    @Singleton
    public static class JPAInitializer {
        @Inject
        public JPAInitializer(final PersistService service) {
            service.start();
        }
    }
}
