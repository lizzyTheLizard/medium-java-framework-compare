package medium.micronaut.r2dbc.example;

import io.micronaut.http.annotation.Controller;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
class Repository {
    private final ConnectionPool connectionPool;

    Mono<Issue> findById(UUID id) {
        return connectionPool.create().flatMap(connection -> {
            final var statement = connection.createStatement("SELECT * FROM issue WHERE id=$1");
            statement.bind(0, id);
            return Mono.from(statement.execute())
                    .map(result -> result.map(this::convertToIssue))
                    .flatMap(Mono::from)
                    .doOnTerminate(connection::close);
        });
    }

    Flux<Issue> findAll() {
        return connectionPool.create().flatMapMany(connection -> {
            final var statement = connection.createStatement("SELECT * FROM issue");
            return Mono.from(statement.execute())
                    .map(result -> result.map(this::convertToIssue))
                    .flatMapMany(Flux::from);
        });
    }

    private Issue convertToIssue(Row r, RowMetadata rm) {
        final var id = (UUID) r.get("id");
        final var name = (String) r.get("name");
        final var description = (String) r.get("description");
        return new Issue(id, name, description);
    }

    Mono<Void> insert(Issue issue) {
        return connectionPool.create().flatMap(connection -> {
            final var statement = connection.createStatement("INSERT INTO issue (id, name, description) VALUES($1, $2, $3)");
            statement.bind(0, issue.getId());
            statement.bind(1, issue.getName());
            statement.bind(2, issue.getDescription());
            return Mono.from(statement.execute())
                    .flatMap(this::checkOneRowUpdated);
        });
    }

    Mono<Void> update(UUID id, Issue issue) {
        return connectionPool.create().flatMap(connection -> {
            final var statement = connection.createStatement("UPDATE issue SET name=$2, description=$3 WHERE id=$1");
            statement.bind(0, id);
            statement.bind(1, issue.getName());
            statement.bind(2, issue.getDescription());
            return Mono.from(statement.execute())
                    .flatMap(this::checkOneRowUpdated);
        });
    }

    Mono<Void> deleteById(UUID id) {
        return connectionPool.create().flatMap(connection -> {
            final var statement = connection.createStatement("DELETE FROM issue WHERE id=$1");
            statement.bind(0, id);
            return Mono.from(statement.execute())
                    .flatMap(this::checkOneRowUpdated);
        });
    }

    private Mono<Void> checkOneRowUpdated(Result result) {
        return Mono.from(result.getRowsUpdated())
                .flatMap(rows -> rows != 1 ? Mono.error(new RuntimeException("Issue not found")) : Mono.empty());
    }
}
