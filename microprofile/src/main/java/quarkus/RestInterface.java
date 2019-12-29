package lizzy.medium.compare.quarkus;

import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/issue")
@RequiredArgsConstructor
public class RestInterface {
    private final Repository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Issue> readAll() {
        List<Issue> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @GET
    @Path("/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<Issue> read(@PathParam("id") UUID id) {
        return repository.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Issue create(Issue body) {
        return repository.insert(body);
    }

    @PUT
    @Path("/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Issue update(@PathParam("id") UUID id, Issue body) {
        return repository.update(body);
    }

    @PATCH
    @Path("/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Issue partialUpdate(@PathParam("id") UUID id, Issue body) {
        final Issue issue = repository.findById(id).orElseThrow(RuntimeException::new);
        issue.partialUpdate(body);
        return repository.update(issue);
    }

    @DELETE
    @Path("/{id}/")
    public void delete(@PathParam("id") UUID id) {
        repository.deleteById(id);
    }
}
