import axios from "axios";

export default axios.create({
    baseURL: '/api', // Adjust the base URL as needed
    headers: {
        'Content-Type': 'application/json'
    }
});