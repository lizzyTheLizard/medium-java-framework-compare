package lizzy.medium.compare.helidon;

import io.helidon.common.CollectionsHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("/")
public class Application extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        return CollectionsHelper.setOf(RestInterface.class);
    }
}
