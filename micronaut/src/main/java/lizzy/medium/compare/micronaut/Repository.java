package lizzy.medium.compare.micronaut;

import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

@io.micronaut.data.annotation.Repository
interface Repository extends CrudRepository<Issue, UUID> {
}
