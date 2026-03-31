package com.example.si.graphql.bdd;

import io.cucumber.core.cli.Main;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrderAggregationCucumberTest {

    @Test
    void shouldRunBddScenario() {
        byte exitStatus = Main.run(new String[]{
                "--glue", "com.example.si.graphql.bdd",
                "--plugin", "pretty",
                "classpath:features"
        }, Thread.currentThread().getContextClassLoader());

        Assertions.assertEquals(0, exitStatus, "Expected all Cucumber scenarios to pass");
    }
}
