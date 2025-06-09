import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/reservations-receipts/kart/get');
}

const get = id => {
    return httpClient.get(`/reservations-receipts/kart/get/${id}`);
}

const create = data => {
    return httpClient.post("/reservations-receipts/kart/create", data);
}

const update = data => {
    return httpClient.post('/reservations-receipts/kart/update', data);
}

const remove = id => {
    return httpClient.delete(`/reservations-receipts/kart/delete/${id}`);
}
export default { getAll, get, create, update, remove };