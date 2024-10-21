import { LatLng } from "leaflet";

export interface Flight {
  coordinate: LatLng;
  AGL: number;
  startTime: Date;
  flightDurationInSeconds: number;
  isActive: boolean;
}
