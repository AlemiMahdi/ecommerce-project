import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";



function Navbar(){

    const {
        user,
        isAuthenticated,
        signOut,
    } = useAuth();

    const navigate = useNavigate();

    function handleLogout() {
        signOut();
        navigate("/");
    }
    return(
        <header>
            <nav>
                <Link to="/"> E-commerce</Link>
                
                <div>
                    <Link to={"/products"}>Produkter</Link>

                    {isAuthenticated ? (
                        <>
                            <Link to="/orders"> Mina orders</Link>
                            {user?.role === "ROLE_ADMIN" && (
                                <Link to="/admin/products"> Admin </Link>
                            )}
                            <span className="nav-user">
                                {user?.username}
                            </span>
                            <button type="button" className="nav-logout" onClick={handleLogout}>
                                Logga ut
                            </button>
                            
                        </>
                    ) : (
                      <>
                        <Link to="/login"> Logga in</Link>
                        <Link to="/register">Registrera</Link>
                      </>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default Navbar;