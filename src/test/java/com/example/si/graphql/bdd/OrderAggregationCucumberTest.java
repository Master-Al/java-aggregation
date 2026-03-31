package com.example.si.graphql.bdd;

import io.cucumber.core.cli.Main;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrderAggregationCucumberTest {

    private static final Logger LOG = Logger.getLogger(OrderAggregationCucumberTest.class);

    @Test
    void shouldRunBddScenario() {
        LOG.info("Starting Cucumber BDD execution for the Camel aggregation flow");

        byte exitStatus = Main.run(new String[]{
                "--glue", "com.example.si.graphql.bdd",
                "--plugin", "pretty",
                "classpath:features"
        }, Thread.currentThread().getContextClassLoader());

        LOG.infof("Finished Cucumber BDD execution with exit status %d", exitStatus);
        Assertions.assertEquals(0, exitStatus, "Expected all Cucumber scenarios to pass");
    }
}
