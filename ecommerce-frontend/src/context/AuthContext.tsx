import { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";
import type { AuthResponse, AuthUser } from "../types/Auth";

interface AuthContextType {
    user: AuthUser | null;
    token: string | null;
    isAuthenticated: boolean;

    signIn: (response: AuthResponse) => void;
    signOut: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);
interface AuthProviderProps {
    children: ReactNode;
}
export function AuthProvider({
    children,
}: AuthProviderProps) {
    const [token, setToken] = useState<string | null>(
        () => localStorage.getItem("authToken")
    );

    const [user, setUser] = useState<AuthUser | null>(() => {
        const userId = localStorage.getItem("userId");
        const username = localStorage.getItem("username");
        const role = localStorage.getItem("role");
        
        if (!userId || !username || !role) { return null;}
        if (role != "ROLE_USER" && role != "ROLE_ADMIN") {return null;}
        
        return {
            userId: Number(userId),
            username,
            role
        };

    });

    const isAuthenticated = token !== null && user !==null;

    function signIn (response: AuthResponse) {
        localStorage.setItem("authToken", response.token);
        localStorage.setItem("userId", response.userId.toString());
        localStorage.setItem("username", response.username);
        localStorage.setItem("role", response.role);
        setToken(response.token);
        setUser({
            userId: response.userId,
            username: response.username,
            role: response.role,
        });
    }

    function signOut() {
        localStorage.removeItem("authToken");
        localStorage.removeItem("userId");
        localStorage.removeItem("username");
        localStorage.removeItem("role");
        setToken(null);
        setUser(null);
    }

    return (
        <AuthContext.Provider 
        value={{
            user,
            token,
            isAuthenticated,
            signIn,
            signOut
        }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);

    if(!context) {
        throw new Error("useAuth must be used inside AuthProvider");
    }

    return context;
}

