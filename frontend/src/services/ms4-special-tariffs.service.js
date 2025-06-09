import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get("/special-tariff/");
}

const getById = id => {
    return httpClient.get(`/special-tariff/${id}`);
}

const getFestive = day => {
    return httpClient.get(`/special-tariff/festive/${day}`);
}

export default { getAll, getById, getFestive };