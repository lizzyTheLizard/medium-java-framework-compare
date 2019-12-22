package lizzy.medium.compare.spring;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface Repository extends CrudRepository<Issue, UUID> {
    List<Issue> findAll();
}
