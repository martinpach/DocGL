package com.docgl;

import com.docgl.db.*;
import com.docgl.api.LoggedUser;
import com.docgl.entities.*;
import com.docgl.resources.*;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.EnumSet;
import java.util.Optional;

public class DocGLServerApplication extends Application<DocGLServerConfiguration> {

    private final HibernateBundle<DocGLServerConfiguration> hibernate = new HibernateBundle<DocGLServerConfiguration>(Admin.class, Doctor.class, User.class, Patient.class) {
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
        final PatientDAO patientDao = new PatientDAO(hibernate.getSessionFactory());

        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        byte[] key = DocGLServerConfiguration.getJwtTokenSecret();
        environment.jersey().register(new AuthResource(dao,patientDao,key));



        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30)
                .setRequireExpirationTime()
                .setRequireSubject()
                .setVerificationKey(new HmacKey(key))
                .setRelaxVerificationKeyValidation()
                .build();

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<LoggedUser>()
                        .setJwtConsumer(consumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator( new ExampleAuthenticator())
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(LoggedUser.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AdminProfileResource(dao));
        environment.jersey().register(new DoctorResource(docDao));
        environment.jersey().register(new PatientResource(patientDao));
        environment.jersey().getResourceConfig().register(new CustomExceptionMapper());

    }

    private static class ExampleAuthenticator  implements Authenticator<JwtContext, LoggedUser> {
        @Override
        public Optional<LoggedUser> authenticate(JwtContext context) {

            try {
                final String subject = context.getJwtClaims().getSubject();
                final int id = Integer.parseInt(context.getJwtClaims().getClaimValue("id").toString());
                if ("admin".equals(subject)) {
                    return Optional.of(new LoggedUser(UserType.ADMIN, id));
                }
                if("doctor".equals(subject)){
                    return Optional.of(new LoggedUser(UserType.DOCTOR, id));
                }
                if("patient".equals(subject)){
                    return Optional.of(new LoggedUser(UserType.PATIENT, id));
                }
                return Optional.empty();
            }
            catch (MalformedClaimException e) { return Optional.empty(); }
        }
    }

}
