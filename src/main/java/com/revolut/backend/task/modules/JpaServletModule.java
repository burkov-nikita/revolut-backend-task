package com.revolut.backend.task.modules;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

public class JpaServletModule extends ServletModule {
    protected void configureServlets() {
        install(new JpaPersistModule("accountingJpaUnit"));

        filter("/*").through(PersistFilter.class);
    }
}
