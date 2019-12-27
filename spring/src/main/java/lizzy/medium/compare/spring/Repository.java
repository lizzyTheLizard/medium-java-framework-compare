package lizzy.medium.compare.spring;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

@org.springframework.stereotype.Repository
interface Repository extends CrudRepository<Issue, UUID> {
}
