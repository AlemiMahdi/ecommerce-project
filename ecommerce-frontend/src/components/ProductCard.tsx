import type { Product } from "../types/Product";
import { Link } from "react-router-dom";

interface ProductCardProps{
    product: Product;
}

function ProductCard({product} : ProductCardProps) {
    return (
        <article className="product-card">
            <h2>{product.name}</h2>
            <p>{product.description}</p>
            <p>{product.price}</p>
            <p>{product.category}</p>
            <Link to={`/products/${product.id}`}> Visa produkt</Link>
        </article>
    );
}

export default ProductCard;