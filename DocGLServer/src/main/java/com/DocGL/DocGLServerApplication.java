package com.DocGL;

import com.DocGL.DB.AdminDAO;
import com.DocGL.api.Admin;
import com.DocGL.resources.AdminResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DocGLServerApplication extends Application<DocGLServerConfiguration> {

    private final HibernateBundle<DocGLServerConfiguration> hibernate = new HibernateBundle<DocGLServerConfiguration>(Admin.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(DocGLServerConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

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
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final DocGLServerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application

        final AdminDAO dao = new AdminDAO(hibernate.getSessionFactory());
        environment.jersey().register(new AdminResource(dao));
    }





}
