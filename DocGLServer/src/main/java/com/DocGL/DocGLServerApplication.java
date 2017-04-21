package com.DocGL;

import com.DocGL.DB.AdminDAO;
import com.DocGL.DB.DoctorDAO;
import com.DocGL.api.LoggedUser;
import com.DocGL.entities.Admin;
import com.DocGL.resources.AdminProfileResource;
import com.DocGL.resources.DoctorResource;
import com.DocGL.resources.AuthResource;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Optional;




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
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final DocGLServerConfiguration configuration,
                    final Environment environment) throws UnsupportedEncodingException {

        final AdminDAO dao = new AdminDAO(hibernate.getSessionFactory());
        final DoctorDAO docDao = new DoctorDAO(hibernate.getSessionFactory());
        environment.jersey().register(new AuthResource(dao,DocGLServerConfiguration.getJwtTokenSecret()));

        byte[] key = DocGLServerConfiguration.getJwtTokenSecret();

        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30)
                .setRequireExpirationTime()
                .setRequireSubject()
                .setVerificationKey(new HmacKey(key))
                .setRelaxVerificationKeyValidation()
                .build();

        environment.jersey().register(CORSResponseFilter.class);

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<LoggedUser>()
                        .setJwtConsumer(consumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator( new ExampleAuthenticator())
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Principal.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AdminProfileResource(dao));
        environment.jersey().register(new DoctorResource(docDao));
    }

    private static class ExampleAuthenticator  implements Authenticator<JwtContext, LoggedUser> {
        @Override
        public Optional<LoggedUser> authenticate(JwtContext context) {

            try {
                final String subject = context.getJwtClaims().getSubject();
                final int id = Integer.parseInt(context.getJwtClaims().getClaimValue("id").toString());
                if ("admin".equals(subject)) {
                    return Optional.of(new LoggedUser("admin", id));
                }
                if("doctor".equals(subject)){
                    return Optional.of(new LoggedUser("doctor", id));
                }
                if("user".equals(subject)){
                    return Optional.of(new LoggedUser("user", id));
                }
                return Optional.empty();
            }
            catch (MalformedClaimException e) { return Optional.empty(); }
        }
    }


}
