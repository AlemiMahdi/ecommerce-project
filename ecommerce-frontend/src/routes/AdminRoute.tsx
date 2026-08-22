import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function AdminRoute(){
    const {isAuthenticated, user} = useAuth();
    
    if (!isAuthenticated) {
        return <Navigate to="/login" replace></Navigate>
    }

    if (user?.role !== "ROLE_ADMIN") {
        return <Navigate to="/" replace></Navigate>
    }

    return <Outlet></Outlet>
}

export default AdminRoute;