package lizzy.medium.compare;

import java.util.UUID;

import io.micronaut.data.repository.CrudRepository;

@io.micronaut.data.annotation.Repository
interface Repository extends CrudRepository<Issue, UUID> {
}
