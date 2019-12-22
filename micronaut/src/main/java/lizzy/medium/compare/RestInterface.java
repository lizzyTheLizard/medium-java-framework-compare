package lizzy.medium.compare;

import io.micronaut.http.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Controller("/issue")
@RequiredArgsConstructor
public class RestInterface {
    private final Repository repository;

    @Get("/")
    public List<Issue> readAll() {
        return repository.findAll();
    }

    @Get("/{id}/")
    public Issue read(@PathVariable("id") UUID id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Post("/")
    public Issue create(@Body Issue issue) {
        return repository.insert(issue);
    }

    @Put("/{id}/")
    public Issue update(@PathVariable("id") UUID id, @Body Issue issue) {
        return repository.update(issue);
    }

    @Patch("/{id}/")
    public Issue partialUpdate(@PathVariable("id") UUID id, @Body Issue issue) {
        final Issue old = repository.findById(id).orElseThrow(RuntimeException::new);
        final Issue updated = old.partialUpdate(issue);
        return repository.update(updated);
    }

    @Delete("/{id}/")
    public void delete(@PathVariable("id") UUID id) {
        repository.deleteById(id);
    }
}
