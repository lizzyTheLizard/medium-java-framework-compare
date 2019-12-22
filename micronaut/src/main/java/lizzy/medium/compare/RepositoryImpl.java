package lizzy.medium.compare;

import com.sun.istack.NotNull;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import io.micronaut.spring.tx.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RepositoryImpl implements Repository {
    @PersistenceContext
    private EntityManager entityManager;

    public RepositoryImpl(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Issue> findById(@NotNull UUID id) {
        return Optional.ofNullable(entityManager.find(Issue.class, id));
    }

    @Override
    @Transactional
    public Issue insert(@NotNull Issue issue) {
        entityManager.persist(issue);
        return issue;
    }

    @Override
    @Transactional
    public void deleteById(@NotNull UUID id) {
        findById(id).ifPresent(issue -> entityManager.remove(issue));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> findAll() {
        String qlString = "SELECT g FROM Issue as g";
        TypedQuery<Issue> query = entityManager.createQuery(qlString, Issue.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Issue update(@NotNull Issue issue) {
        entityManager.createQuery("UPDATE Issue g SET name = :name, description = :description where id = :id")
                .setParameter("name", issue.getName())
                .setParameter("description", issue.getDescription())
                .setParameter("id", issue.getId())
                .executeUpdate();
        return findById(issue.getId()).orElseThrow();
    }
}
