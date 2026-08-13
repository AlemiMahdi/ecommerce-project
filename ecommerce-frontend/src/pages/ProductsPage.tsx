import { useEffect, useState } from "react";

import { getProducts } from "../api/productApi";
import type { Product } from "../types/Product";
import ProductCard from "../components/ProductCard";


function ProductsPages(){

    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);


    useEffect(() => {
        async function loadProducts() {
            try {
                const data = await getProducts();
                setProducts(data);

            } catch (error) {
                setError("Kunde inte hämtar produkter");
            } finally {
                setLoading(false);
            }
        }
        
        loadProducts();
    }, []);

    if (loading) {
        return (
            <main>
                <p>Laddar produkter ...</p>
            </main>
        );
    }

    if (error) {
        return (
            <main>
                <p>{error}</p>
            </main>
        );
    }
    return(
        <main className="page-container">
            <section className="page-header">
                <p className="page-eyebrow"> BUTIK</p>
                <h1>Våra produkter</h1>
                <p>Utforska vårt aktuella sortiment</p>
            </section>
            <div className="product-grid">
                {products.map((product) => (
                    <ProductCard key={product.id} product={product}/>
                ))}
            </div>
        </main>
    );
}

export default ProductsPages;