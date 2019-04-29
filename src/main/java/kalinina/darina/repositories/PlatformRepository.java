package kalinina.darina.repositories;

import kalinina.darina.entities.City;
import kalinina.darina.entities.Platform;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface PlatformRepository extends CrudRepository<Platform, Long> {
    Collection<Platform> findByCity(City city);
}
