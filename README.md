# Drone Zone

This application allows you to verify the air zone in which you intend to fly your drone. You can also report flights so that other users will be attentive when flying UAVs

## Tech Stack

- React, TypeScript, Leafletjs, StompJs

- Spring Boot, MySQL, WebSocket, WebClient

- Docker Compose


## Screenshots




## Environment Variables

To run this project, you will need to add the following environment variables to your .env file and environment variables in backend.

`.env | REACT_APP_WS_URL` - stores the URL of the WebSocket server that the React application will connect to. 

`Environment variables | OPENAIP_TOKEN` - used to store the API key required for authenticating requests to OpenAIP's API.

## Credits

[Leafletjs](https://leafletjs.com/) - Leaflet was originally created by Volodymyr Agafonkin, but is now developed by a big community of contributors.

[OpenAIP](https://www.openaip.net/) - The openaip platform provides current and precise worldwide aeronautical data. Based on contributions from a vibrant community of aviation enthusiasts. Free to use for anyone. Developer tools and API available.

[Carto](https://carto.com/basemaps/) - Carto provides a range of base maps and geospatial services that are designed for data visualization and analysis.