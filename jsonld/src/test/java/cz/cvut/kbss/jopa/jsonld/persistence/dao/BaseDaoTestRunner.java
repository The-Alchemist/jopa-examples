package cz.cvut.kbss.jopa.jsonld.persistence.dao;

import cz.cvut.kbss.jopa.jsonld.environment.TestPersistenceConfig;
import cz.cvut.kbss.jopa.jsonld.environment.TestUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseDaoTestRunner {

    @Autowired
    private PlatformTransactionManager txManager;

    /**
     * Since JOPA does not understand SPARQL queries, any DAO method using a query will not be able to see uncommitted
     * transactional changes. So the whole test cannot run in a single transaction, as is common in regular Spring testing.
     * <p>
     * Instead, we need to perform methods which change the state of the storage in transactions, so that the changes are
     * really committed into the storage.
     *
     * @param procedure Code to execute
     */
    public void executeInTransaction(Runnable procedure) {
        TestUtils.executeInTransaction(txManager, procedure);
    }
}
