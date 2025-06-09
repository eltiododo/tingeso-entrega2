import httpClient from "../http-common";

const create = id => {
    return httpClient.post(`/reservations-receipts/receipt/create/${id}`);
}

const generatePdf = id => {
    return httpClient.get(`/reservations-receipts/receipt/${id}/pdf`);
}

const sendPdf = id => {
    return httpClient.post(`/reservations-receipts/receipt/${id}/send`, null, { responseType: "blob" });
};
export default { create, generatePdf, sendPdf };