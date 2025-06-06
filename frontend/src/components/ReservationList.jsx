import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import reservationService from "../services/reservation.service";
import receiptService from "../services/receipt.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Typography from "@mui/material/Typography";

export const categoryDescription = (category) => {
    const desc = " Vueltas / Minutos En Pista";
    switch (category) {
      case "TIER1":
        return "10" + desc;
      case "TIER2":
        return "15" + desc;
      case "TIER3":
        return "20" + desc;
      default:
        return category;
    }
};

const ReservationList = () => {
  const [reservations, setReservations] = useState([]);
  const [selectedReservation, setSelectedReservation] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const navigate = useNavigate();

  const init = () => {
    reservationService
      .getAll()
      .then((response) => {
        setReservations(response.data);
        console.log("Mostrando listado de todas las reservas.", response.data);
      })
      .catch((error) => {
        console.log("Error loading reservations", error);
      });
  };

  const handleViewDetails = (reservation) => {
    setSelectedReservation(reservation);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
  };

  const handleSendPdf = (reservationId) => {
    receiptService
      .sendPdf(reservationId)
      .then((response) => {
        const blob = new Blob([response.data], { type: "application/pdf" });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `receipt_${reservationId}.pdf`);
        document.body.appendChild(link);
        link.click();
        link.parentNode.removeChild(link);
        console.log("PDF enviado y descargado correctamente.");
      })
      .catch((error) => {
        console.error("Error al enviar el PDF:", error);
      });
  };

  useEffect(() => {
    init();
  }, []);

  const handleCreateReservation = () => {
    navigate("/reservation/create");
  };

  return (
    <div>
      <Button
        variant="contained"
        color="secondary"
        onClick={handleCreateReservation}
        style={{ marginTop: "20px" }}
      >
        Crear Nueva Reserva
      </Button>
    <TableContainer component={Paper}>
      <h3>Lista de Reservas</h3>
      <Table sx={{ minWidth: 650 }} size="small">
        <TableHead>
          <TableRow>
            <TableCell>Fecha de Reserva</TableCell>
            <TableCell>Categoría</TableCell>
            <TableCell>Cliente Reservante</TableCell>
            <TableCell>Participantes</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reservations.map((reservation) => (
            <TableRow key={reservation.id}>
              <TableCell>{new Date(reservation.bookingDate).toLocaleString()}</TableCell>
              <TableCell>{categoryDescription(reservation.category)}</TableCell>
              <TableCell>{reservation.clients[0].firstName + " " + reservation.clients[0].lastName}</TableCell>
              <TableCell>{reservation.clients.length}</TableCell>
              <TableCell>
                <Button 
                    variant="contained"
                    color="secondary" 
                    onClick={() => handleViewDetails(reservation)}>
                  Ver Más
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => handleSendPdf(reservation.id)}
                    style={{ marginLeft: "10px" }}
                  >
                    Enviar PDF
                  </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>

    {/* Dialog for showing client details */}
    <Dialog open={dialogOpen} onClose={handleCloseDialog}>
        <DialogTitle>Detalles de la Reserva</DialogTitle>
        <DialogContent>
          {selectedReservation && (
            <div>
              <Typography variant="h6">Participantes:</Typography>
              {selectedReservation.clients.map((client, index) => (
                <Typography key={index}>
                  {client.firstName} {client.lastName} - {client.email}
                </Typography>
              ))}
            </div>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary">
            Cerrar
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default ReservationList;
