import React, { useEffect, useState } from "react";
import "leaflet/dist/leaflet.css";
import { Airspace, Category, SubCategory } from "../models/airspace";
import { getAirspaces } from "../requests/airspace";
import { Flight } from "../models/flight";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { Map } from "../components/Map";

export const MainPage = () => {
  const [airspaces, setAirspaces] = useState<Airspace[]>([]);
  const [flights, setFlights] = useState<Flight[]>([]);
  const [subCategory, setSubCategory] = useState<SubCategory>(SubCategory.A1);
  const [lat, setLat] = useState<number>(0); // State for latitude
  const [lng, setLng] = useState<number>(0); // State for longitude
  const [stompClient, setStompClient] = useState<Client | null>(null); // State for stompClient

  const handleFetchData = () => {
    getAirspaces(Category.OPEN, subCategory).then((data) => setAirspaces(data));
  };

  useEffect(() => {
    handleFetchData();
  }, [subCategory]);

  useEffect(() => {
    const socket = new SockJS(process.env.REACT_APP_WS_URL!);
    const stompClientInstance = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log(str);
      },
      onConnect: (frame) => {
        console.log("Connected to WebSocket:", frame);
        setStompClient(stompClientInstance);

        stompClientInstance.subscribe("/topic/public", (message) => {
          const fetchedFlights = JSON.parse(message.body);

          if (Array.isArray(fetchedFlights)) {
            setFlights((f) => [...fetchedFlights]);
          } else {
            setFlights((f) => [...f, fetchedFlights]);
          }
        });

        stompClientInstance.publish({
          destination: "/app/flights.active",
          body: "",
        });
      },
      onStompError: (frame) => {
        console.error("Broker reported error: " + frame.headers["message"]);
      },
    });

    stompClientInstance.activate();

    return () => {
      stompClientInstance.deactivate();
    };
  }, []);

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();

    if (stompClient) {
      const flightData = {
        coordinate: {
          lat: lat,
          lng: lng,
        },
        AGL: 125.5,
        startTime: new Date().toISOString(),
        flightDurationInSeconds: 60,
      };
      console.log(flightData);
      stompClient.publish({
        destination: "/app/flights.report",
        body: JSON.stringify(flightData),
      });

      setLat(0);
      setLng(0);
    }
  };

  return (
    <div className="relative w-full h-screen">
      <div className="absolute z-10 top-8 right-8 flex">
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="">Lat</label>
            <input
              type="number"
              value={lat}
              onChange={(e) => setLat(Number(e.target.value))}
            />
          </div>
          <div>
            <label htmlFor="">Lng</label>
            <input
              type="number"
              value={lng}
              onChange={(e) => setLng(Number(e.target.value))}
            />
          </div>
          <button type="submit">Send</button>
        </form>
        <div className="flex flex-col gap-4">
          <button className="bg-black text-white p-4 px-8 rounded-2xl">
            Report Flight
          </button>
          <button
            className="bg-black text-white p-4 px-8 rounded-2xl"
            onClick={() => setSubCategory(SubCategory.A1)}
          >
            A1
          </button>
          <button
            className="bg-black text-white p-4 px-8 rounded-2xl"
            onClick={() => setSubCategory(SubCategory.A2)}
          >
            A2
          </button>
          <button
            className="bg-black text-white p-4 px-8 rounded-2xl"
            onClick={() => setSubCategory(SubCategory.A3)}
          >
            A3
          </button>
        </div>
      </div>
      <Map airspaces={airspaces} flights={flights} />
    </div>
  );
};

export default MainPage;
