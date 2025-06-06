import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/reservation/get');
}

const get = id => {
    return httpClient.get(`/reservation/get/${id}`);
}

const create = data => {
    return httpClient.post("/reservation/create", data);
}

const update = data => {
    return httpClient.post('/reservation/update', data);
}

const remove = id => {
    return httpClient.delete(`/reservation/delete/${id}`);
}
export default { getAll, get, create, update, remove };