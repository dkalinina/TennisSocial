package kalinina.darina.repositories;

import kalinina.darina.entities.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, String> {
    default City findCloseTo(double lat, double lng) {
        Iterable<City> cities = this.findAll();
        double minDist2 = Double.MAX_VALUE;
        double distance2;
        City close = null;

        for (City city: this.findAll()) {
            // TODO
            // не географическое расстояние
            distance2 = Math.pow(lat - city.getLatitude(), 2) + Math.pow(lng - city.getLongitude(), 2);
            if (distance2 <= minDist2) {
                minDist2 = distance2;
                close = city;
            }
        }
        return close;
    }
}
