package lizzy.medium.compare.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/issue")
@RequiredArgsConstructor
public class RestInterface {
    private final Repository repository;

    @GetMapping("/")
    public List<Issue> readAll() {
        List<Issue> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @GetMapping("/{id}/")
    public Optional<Issue> read(@PathVariable("id") UUID id) {
        return repository.findById(id);
    }

    @PostMapping("/")
    public Issue create(@RequestBody Issue body) {
        return repository.insert(body);
    }

    @PutMapping("/{id}/")
    public Issue update(@PathVariable("id") UUID id, @RequestBody Issue body) {
        return repository.update(body);
    }

    @PatchMapping("/{id}/")
    public Issue partialUpdate(@PathVariable("id") UUID id, @RequestBody Issue body) {
        final Issue issue = repository.findById(id).orElseThrow(RuntimeException::new);
        issue.partialUpdate(body);
        return repository.update(issue);
    }

    @DeleteMapping("/{id}/")
    public void delete(@PathVariable("id") UUID id) {
        repository.deleteById(id);
    }
}
