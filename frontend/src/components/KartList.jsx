import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import kartService from "../services/kart.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { DirectionsCarFilled } from "@mui/icons-material";

const KartList = () => {
  const [karts, setKarts] = useState([]);

  const navigate = useNavigate();

  const init = () => {
    kartService
      .getAll()
      .then((response) => {
        setKarts(response.data);
      })
      .catch((error) => {
        console.log(
          "Se ha producido un error al intentar mostrar listado de todos los karts.",
          error
        );
      });
  };

  useEffect(() => {
    init();
  }, []);

  const handleDelete = (id) => {
    console.log("Printing id", id);
    const confirmDelete = window.confirm(
      "¿Esta seguro que desea borrar este kart?"
    );
    if (confirmDelete) {
      kartService
        .remove(id)
        .then((response) => {
          init();
        })
        .catch((error) => {
          console.log(
            "Se ha producido un error al intentar eliminar al kart",
            error
          );
        });
    }
  };

  const handleEdit = (id) => {
    console.log("Printing id", id);
    navigate(`/kart/edit/${id}`);
  };

  return (
    <TableContainer component={Paper}>
      <br />
      <Link
        to="/kart/add"
        style={{ textDecoration: "none", marginBottom: "1rem" }}
      >
        <Button
          variant="contained"
          color="secondary"
          startIcon={<DirectionsCarFilled />}
        >
          Añadir Kart
        </Button>
      </Link>
      <br /> <br />
      <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Código
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Modelo
            </TableCell>
            <TableCell align="right" sx={{ fontWeight: "bold" }}>
              Estado
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {karts.map((kart) => (
            <TableRow
              key={kart.id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell align="left">{`K${kart.id}`}</TableCell>
              <TableCell align="left">{kart.model}</TableCell>
              <TableCell align="right">{kart.state}</TableCell>
              <TableCell>
                <Button
                  variant="contained"
                  color="secondary"
                  size="small"
                  onClick={() => handleEdit(kart.id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<EditIcon />}
                >
                  Editar
                </Button>

                <Button
                  variant="contained"
                  color="error"
                  size="small"
                  onClick={() => handleDelete(kart.id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<DeleteIcon />}
                >
                  Eliminar
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default KartList;
