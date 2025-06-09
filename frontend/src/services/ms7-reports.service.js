import httpClient from "../http-common";

const generateExcel = (yearMonthStart, yearMonthEnd, usingCategory) => {
    return httpClient.get(`/reports/generate?start=${yearMonthStart}&end=${yearMonthEnd}&usingCategory=${usingCategory}`, {
        responseType: 'blob'
    });
}

export default { generateExcel };