import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import { getProductById } from "../api/productApi";
import type { Product } from "../types/Product";

function ProductDetailsPage() {
    const {id} = useParams();

    const [product, setProduct] = useState<Product | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect (() => {
        async function loadProduct() {
            if (!id) {
                setError("Produkt - ID sakans");
                setLoading(false);
                return;
            }

            try {
                const productId = Number(id);
                const data = await getProductById(productId);
                setProduct(data);

            } catch {
                setError("kunde inte hämtar produkten")
            } finally {
                setLoading(false);
            }  
        }
        loadProduct();
    }, [id]);

    if (loading){
        return (
            <main>
                <p>Laddar produkt ...</p>
            </main>
        )
    }

    if (error || !product) {
        return (
            <main>
                <p> {error ?? "Produkten hittades inte."}</p>
            </main>
        );
    }

    return (
        <main>
            <h1>{product.name}</h1>
            <p>{product.description}</p>
            <h2>{product.price}</h2>
            <p>Kategori: {product.category}</p>
        </main>
    )
}

export default ProductDetailsPage;