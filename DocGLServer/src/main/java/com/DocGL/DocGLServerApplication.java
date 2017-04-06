package com.DocGL;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DocGLServerApplication extends Application<DocGLServerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DocGLServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "DocGLServer";
    }

    @Override
    public void initialize(final Bootstrap<DocGLServerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final DocGLServerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
