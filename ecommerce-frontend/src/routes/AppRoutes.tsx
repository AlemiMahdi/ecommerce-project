import { Route, Routes } from "react-router-dom";
import HomePage from "../pages/HomePage";
import ProductsPages from "../pages/ProductsPage";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import ProductDetailsPage from "../pages/PRoductDetailsPage";
import ProtectedRoute from "./ProtectedRoutes";
import AdminRoute from "./AdminRoute";
import OrdersPage from "../pages/OrdersPage";
import AdminProductsPage from "../pages/admin/AdminProductsPage";



function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/products" element={<ProductsPages />} />
      <Route path="/products/:id" element={<ProductDetailsPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      
      //krävs login
      <Route element={<ProtectedRoute />}>
        <Route path="/orders" element={<OrdersPage />}/>
      </Route>

      //Kräver ROLE_ADMIN
      <Route element={<AdminRoute />}>
        <Route path="/admin/products" element={<AdminProductsPage />}/>
      </Route>
    </Routes>
  );
}

export default AppRoutes;