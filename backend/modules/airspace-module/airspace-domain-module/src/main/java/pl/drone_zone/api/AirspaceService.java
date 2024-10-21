package pl.drone_zone.api;

import pl.drone_zone.api.models.Airspace;
import pl.drone_zone.api.models.SubCategory;

import java.util.List;

public interface AirspaceService {
    List<Airspace> getAll();
    List<Airspace> getOpenZonesBySubCategory(SubCategory subCategory);
}
