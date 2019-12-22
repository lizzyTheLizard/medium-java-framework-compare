package lizzy.medium.compare;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
class Issue {
    @Id
    private final UUID id;
    private final String name;
    private final String description;

    Issue partialUpdate(Issue partialIssue) {
        return new Issue(this.id,
                partialIssue.name != null ? partialIssue.name : this.name,
                partialIssue.description != null ? partialIssue.description : this.description);
    }
}
