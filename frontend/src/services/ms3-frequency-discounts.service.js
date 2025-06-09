import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get("/frequency-discount/");
}

const getByVisits = visits => {
    return httpClient.get(`/frequency-discount/${visits}`);
}

export default { getAll, get: getByVisits };