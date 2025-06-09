import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get("/quantity-discount/");
}

const getByClientAmount = amount => {
    return httpClient.get(`/quantity-discount/${amount}`);
}

export default { getAll, getByClientAmount };