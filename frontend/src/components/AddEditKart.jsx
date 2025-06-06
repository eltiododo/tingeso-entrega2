import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import MenuItem from "@mui/material/MenuItem";
import SaveIcon from "@mui/icons-material/Save";
import kartService from "../services/kart.service";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";

const AddEditKart = () => {
  const [kartId, setKartId] = useState("");
  const [model, setModel] = useState("");
  const [state, setKartState] = useState("");
  const { id } = useParams();
  const [titleEmployeeForm, setTitleEmployeeForm] = useState("");
  const navigate = useNavigate();

  const saveKart = (e) => {
    e.preventDefault();

    const kart = { id, model, state };
    if (id) {
      //Actualizar Datos Kart
      kartService
        .update(kart)
        .then((response) => {
          console.log("Kart ha sido actualizado.", response.data);
          navigate("/kart/list");
        })
        .catch((error) => {
          console.log(
            "Ha ocurrido un error al intentar actualizar datos del kart.",
            error
          );
        });
    } else {
      //Crear nuevo kart
      kartService
        .create(kart)
        .then((response) => {
          console.log("Kart ha sido añadido.", response.data);
          navigate("/kart/list");
        })
        .catch((error) => {
          console.log(
            "Ha ocurrido un error al intentar crear nuevo kart.",
            error
          );
        });
    }
  };

  useEffect(() => {
    if (id) {
      setTitleEmployeeForm("Editar Kart");
      kartService
        .get(id)
        .then((kart) => {
          setKartId(id);
          setModel(kart.data.model);
          setKartState(kart.data.state);
        })
        .catch((error) => {
          console.log("Se ha producido un error.", error);
        });
    } else {
      setTitleEmployeeForm("Nuevo Kart");
    }
  }, []);

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      component="form"
    >
      <h3> {titleEmployeeForm} </h3>
      <hr />
      <form>
        <FormControl fullWidth>
          <TextField
            id="name"
            label="Modelo"
            value={model}
            variant="standard"
            onChange={(e) => setModel(e.target.value)}
          />
        </FormControl>

        <FormControl fullWidth variant="standard">
          <InputLabel id="state-label">Estado</InputLabel>
          <Select
            labelId="state-label"
            id="state"
            value={state}
            color="secondary"
            onChange={(e) => setKartState(e.target.value)}
            label="Estado"
          >
            <MenuItem value="AVAILABLE">Disponible</MenuItem>
            <MenuItem value="UNAVAILABLE">No Disponible</MenuItem>
            <MenuItem value="MAINTENANCE">Mantencion</MenuItem>
          </Select>
        </FormControl>

        <FormControl>
          <br />
          <Button
            variant="contained"
            color="secondary"
            onClick={(e) => saveKart(e)}
            style={{ marginLeft: "0.5rem", marginTop: "1rem" }}
            startIcon={<SaveIcon />}
          >
            Grabar
          </Button>
        </FormControl>
      </form>
      <hr />
      <Link to="/kart/list">Volver</Link>
    </Box>
  );
};

export default AddEditKart;
