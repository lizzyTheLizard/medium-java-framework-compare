package lizzy.medium.compare.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class RepositoryImpl implements Repository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Issue> findById(UUID id) {
        return Optional.ofNullable(jdbcTemplate.query("SELECT id, name, description FROM Issue WHERE id = ?",
                new Object[]{id}, rs -> { rs.next(); return convert(rs);}));
    }

    @Override
    public Issue insert(Issue issue) {
        jdbcTemplate.update("INSERT INTO Issue(id, name, description) VALUES (?,?,?)",
                issue.getId(),
                issue.getName(),
                issue.getDescription());
        return findById(issue.getId()).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM Issue WHERE id = ?", id);
    }

    @Override
    public List<Issue> findAll() {
        return jdbcTemplate.query("SELECT id, name, description FROM Issue", (rs, row) -> convert(rs));
    }

    @Override
    public Issue update(Issue issue) {
        jdbcTemplate.update("UPDATE Issue g SET name = ?, description = ? where id = ?",
                issue.getName(),
                issue.getDescription(),
                issue.getId());
        return findById(issue.getId()).orElseThrow(RuntimeException::new);
    }

    private Issue convert(ResultSet rs) {
        try {
            UUID id = (UUID) rs.getObject("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            return new Issue(id, name, description);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
