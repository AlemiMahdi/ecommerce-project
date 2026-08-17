import { apiClient } from "./apiClient";
import type { AuthResponse, LoginRequest, RegisterRequest } from "../types/Auth";

export function login( request: LoginRequest) : Promise<AuthResponse> {
    return apiClient<AuthResponse>("/api/v1/auth/login",{
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    } );
}

export function register ( request: RegisterRequest) : Promise<AuthResponse> {
    return apiClient<AuthResponse>(
        "/api/v1/auth/register",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request),
        }
    );
}