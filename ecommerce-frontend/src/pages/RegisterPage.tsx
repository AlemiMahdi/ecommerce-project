import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../api/authApi";




function RegisterPage(){

    const navigate = useNavigate();

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    async function handleSubmit(event:React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        setError(null);
        setLoading(true);

        try {
            await register({
                firstName, 
                lastName,
                username,
                email,
                password
            });
        //Efter registrering skickar vi användaren till login-sidan
        navigate("/login");
        } catch {
            setError("Registreringen misslyckades.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <main className="auth-page">
            <section className="auth-card">
                <div className="auth-header">
                    <p className="page-eyebrow"> SKAPA KONTO</p>
                    <h1> Registrera dig</h1>
                    <p>Skapa ett konto för att kunna lägga orders och genomföra köp</p>
                </div>
                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="form-row">
                        <div className="form-group">
                            <label className="form-label" htmlFor="firstName">Förnamn</label>
                            <input id="firstName" className="form-input" type="text" value={firstName} 
                            onChange={(event) => setFirstName(event.target.value)} required/>
                        </div>
                        <div className="form-group">
                            <label className="form-label" htmlFor="lastName">Efternamn</label>
                            <input id="lastName" className="form-input" type="text" value={lastName}
                            onChange={(event) => setLastName(event.target.value)} required/>
                        </div>
                    </div>
                        <div className="form-group">
                            <label className="form-label" htmlFor="username">användarnamn</label>
                            <input id="username" className="form-input" type="text" value={username}
                            onChange={(event) => setUsername(event.target.value)} required/>
                        </div>
                        <div className="form-group">
                            <label className="form-label" htmlFor="email">E-post</label>
                            <input id="email" className="form-input" type="email" value={email}
                            onChange={(event) => setEmail(event.target.value)} required/>
                        </div>
                        <div className="form-group">
                            <label className="form-label" htmlFor="password">Lösenord</label>
                            <input id="passowrd" className="form-input" type="passowrd" value={password}
                            onChange={(event) => setPassword(event.target.value)} required/>
                        </div>
                        {error && (<p className="form-error">{error}</p>)}
                        <button type="submit" className="button button-primary auth-submit" disabled={loading}>
                            {loading ? "Skapar konto..." : "Skapa konto"}
                        </button>
                </form>
            </section>
        </main>
    );
}

export default RegisterPage;