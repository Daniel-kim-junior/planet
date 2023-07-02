package rocket.planet.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ProfileTechId implements Serializable {
    private UUID profile;
    private UUID tech;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        final ProfileTechId that = (ProfileTechId) o;
        return profile.equals(that.tech) && tech.equals(that.tech);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, tech);
    }
}
