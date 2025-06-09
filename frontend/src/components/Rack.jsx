import React, { useState, useEffect } from 'react';
import moment from 'moment';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import RackService from '../services/ms6-rack.service';
import reservationService from "../services/reservation.service";
import { 
  Container, Paper, Typography, Box, CircularProgress,
  Dialog, DialogTitle, DialogContent, DialogActions, Button, Divider, Alert
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

import 'moment/dist/locale/es'; // Import Spanish locale
// Setup the localizer for react-big-calendar
moment.locale("es"); // Set locale to Spanish
const localizer = momentLocalizer(moment);

const Rack = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [view, setView] = useState('week');
  const [currentDate, setCurrentDate] = useState(new Date());

  const [selectedEvent, setSelectedEvent] = useState(null);
  const [openEventModal, setOpenEventModal] = useState(false);
  const [openConfirmDialog, setOpenConfirmDialog] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);

  const navigate = useNavigate();
  
  const fetchEvents = async () => {
    try {
      const response = await RackService.getEvents();
      
      const formattedEvents = response.data.map(event => ({
        id: event.id,
        title: event.reserveeClientName || 'Reserva',
        start: new Date(event.bookingDate),
        end: new Date(event.bookingDateEnd),
        resource: event.category
      }));

      console.log("Fetched rack events:", formattedEvents);
      
      setEvents(formattedEvents);
      setLoading(false);
    } catch (err) {
      console.error("Error fetching rack events:", err);
      setError("Error al cargar rack. Por favor intentar más tarde.");
      setLoading(false);
    }
  };

  useEffect(() => {

    fetchEvents();
  }, []);

  const eventStyleGetter = (event) => {
    return {
      style: {
        backgroundColor: '#3174ad',
        borderRadius: '5px',
        color: 'white',
        border: 'none',
        display: 'block'
      }
    };
  };

  eventWrapper: ({ event, children }) => {
    return (
      <div className="custom-event-wrapper">
        {/* Replace the default event rendering with our custom version */}
        <div className="rbc-event-content" style={{ height: '100%', padding: '2px' }}>
          <strong>{event.title}</strong>
          <div>{event.resource}</div>
        </div>
      </div>
    );
  }

  const EventComponent = ({ event }) => (
   <div>
    <strong>{event.title}</strong>
    <div>{event.start.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", hour12: false })} - {event.end.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", hour12: false })}</div>
   </div>
  );

  // Handle view changes
  const handleViewChange = (newView) => setView(newView);

  // Add these navigation handlers
  const handleNavigate = (newDate) => {
    setCurrentDate(newDate);
  };

  const handleSelectSlot = ({ start }) => {
    // If the selected slot is in the past, do not allow selection
    if (moment(start).isBefore(moment(), 'minute')) {
      return;
    }

    // Format the date in the format expected by the datetime-local input
    // YYYY-MM-DDThh:mm format for datetime-local inputs
    const formattedDate = moment(start).format('YYYY-MM-DDTHH:mm');
    
    // Navigate to reservation create page with the selected date
    navigate(`/reservation/create?bookingDate=${formattedDate}`);
  };

  // Event selection handler
  const handleSelectEvent = (event) => {
    setSelectedEvent(event);
    setOpenEventModal(true);
  };
  
  // Modal close handler
  const handleCloseModal = () => {
    setOpenEventModal(false);
  };
  
  // Delete button click handler
  const handleDeleteClick = () => {
    setOpenConfirmDialog(true);
  };
  
  // Cancel delete handler
  const handleCancelDelete = () => {
    setOpenConfirmDialog(false);
  };
  
  // Confirm delete handler
  const handleConfirmDelete = async () => {
    if (!selectedEvent || !selectedEvent.id) return;
    
    setDeleteLoading(true);
    try {
      await reservationService.remove(selectedEvent.id);
      // Close dialogs
      setOpenConfirmDialog(false);
      setOpenEventModal(false);
      // Refresh the events list
      fetchEvents();
    } catch (error) {
      console.error("Error deleting reservation:", error);
      setError("Failed to delete reservation. Please try again.");
    } finally {
      setDeleteLoading(false);
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
        <Alert 
          severity="error" 
          sx={{ 
            mb: 3, 
            width: '100%', 
            maxWidth: 600 
          }}
        >
          {error}
        </Alert>
      </Box>
    );
  }

  // horarios para mostrar en el calendario
  const minTime = new Date();
  minTime.setHours(10, 0, 0);

  const maxTime = new Date();
  maxTime.setHours(22, 0, 0);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Paper sx={{ p: 3, display: 'flex', flexDirection: 'column' }}>
        <Typography variant="h4" gutterBottom component="div" sx={{ mb: 3 }}>
          Rack semanal de ocupación de la pista
        </Typography>

        <Typography variant="h6" gutterBottom component="div" sx={{ mb: 2, color: 'text.secondary' }}>
          {moment(currentDate).format('YYYY')}
        </Typography>

        <div style={{ height: 700 }}>
            <style>
            {`
              /* Make cursor pointer for empty slots in day/week view */
              .rbc-day-slot .rbc-time-slot {
                cursor: pointer;
              }
              
              /* Make cursor pointer for empty slots in month view */
              .rbc-month-view .rbc-day-bg {
                cursor: pointer;
              }
              
              /* Keep the other existing styles */
              .rbc-time-view .rbc-event {
                min-height: 40px !important;
                padding: 2px 5px !important;
              }
              
              .rbc-event-label {
                display: none !important;
              }
              
              .rbc-event-content {
                width: 100% !important;
                height: 100% !important;
              }
            `}
          </style>
          <Calendar
            localizer={localizer}
            culture="es"
            events={events}
            startAccessor="start"
            endAccessor="end"
            style={{ height: '100%' }}
            components={{
                event: EventComponent
            }}
            eventPropGetter={eventStyleGetter}
            views={['month', 'week', 'day', 'agenda']}
            view={view}
            onView={handleViewChange}
            date={currentDate}
            onNavigate={handleNavigate}
            defaultView="week"
            defaultDate={new Date()}
            tooltipAccessor={event => `${event.title} - Tipo: ${event.resource}`}
            min={minTime}
            max={maxTime}
            messages={{
                month: 'Mes',
                week: 'Semana',
                day: 'Día',
                agenda: 'Agenda',
                date: 'Fecha',
                time: 'Hora',
                event: 'Evento',
                today: 'Hoy',
                next: 'Siguiente',
                previous: 'Anterior',
                showMore: total => `+ Ver ${total} más`,
                noEventsInRange: 'No hay eventos en este rango',
                allDay: 'Todo el día'
              }}
            selectable={true}
            onSelectSlot={handleSelectSlot}
            onSelectEvent={handleSelectEvent}
          />
        </div>
        {/* Event Details Modal */}
        <Dialog open={openEventModal} onClose={handleCloseModal} maxWidth="sm" fullWidth>
          <DialogTitle>Detalles de la Reserva</DialogTitle>
          <DialogContent>
            {selectedEvent && (
              <Box sx={{ mt: 2 }}>
                <Typography variant="body1">
                  <strong>Reservante:</strong> {selectedEvent.title}
                </Typography>
                <Typography variant="body1">
                  <strong>Tipo:</strong> {selectedEvent.resource}
                </Typography>
                <Typography variant="body1">
                  <strong>Fecha:</strong> {moment(selectedEvent.start).format('dddd, D [de] MMMM [de] YYYY')}
                </Typography>
                <Typography variant="body1">
                  <strong>Hora:</strong> {moment(selectedEvent.start).format('HH:mm')} - {moment(selectedEvent.end).format('HH:mm')}
                </Typography>
              </Box>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseModal}>Cerrar</Button>
            <Button 
              onClick={handleDeleteClick}
              color="error"
              disabled={selectedEvent && moment(selectedEvent.start).isBefore(moment())}
            >
              Eliminar
            </Button>
          </DialogActions>
        </Dialog>
        
        {/* Confirmation Dialog */}
        <Dialog open={openConfirmDialog} onClose={handleCancelDelete}>
          <DialogTitle>Confirmar Eliminación</DialogTitle>
          <DialogContent>
            <Typography variant="body1">
              ¿Está seguro que desea eliminar esta reserva? Esta acción no se puede deshacer.
            </Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCancelDelete}>Cancelar</Button>
            <Button 
              onClick={handleConfirmDelete} 
              color="error" 
              disabled={deleteLoading || (selectedEvent && moment(selectedEvent.start).isBefore(moment()))}
            >
              {deleteLoading ? <CircularProgress size={24} /> : "Eliminar"}
            </Button>
          </DialogActions>
        </Dialog>
      </Paper>
    </Container>
  );
};

export default Rack;