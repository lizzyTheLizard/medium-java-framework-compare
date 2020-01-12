package lizzy.medium.compare.spring;

import java.util.Optional;
import java.util.UUID;

interface Repository {
    Optional<Issue> findById(UUID id);

    Issue insert(Issue body);

    Issue update(Issue body);

    void deleteById(UUID id);

    Iterable<Issue> findAll();
}
