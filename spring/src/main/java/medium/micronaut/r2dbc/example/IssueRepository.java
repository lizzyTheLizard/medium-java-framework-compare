package medium.micronaut.r2dbc.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IssueRepository extends CrudRepository<Issue, UUID> {
}
