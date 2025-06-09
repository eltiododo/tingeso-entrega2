import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/reservation-category/get');
}

const get = id => {
    return httpClient.get(`/reservation-category/get/${id}`);
}

export default { getAll, get };