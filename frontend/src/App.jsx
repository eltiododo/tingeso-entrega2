import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import NotFound from './components/NotFound';
import KartList from './components/KartList';
import AddEditKart from './components/AddEditKart';
import ReservationList from './components/ReservationList';
import ReservationCreate from './components/ReservationCreate';
import Rack from './components/Rack';
import ReportGenerator from './components/ReportGenerator';
import ReservationCategoryList from './components/ReservationCategoryList';
import QuantityDiscountList from './components/QuantityDiscountList';
import FrequencyDiscountList from './components/FrequencyDiscountList';
import SpecialTariffList from './components/SpecialTariffList';

function App() {
  return (
      <Router>
          <div className="container">
          <Navbar></Navbar>
            <Routes>
              <Route path="/" element={<Home/>} />
              <Route path="/home" element={<Home/>} />
              <Route path="/kart/list" element={<KartList/>} />
              <Route path="/kart/add" element={<AddEditKart/>} />
              <Route path="/kart/edit/:id" element={<AddEditKart/>} />
              <Route path="/reservation/list" element={<ReservationList/>} />
              <Route path="/reservation/create" element={<ReservationCreate/>} />
              <Route path="/rack" element={<Rack/>} />
              <Route path="/reports" element={<ReportGenerator/>} />
              <Route path="/reservation-categories" element={<ReservationCategoryList/>} />
              <Route path="/quantity-discounts" element={<QuantityDiscountList/>} />
              <Route path="/frequency-discounts" element={<FrequencyDiscountList/>} />
              <Route path="/special-tariffs" element={<SpecialTariffList/>} />
              <Route path="*" element={<NotFound/>} />
            </Routes>
          </div>
      </Router>
  );
}

export default App
