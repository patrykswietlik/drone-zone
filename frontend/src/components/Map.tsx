import { Circle, MapContainer, Polygon, Popup, TileLayer } from "react-leaflet";
import React from "react";
import { Airspace } from "../models/airspace";
import { Flight } from "../models/flight";

export interface MapProps {
  airspaces: Airspace[];
  flights: Flight[];
}

export const Map = ({ airspaces, flights }: MapProps) => {
  return (
    <MapContainer center={[52.0, 21.0]} zoom={6} className="size-full z-0">
      <TileLayer
        url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
        attribution='&copy; <a href="https://carto.com/">CARTO</a>'
      />
      {airspaces &&
        airspaces.map((airspace) => (
          <Polygon
            key={airspace.id}
            positions={airspace.geometry.coordinates}
            color={airspace.color}
            className="opacity-30 z-0"
          >
            <Popup>{airspace.name}</Popup>
          </Polygon>
        ))}
      {flights &&
        flights.map((flight, index) => (
          <Circle
            key={index}
            center={flight.coordinate}
            radius={100000}
            color={"blue"}
            className="z-40"
          >
            <Popup>{flight.AGL}</Popup>
          </Circle>
        ))}
    </MapContainer>
  );
};
