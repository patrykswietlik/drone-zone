interface Coordinate {
  lat: number;
  lng: number;
}

export interface Geometry {
  type: string;
  coordinates: Coordinate[][];
}

export interface HeightLimit {
  value: number;
  unit: number;
}

export enum Category {
  OPEN,
}

export enum SubCategory {
  A1 = "A1",
  A2 = "A2",
  A3 = "A3",
}
export interface Airspace {
  id: string;
  name: string;
  color: string;
  type: number;
  icaoClass: number;
  activity: number;
  onDemand: boolean;
  onRequest: boolean;
  byNotam: boolean;
  specialAgreement: boolean;
  requestCompliance: boolean;
  upperLimit: HeightLimit;
  lowerLimit: HeightLimit;
  geometry: Geometry;
}
