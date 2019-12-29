package lizzy.medium.compare.micronaut;

import io.micronaut.http.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller("/issue")
@RequiredArgsConstructor
public class RestInterface {
    private final Repository repository;

    @Get
    public List<Issue> readAll() {
        List<Issue> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Get("/{id}/")
    public Optional<Issue> read(@PathVariable("id") UUID id) {
        return repository.findById(id);
    }

    @Post
    public Issue create(@Body Issue body) {
        return repository.save(body);
    }

    @Put("/{id}/")
    public Issue update(@PathVariable("id") UUID id, @Body Issue body) {
        return repository.update(body);
    }

    @Patch("/{id}/")
    public Issue partialUpdate(@PathVariable("id") UUID id, @Body Issue body) {
        final Issue issue = repository.findById(id).orElseThrow(RuntimeException::new);
        issue.partialUpdate(body);
        return repository.update(issue);
    }

    @Delete("/{id}/")
    public void delete(@PathVariable("id") UUID id) {
        repository.deleteById(id);
    }
}
