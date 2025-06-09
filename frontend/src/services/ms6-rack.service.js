import httpClient from "../http-common";

const getEvents = () => {
    return httpClient.get("/rack/");
}

export default { getEvents };