package com.revolut.backend.task;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.backend.task.modules.JpaServletModule;
import com.revolut.backend.task.modules.GuiceModule;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import static org.jvnet.hk2.guice.bridge.api.GuiceBridge.getGuiceBridge;

@ApplicationPath("/")
public class ApplicationConfig extends ResourceConfig {

    @Inject
    public ApplicationConfig(ServiceLocator serviceLocator) {
        packages("com.revolut.backend.task");
        Injector injector =  Guice.createInjector(
                new GuiceModule(),
                new JpaServletModule()
        );
        initGuiceIntoHK2Bridge(serviceLocator, injector);
    }

    private void initGuiceIntoHK2Bridge(ServiceLocator serviceLocator, Injector injector) {
        getGuiceBridge().initializeGuiceBridge(serviceLocator);
        serviceLocator
                .getService(GuiceIntoHK2Bridge.class)
                .bridgeGuiceInjector(injector);
    }
}
