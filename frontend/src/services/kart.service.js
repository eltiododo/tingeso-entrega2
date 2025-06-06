import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/kart/get');
}

const get = id => {
    return httpClient.get(`/kart/get/${id}`);
}

const create = data => {
    return httpClient.post("/kart/create", data);
}

const update = data => {
    return httpClient.post('/kart/update', data);
}

const remove = id => {
    return httpClient.delete(`/kart/delete/${id}`);
}
export default { getAll, get, create, update, remove };