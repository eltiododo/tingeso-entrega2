import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/reservations-receipts/reservation/get');
}

const get = id => {
    return httpClient.get(`/reservations-receipts/reservation/get/${id}`);
}

const create = data => {
    return httpClient.post("/reservations-receipts/reservation/create", data);
}

const update = data => {
    return httpClient.post('/reservations-receipts/reservation/update', data);
}

const remove = id => {
    return httpClient.delete(`/reservations-receipts/reservation/delete?id=${id}`);
}
export default { getAll, get, create, update, remove };