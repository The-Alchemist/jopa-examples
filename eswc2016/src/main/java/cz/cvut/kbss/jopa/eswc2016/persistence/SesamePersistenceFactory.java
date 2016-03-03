/**
 * Copyright (C) 2016 Czech Technical University in Prague
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jopa.eswc2016.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.eswc2016.util.Constants;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.owlapi.config.OwlapiOntoDriverProperties;
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides entity manager factory as a Spring bean.
 * <p/>
 * This class provides access to Sesame persistence.
 */
@Configuration
@PropertySource("classpath:config.properties")
public class SesamePersistenceFactory {

    private static final String URL_PROPERTY = "sesame.repositoryUrl";
    private static final String DRIVER_PROPERTY = "sesame.driver";

    static final Map<String, String> PARAMS = initParams();

    @Autowired
    Environment environment;

    private EntityManagerFactory emf;

    @Bean(name = "sesameEMF")
    public EntityManagerFactory entityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final Map<String, String> properties = new HashMap<>(PARAMS);
        properties.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, environment.getProperty(URL_PROPERTY));
        properties.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, environment.getProperty(DRIVER_PROPERTY));
        this.emf = Persistence.createEntityManagerFactory("eswc2016-sesame", properties);
    }

    @PreDestroy
    private void close() {
        emf.close();
    }

    private static Map<String, String> initParams() {
        final Map<String, String> map = new HashMap<>();
        map.put(OntoDriverProperties.ONTOLOGY_LANGUAGE, Constants.LANGUAGE);
        map.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.jopa.eswc2016.model");
        map.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        map.put(SesameOntoDriverProperties.SESAME_USE_INFERENCE, Boolean.TRUE.toString());
        return Collections.unmodifiableMap(map);
    }
}
