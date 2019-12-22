package lizzy.medium.compare.quarkus;

import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/issue")
@RequiredArgsConstructor
public class RestInterface {
    private final Repository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Issue> readAll() {
        return repository.findAll();
    }

    @GET
    @Path("/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Issue read(@PathParam("id") UUID id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Issue create(Issue issue) {
        return repository.insert(issue);
    }

    @PUT
    @Path("/{id}/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Issue update(@PathParam("id") UUID id, Issue issue) {
        return repository.update(issue);
    }

    @PATCH
    @Path("/{id}/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Issue partialUpdate(@PathParam("id") UUID id, Issue issue) {
        final Issue old = repository.findById(id).orElseThrow(RuntimeException::new);
        final Issue updated = old.partialUpdate(issue);
        return repository.update(updated);
    }

    @DELETE
    @Path("/{id}/")
    public void delete(@PathParam("id") UUID id) {
        repository.deleteById(id);
    }
}
