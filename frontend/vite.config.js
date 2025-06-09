import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import * as dotenv from 'dotenv';
dotenv.config();

const apiUrl = process.env.VITE_APP_API_URL || 'http://localhost:8080/api'; // Default to localhost if not set

console.log(`API URL from .env: ${apiUrl}`);

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: apiUrl,
        changeOrigin: true,
        secure: false,
      }
    }
  }
});