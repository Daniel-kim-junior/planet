package rocket.planet.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UserProjectId implements Serializable {
    private UUID user;
    private UUID project;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final UserProjectId that = (UserProjectId) o;
        return user.equals(that.user) && project.equals(that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, project);
    }
}
