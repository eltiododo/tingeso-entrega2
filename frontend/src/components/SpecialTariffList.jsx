import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  CircularProgress,
  Alert,
  Button,
  Chip
} from '@mui/material';
import specialTariffService from '../services/ms4-special-tariffs.service';

const SpecialTariffList = () => {
  const [tariffs, setTariffs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchTariffs = async () => {
    setLoading(true);
    setError('');
    
    try {
      const response = await specialTariffService.getAll();
      
      if (Array.isArray(response.data)) {
        setTariffs(response.data);
      } else if (response.data && typeof response.data === 'object') {
        // Convert object to array if necessary
        const tariffsArray = Object.values(response.data);
        setTariffs(tariffsArray);
      } else {
        setTariffs([]);
        console.error('Unexpected API response format:', response);
        setError('Formato de respuesta inesperado. Contacte al administrador.');
      }
    } catch (err) {
      console.error('Error fetching special tariffs:', err);
      setTariffs([]);
      setError('Error al cargar las tarifas especiales. Por favor intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTariffs();
  }, []);

  // Helper function to format the textId into a more readable form
  const formatTariffType = (textId) => {
    // This is a placeholder - adjust based on actual format
    return textId.replace(/_/g, ' ').toLowerCase();
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Paper sx={{ p: 3 }}>
        <Box 
          display="flex" 
          flexDirection="column" 
          alignItems="center" 
          width="100%"
        >
          <Typography variant="h4" gutterBottom component="div" sx={{ mb: 3 }}>
            Tarifas Especiales
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 3, width: '100%', maxWidth: 700 }}>
              {error}
              <Button 
                variant="outlined" 
                size="small" 
                onClick={fetchTariffs} 
                sx={{ ml: 2 }}
              >
                Reintentar
              </Button>
            </Alert>
          )}
          
          {loading ? (
            <Box display="flex" justifyContent="center" my={4}>
              <CircularProgress />
            </Box>
          ) : (
            <TableContainer component={Paper} elevation={0} sx={{ maxWidth: 700 }}>
              <Table aria-label="table of special tariffs">
                <TableHead>
                  <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                    <TableCell><strong>Tipo</strong></TableCell>
                    <TableCell align="center"><strong>Porcentaje</strong></TableCell>
                    <TableCell align="center"><strong>Categoría</strong></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {tariffs.map((tariff) => (
                    <TableRow key={tariff.id}>
                      <TableCell component="th" scope="row">
                        {formatTariffType(tariff.textId)}
                      </TableCell>
                      <TableCell align="center">
                        {tariff.percentage}%
                      </TableCell>
                      <TableCell align="center">
                        <Chip 
                          label={tariff.isDiscount ? "Descuento" : "Recargo"} 
                          color={tariff.isDiscount ? "success" : "error"}
                          size="small"
                        />
                      </TableCell>
                    </TableRow>
                  ))}
                  {tariffs.length === 0 && !loading && !error && (
                    <TableRow>
                      <TableCell colSpan={3} align="center">
                        No hay tarifas especiales disponibles
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </Box>
      </Paper>
    </Container>
  );
};

export default SpecialTariffList;