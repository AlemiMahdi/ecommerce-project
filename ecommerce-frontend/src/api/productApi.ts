import { apiClient } from "./apiClient";
import type { Product } from "../types/Product";

export function getProducts(): Promise<Product[]> {
  return apiClient<Product[]>("/api/v1/products");
}

export function getProductById(id: number): Promise<Product> {
  return apiClient<Product>(`/api/v1/products/${id}`)
}