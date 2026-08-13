import type { Product } from "../types/Product";
import { Link } from "react-router-dom";

interface ProductCardProps{
    product: Product;
}

function ProductCard({product} : ProductCardProps) {
    return (
        <article className="product-card">
            <div className="product-card-image-wrapper">
                {product.imageUrl} ? (
                    <img src={product.imageUrl} alt={product.name} className="product-card-image" />
                ) : ( <div className="product-image-placeholder"> Ingen bild</div> )
            </div>
            <div className="product-card-content">
                <span className="product-category"> {product.category}</span>
                <h2>{product.name}</h2>
                <p className="product-description"> {product.description}</p>
            
            <div className="product-card-footer">
                <strong className="product-price"> {product.price.toLocaleString("sv-SE")} kr</strong>
                <Link to={`/products/${product.id}`} className="button button-primary">Visa produkt</Link>
            </div>
            </div>
        </article>
    );
}

export default ProductCard;