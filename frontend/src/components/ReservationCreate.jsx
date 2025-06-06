import { useState } from "react";
import { useNavigate } from "react-router-dom";
import reservationService from "../services/reservation.service";
import Text from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import MenuItem from "@mui/material/MenuItem";
import { categoryDescription } from "./ReservationList.jsx";
import { Box, Typography } from "@mui/material";

const ReservationCreate = () => {
  const [bookingDate, setBookingDate] = useState("");
  const [category, setCategory] = useState("TIER1");
  const [reserveeClientEmail, setReserveeClientEmail] = useState("");
  const [clients, setClients] = useState([{ name: "", email: "" }]);
  const [warning, setWarning] = useState("");
  const navigate = useNavigate();

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();

    // Validate date and overlap
    const validBookingTime = new Date(bookingDate);
    const openingTime = validBookingTime.getDay() === 0 || validBookingTime.getDay() === 6
      ? 10
      : 14;
    const closingTime = 22;

    if (validBookingTime.getHours() < openingTime || validBookingTime.getHours() > closingTime) {
      setWarning("La reserva debe estar entre las 10:00 y las 22:00.");
      return;
    }

    // Sending data with email instead of ID for reservee client
    reservationService
      .create({
        bookingDate,
        category,
        clients
      })
      .then(() => {
        navigate("/reservation/list");
      })
      .catch((error) => {
        setWarning("Error al crear la reserva.");
      });
  };

  // Handle client input changes (name and email)
  const handleClientChange = (index, field, value) => {
    const newClients = [...clients];
    newClients[index][field] = value;
    setClients(newClients);
  };

  // Add a new client form
  const handleAddClient = () => {
    setClients([...clients, { firstName: "", lastName: "", email: "", birthday: "" }]);
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>Crear Nueva Reserva</h3>
      <Box sx={{ display: "flex", 
        flexDirection: "column", 
        gap: 2, 
        width: "300px",
        margin: "auto" }}>
        <FormControl>
          <TextField
            required
            label="Fecha de Reserva"
            type="datetime-local"
            value={bookingDate}
            onChange={(e) => setBookingDate(e.target.value)}
            focused
            margin="normal"
          />
        </FormControl>
        <FormControl>
          <TextField
            required
            label="Categoría"
            select
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            margin="normal"
          >
            <MenuItem value="TIER1">{categoryDescription("TIER1")}</MenuItem>
            <MenuItem value="TIER2">{categoryDescription("TIER2")}</MenuItem>
            <MenuItem value="TIER3">{categoryDescription("TIER3")}</MenuItem>
          </TextField>
        </FormControl>
        <Typography variant="body" color="textPrimary" marginTop={2} align="center">
          Información Clientes
        </Typography>
        {clients.map((client, index) => (
          <Box key={index} sx={{ display: "flex", flexDirection: "row", gap: 2, width: "200%", alignSelf: "center" }}>
            <TextField
              label="Nombre"
              value={client.firstName}
              onChange={(e) => handleClientChange(index, "firstName", e.target.value)}
              margin="normal"
            />
            <TextField
              label="Apellido"
              value={client.lastName}
              onChange={(e) => handleClientChange(index, "lastName", e.target.value)}
              margin="normal"
            />
            <TextField
              label="Email"
              value={client.email}
              onChange={(e) => handleClientChange(index, "email", e.target.value)}
              margin="normal"
            />
            <TextField
              label="Fecha Nac."
              type="date"
              value={client.birthday}
              onChange={(e) => handleClientChange(index, "birthday", e.target.value)}
              InputLabelProps={{ shrink: true }}
              margin="normal"
            />
          </Box>
        ))}
        <Button 
          variant="outlined" 
          color="secondary" 
          onClick={handleAddClient} 
          style={{ marginBottom: "10px" }}
        >
          Añadir Cliente
        </Button>
        <Button variant="contained" color="primary" type="submit">
          Generar Reserva
        </Button>
        {warning && <div>{warning}</div>}
      </Box>
    </form>
  );
};

export default ReservationCreate;
