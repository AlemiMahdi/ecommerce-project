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
      <main className="page-container">
        <div className="product-details">
            <div className="product-details-image-wrapper">
                {product.imageUrl} ? (
                    <img src={product.imageUrl} alt={product.name} className="product-details-image" />
                ) : ( <div className="product-image-placeholder"> Ingen bild</div> )
            </div>
            <div className="product-details-content">
                <span className="product-category"> {product.category} </span>
                <h1>{product.name}</h1>
                <p className="product-details-description"> {product.description} </p>
                <p className="product-details-price">{product.price.toLocaleString("sv-SE")}kr</p>
                <button type="button" className="button button-primary"> Lägg i kundvagnen</button>
            </div>

        </div>
      </main>
    )
}

export default ProductDetailsPage;