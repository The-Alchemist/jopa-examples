package cz.cvut.kbss.jopa.jsonld.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@PropertySource("classpath:config.properties")
@Configuration
public class PersistenceFactory {

    public static final String URL_PROPERTY = "repositoryUrl";
    public static final String DRIVER_PROPERTY = "driver";

    private static final Map<String, String> PARAMS = initParams();

    @Autowired
    private Environment environment;

    private EntityManagerFactory emf;

    @Bean
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final Map<String, String> properties = new HashMap<>(PARAMS);
        properties.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, environment.getProperty(URL_PROPERTY));
        properties.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, environment.getProperty(DRIVER_PROPERTY));
        this.emf = Persistence.createEntityManagerFactory("jsonldPU", properties);
    }

    @PreDestroy
    private void close() {
        emf.close();
    }

    private static Map<String, String> initParams() {
        final Map<String, String> map = new HashMap<>();
        map.put(OntoDriverProperties.ONTOLOGY_LANGUAGE, "en");
        map.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.jopa.jsonld.model");
        map.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        return map;
    }
}
