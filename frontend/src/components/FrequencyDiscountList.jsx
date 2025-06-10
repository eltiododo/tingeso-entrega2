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
  Button
} from '@mui/material';
import frequencyDiscountService from '../services/ms3-frequency-discounts.service';

const FrequencyDiscountList = () => {
  const [discounts, setDiscounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchDiscounts = async () => {
    setLoading(true);
    setError('');
    
    try {
      const response = await frequencyDiscountService.getAll();
      
      if (Array.isArray(response.data)) {
        setDiscounts(response.data);
      } else if (response.data && typeof response.data === 'object') {
        // Convert object to array if necessary
        const discountsArray = Object.values(response.data);
        setDiscounts(discountsArray);
      } else {
        setDiscounts([]);
        console.error('Unexpected API response format:', response);
        setError('Formato de respuesta inesperado. Contacte al administrador.');
      }
    } catch (err) {
      console.error('Error fetching frequency discounts:', err);
      setDiscounts([]);
      setError('Error al cargar los descuentos por frecuencia. Por favor intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDiscounts();
  }, []);

  const formatFrequencyRange = (textId) => {
    const numberIndex = textId.search(/\d+/g);
    const range = textId.substring(numberIndex);
    return range.replace('TO', ' - ') + ' visitas mensuales';
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
            Descuentos por Frecuencia
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 3, width: '100%', maxWidth: 600 }}>
              {error}
              <Button 
                variant="outlined" 
                size="small" 
                onClick={fetchDiscounts} 
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
            <TableContainer component={Paper} elevation={0} sx={{ maxWidth: 600 }}>
              <Table aria-label="table of frequency discounts">
                <TableHead>
                  <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                    <TableCell><strong>Rango de Visitas</strong></TableCell>
                    <TableCell align="right"><strong>Porcentaje de Descuento</strong></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {discounts.map((discount) => (
                    <TableRow key={discount.id}>
                      <TableCell component="th" scope="row">
                        {formatFrequencyRange(discount.textId)}
                      </TableCell>
                      <TableCell align="right">
                        {discount.percentage}%
                      </TableCell>
                    </TableRow>
                  ))}
                  {discounts.length === 0 && !loading && !error && (
                    <TableRow>
                      <TableCell colSpan={2} align="center">
                        No hay descuentos disponibles
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

export default FrequencyDiscountList;