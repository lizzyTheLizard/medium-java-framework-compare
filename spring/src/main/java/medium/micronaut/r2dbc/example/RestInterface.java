package medium.micronaut.r2dbc.example;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController("/issue")
@RequiredArgsConstructor
public class RestInterface {
    private final IssueRepository repository;

    @GetMapping("/")
    public Iterable<Issue> readAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}/")
    public Optional<Issue> read(@PathVariable("id") UUID id) {
        return repository.findById(id);
    }

    @PostMapping
    public Issue create(@RequestBody Issue issue) {
        return repository.save(issue);
    }

    @PutMapping("/{id}")
    public Issue update(@PathVariable("id") UUID id, @RequestBody Issue issue) {
        return repository.save(issue);
    }

    @PatchMapping("/{id}")
    public Issue partialUpdate(@PathVariable("id") UUID id, @RequestBody Issue issue) {
        final var old = repository.findById(id).orElseThrow();
        final var updated = old.partialUpdate(issue);
        return repository.save(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        repository.deleteById(id);
    }
}
