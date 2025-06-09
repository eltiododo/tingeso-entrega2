import * as React from "react";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import PeopleAltIcon from "@mui/icons-material/PeopleAlt";
import PaidIcon from "@mui/icons-material/Paid";
import CalculateIcon from "@mui/icons-material/Calculate";
import AnalyticsIcon from "@mui/icons-material/Analytics";
import DiscountIcon from "@mui/icons-material/Discount";
import HailIcon from "@mui/icons-material/Hail";
import MedicationLiquidIcon from "@mui/icons-material/MedicationLiquid";
import MoreTimeIcon from "@mui/icons-material/MoreTime";
import HomeIcon from "@mui/icons-material/Home";
import CalendarMonth from "@mui/icons-material/CalendarMonth";
import { LocalAtm } from "@mui/icons-material";
import { DiscountOutlined, Discount } from "@mui/icons-material";
import { Cake } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import { DirectionsCarFilled, ViewDay } from "@mui/icons-material";

export default function Sidemenu({ open, toggleDrawer }) {
  const navigate = useNavigate();

  const listOptions = () => (
    <Box
      role="presentation"
      onClick={toggleDrawer(false)}
    >
      <List>
        <ListItemButton onClick={() => navigate("/home")}>
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Home" />
        </ListItemButton>

        <Divider />

        <ListItemButton onClick={() => navigate("/kart/list")}>
          <ListItemIcon>
            <DirectionsCarFilled />
          </ListItemIcon>
          <ListItemText primary="Lista de Karts" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/reservation-categories")}>
          <ListItemIcon>
            <LocalAtm />
          </ListItemIcon>
          <ListItemText primary="Categorías de Reserva" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/quantity-discounts")}>
          <ListItemIcon>
            <Discount />
          </ListItemIcon>
          <ListItemText primary="Descuentos por Cantidad" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/frequency-discounts")}>
          <ListItemIcon>
            <DiscountOutlined />
          </ListItemIcon>
          <ListItemText primary="Descuentos por Cliente Frecuente" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/special-tariffs")}>
          <ListItemIcon>
            <Cake />
          </ListItemIcon>
          <ListItemText primary="Tarifas Dias Especiales" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/reservation/list")}>
          <ListItemIcon>
            <ViewDay />
          </ListItemIcon>
          <ListItemText primary="Lista de Reservas" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/rack")}>
          <ListItemIcon>
            <CalendarMonth />
          </ListItemIcon>
          <ListItemText primary="Rack Semanal" />
        </ListItemButton>

        <ListItemButton onClick={() => navigate("/reports")}>
          <ListItemIcon>
            <CalculateIcon />
          </ListItemIcon>
          <ListItemText primary="Reportes" />
        </ListItemButton>
      </List>

      <Divider />
    </Box>
  );

  return (
    <div>
      <Drawer anchor={"left"} open={open} onClose={toggleDrawer(false)}>
        {listOptions()}
      </Drawer>
    </div>
  );
}
