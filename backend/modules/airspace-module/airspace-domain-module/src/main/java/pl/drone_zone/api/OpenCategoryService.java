package pl.drone_zone.api;

import pl.drone_zone.api.models.Airspace;
import pl.drone_zone.api.models.SubCategory;

import java.util.List;

public interface OpenCategoryService {
    List<Airspace> getOpenZonesBySubCategory(List<Airspace> airspaceList, SubCategory subCategory);
}
