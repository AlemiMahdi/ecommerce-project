import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/authApi";
import { useAuth } from "../context/AuthContext";




function LoginPage(){

    const navigate = useNavigate();
    const { signIn } = useAuth();
    
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState<string | null> (null);
    const [loading, setLoading] = useState(false);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event?.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const response = await login({
                username,
                password
            });

            signIn(response);
            navigate("/products");
        } catch (error) {
            setError("Fel användarnaman eller lösenord");
        } finally {
            setLoading(false);
        }
    }


    return(
        <main className="auth-page">
            <section className="auth-card">
                <div className="auth-header">
                    <p className="page-eyebrow">
                        VÄLKOMMEN TILLBAKA
                    </p>
                    <h1> Logga in</h1>
                    <p>Loggin för att hantera dina orders och genomföra köp.</p>
                </div>
                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="form-label" htmlFor="username">Användarnamn</label>
                        <input id="username" className="form-input" type="text" value={username} 
                        onChange={(event) => setUsername(event?.target.value)} required/>
                    </div>
                    <div className="form-group">
                        <label className="form-label" htmlFor="password">Lösenord</label>
                        <input id="password" className="form-input" type="password" value={password}
                        onChange={(event) => setPassword(event.target.value)} required/>
                    </div>
                    {error && (<p className="form-error">{error}</p>)}
                    <button className="button button-primary auth-submit" type="submit" disabled={loading}>
                        {loading ? "Loggar in..." : "Logga in"}
                    </button>
                </form>
            </section>
        </main>
    );
}

export default LoginPage;