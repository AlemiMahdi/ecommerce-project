import { Link } from "react-router-dom";

function Navbar(){
    return(
        <header>
            <nav>
                <Link to="/"> E-commerce</Link>
                
                <div>
                    <Link to={"/products"}>Produkter</Link>
                    <Link to={"/login"}>Logga in</Link>
                    <Link to={"/register"}>Registrera</Link>
                </div>
            </nav>
        </header>
    );
}

export default Navbar;