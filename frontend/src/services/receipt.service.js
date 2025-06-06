import httpClient from "../http-common";

const create = id => {
    return httpClient.post(`/receipt/create/${id}`);
}

const generatePdf = id => {
    return httpClient.get(`/receipt/${id}/pdf`);
}

const sendPdf = id => {
    return httpClient.post(`/receipt/${id}/send`, null, { responseType: "blob" });
};
export default { create, generatePdf, sendPdf };