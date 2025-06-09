import React, { useState } from 'react';
import { 
  Container, 
  Paper, 
  Typography, 
  Box, 
  Button, 
  Grid, 
  FormControl,
  FormLabel,
  RadioGroup,
  FormControlLabel,
  Radio,
  Alert,
  CircularProgress,
  TextField
} from '@mui/material';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { LocalizationProvider, DatePicker } from '@mui/x-date-pickers';
import moment from 'moment';
import reportsService from '../services/ms7-reports.service';

const ReportGenerator = () => {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [usingCategory, setUsingCategory] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    // Validate dates
    if (!startDate || !endDate) {
      setError('Por favor seleccione ambas fechas');
      return;
    }

    // Convert dates to YearMonth format
    const formatDate = (date) => {
      return moment(date).format('MM-YYYY');
    };

    try {
      setLoading(true);
      const response = await reportsService.generateExcel(
        formatDate(startDate), 
        formatDate(endDate), 
        usingCategory
      );
      
      // Create file download
      const fileName = response.headers['content-disposition']
        ? response.headers['content-disposition'].split('filename=')[1]
        : `reporte-${usingCategory ? 'categoria' : 'personas'}.xlsx`;
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', fileName);
      document.body.appendChild(link);
      link.click();
      link.remove();
      
    } catch (err) {
      setError('Error al generar el reporte. Por favor intente de nuevo.');
      console.error(err);
    } finally {
      setLoading(false);
    }
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
            Generador de Reportes
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 3, width: '100%', maxWidth: 600 }}>
              {error}
            </Alert>
          )}
          
          <Box 
            component="form" 
            onSubmit={handleSubmit} 
            noValidate 
            sx={{
              width: '100%',
              maxWidth: 600,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center'
            }}
          >
            <LocalizationProvider dateAdapter={AdapterMoment}>
              <Grid container spacing={3} sx={{ mb: 3 }}>
                <Grid item xs={12} md={6}>
                  <DatePicker
                    label="Fecha de inicio"
                    views={['year', 'month']}
                    value={startDate}
                    onChange={(newValue) => setStartDate(newValue)}
                    sx={{ width: '100%' }}
                    renderInput={(params) => <TextField {...params} fullWidth required />}
                    inputFormat="MM/YYYY"
                  />
                </Grid>
                
                <Grid item xs={12} md={6}>
                  <DatePicker
                    label="Fecha de fin"
                    views={['year', 'month']}
                    value={endDate}
                    onChange={(newValue) => setEndDate(newValue)}
                    sx={{ width: '100%' }}
                    renderInput={(params) => <TextField {...params} fullWidth required />}
                    inputFormat="MM/YYYY"
                  />
                </Grid>
              </Grid>
            </LocalizationProvider>
            
            <FormControl component="fieldset" sx={{ mb: 4, width: '100%', textAlign: 'center' }}>
              <FormLabel component="legend" sx={{ mb: 1 }}>Tipo de Reporte</FormLabel>
              <RadioGroup
                row
                value={usingCategory ? "category" : "people"}
                onChange={(e) => setUsingCategory(e.target.value === "category")}
                sx={{ justifyContent: 'center' }}
              >
                <FormControlLabel 
                  value="category" 
                  control={<Radio />} 
                  label="Por Categoría de Reserva" 
                />
                <FormControlLabel 
                  value="people" 
                  control={<Radio />} 
                  label="Por Cantidad de Personas" 
                />
              </RadioGroup>
            </FormControl>
            
            <Button
              variant="contained"
              type="submit"
              disabled={loading}
              startIcon={loading ? <CircularProgress size={20} /> : null}
              sx={{ minWidth: 200 }}
            >
              {loading ? 'Generando...' : 'Generar Reporte Excel'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default ReportGenerator;