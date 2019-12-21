package medium.micronaut.r2dbc.example;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;

@Factory
public class DatabaseConfiguration {

    @Bean
    public ConnectionPool connectionPool() {
        final var baseOptions = ConnectionFactoryOptions.parse("r2dbc:postgresql://postgres:postgres@postgres");
        final var options = ConnectionFactoryOptions.builder().from(baseOptions).build();
        final var factory = ConnectionFactories.get(options);
        final var poolOptions = ConnectionPoolConfiguration.builder(factory).build();
        return new ConnectionPool(poolOptions);
    }
}
