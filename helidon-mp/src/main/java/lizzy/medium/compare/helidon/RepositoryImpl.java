package lizzy.medium.compare.helidon;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RepositoryImpl implements Repository {
    @PersistenceContext
    private
    EntityManager entityManager;

    @Override
    public Optional<Issue> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Issue.class, id));
    }

    @Override
    public Issue insert(Issue issue) {
        entityManager.persist(issue);
        return issue;
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(issue -> entityManager.remove(issue));
    }

    @Override
    public List<Issue> findAll() {
        String qlString = "SELECT g FROM Issue as g";
        TypedQuery<Issue> query = entityManager.createQuery(qlString, Issue.class);
        return query.getResultList();
    }

    @Override
    public Issue update(Issue issue) {
        entityManager.createQuery("UPDATE Issue g SET name = :name, description = :description where id = :id")
                .setParameter("name", issue.getName())
                .setParameter("description", issue.getDescription())
                .setParameter("id", issue.getId())
                .executeUpdate();
        return findById(issue.getId()).orElseThrow(RuntimeException::new);
    }
}
