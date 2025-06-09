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
import quantityDiscountService from '../services/ms2-quantity-discounts.service';

const QuantityDiscountList = () => {
  const [discounts, setDiscounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchDiscounts = async () => {
    setLoading(true);
    setError('');
    
    try {
      const response = await quantityDiscountService.getAll();
      
      const discountsArray = Object.values(response.data).filter(discount =>
        discount.textId !== 'NONE');
      setDiscounts(discountsArray);
      
    } catch (err) {
      console.error('Error fetching quantity discounts:', err);
      setDiscounts([]);
      setError('Error al cargar los descuentos por cantidad. Por favor intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDiscounts();
  }, []);

  // Helper function to format the textId into a more readable form
  const formatDiscountRange = (textId) => {
    const numberIndex = textId.search(/\d+/g);
    const range = textId.substring(numberIndex);
    return range.replace('TO', ' - ') + ' personas';
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
            Descuentos por Cantidad
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
              <Table aria-label="table of quantity discounts">
                <TableHead>
                  <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                    <TableCell><strong>Rango de Personas</strong></TableCell>
                    <TableCell align="right"><strong>Porcentaje de Descuento</strong></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {discounts.map((discount) => (
                    <TableRow key={discount.id}>
                      <TableCell component="th" scope="row">
                        {formatDiscountRange(discount.textId)}
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

export default QuantityDiscountList;