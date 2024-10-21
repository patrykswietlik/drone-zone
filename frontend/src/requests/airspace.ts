import { AirspaceResponse } from "../models/response";
import { Airspace, Category, SubCategory } from "../models/airspace";

export const getAirspaces = async (
  category: Category,
  subCategory: SubCategory,
): Promise<Airspace[]> => {
  const response = await fetch(
    `http://localhost:8080/airspaces/open?subCategory=${subCategory}`,
  );

  if (!response.ok) {
    throw new Error("Could not fetch data");
  }

  const data = (await response.json()) as AirspaceResponse;
  return data.airspaceList;
};
