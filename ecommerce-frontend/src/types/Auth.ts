export interface LoginRequest {
    username: string;
    password: string;
}

export interface RegisterRequest {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    password: string;
}

export interface AuthResponse {
    userId: number;
    username: string;
    email: string;
    role: "ROLE_USER" | "ROLE_ADMIN";
    token: string;
}

export interface AuthUser {
    userId: number;
    username: string;
    role: "ROLE_USER" | "ROLE_ADMIN";
}