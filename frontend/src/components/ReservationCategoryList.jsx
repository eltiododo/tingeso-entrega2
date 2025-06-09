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
import reservationCategoryService from '../services/ms1-reservation-categories.service';

const ReservationCategoryList = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchCategories = async () => {
    setLoading(true);
    setError('');
    
    try {
      const response = await reservationCategoryService.getAll();
      
      // Handle the object response format
      if (response.data && typeof response.data === 'object') {
        // Convert object to array
        const categoriesArray = Object.values(response.data).sort((a, b) => a.id - b.id);
        setCategories(categoriesArray);
      } else {
        // If unexpected format, set empty array
        setCategories([]);
        console.error('Unexpected API response format:', response);
        setError('Formato de respuesta inesperado. Contacte al administrador.');
      }
    } catch (err) {
      console.error('Error fetching reservation categories:', err);
      setCategories([]);
      setError('Error al cargar las categorías de reserva. Por favor intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

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
            Categorías de Reserva
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 3, width: '100%', maxWidth: 800 }}>
              {error}
              <Button 
                variant="outlined" 
                size="small" 
                onClick={fetchCategories} 
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
            <TableContainer component={Paper} elevation={0} sx={{ maxWidth: 800 }}>
              <Table aria-label="table of reservation categories">
                <TableHead>
                  <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                    <TableCell><strong>Nombre</strong></TableCell>
                    <TableCell align="center"><strong>Vueltas</strong></TableCell>
                    <TableCell align="center"><strong>Minutos Máx.</strong></TableCell>
                    <TableCell align="center"><strong>Minutos Totales</strong></TableCell>
                    <TableCell align="right"><strong>Costo</strong></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {categories.map((category) => (
                    <TableRow key={category.id}>
                      <TableCell component="th" scope="row">
                        {category.name}
                      </TableCell>
                      <TableCell align="center">{category.laps || '—'}</TableCell>
                      <TableCell align="center">{category.minutesMax || '—'}</TableCell>
                      <TableCell align="center">{category.minutesTotal || '—'}</TableCell>
                      <TableCell align="right">
                        {category.cost ? `$${category.cost.toLocaleString()}` : '—'}
                      </TableCell>
                    </TableRow>
                  ))}
                  {categories.length === 0 && !loading && !error && (
                    <TableRow>
                      <TableCell colSpan={5} align="center">
                        No hay categorías disponibles
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

export default ReservationCategoryList;