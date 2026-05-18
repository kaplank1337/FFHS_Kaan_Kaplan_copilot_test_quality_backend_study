import { useState } from "react";
import { Product } from "@/types/shop";
import { useCart } from "@/context/CartContext";
import { ShoppingCart } from "lucide-react";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";

interface ProductCardProps {
  product: Product;
}

const ProductCard = ({ product }: ProductCardProps) => {
  const { addProduct } = useCart();
  const [loading, setLoading] = useState(false);

  const handleAdd = async () => {
    setLoading(true);
    try {
      await addProduct(product);
      toast.success(`${product.name} wurde zum Warenkorb hinzugefügt`);
    } catch {
      toast.error("Fehler beim Hinzufügen zum Warenkorb");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="group flex flex-col overflow-hidden rounded-lg border border-border bg-card shadow-sm transition-shadow hover:shadow-md">
      <div className="aspect-square overflow-hidden bg-muted">
        <img
          src={product.bildUrl}
          alt={product.name}
          className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
          loading="lazy"
        />
      </div>
      <div className="flex flex-1 flex-col gap-2 p-4">
        <div className="flex items-start justify-between gap-2">
          <h3 className="text-base font-semibold text-foreground leading-tight">{product.name}</h3>
          <span className="shrink-0 rounded-md bg-accent px-2 py-0.5 text-sm font-bold text-accent-foreground">
            {product.preis.toFixed(2)} €
          </span>
        </div>
        <p className="flex-1 text-sm text-muted-foreground leading-relaxed">{product.beschreibung}</p>
        <p className="text-xs text-muted-foreground">ID: {product.id}</p>
        <Button onClick={handleAdd} disabled={loading} className="mt-2 w-full gap-2">
          <ShoppingCart className="h-4 w-4" />
          In den Warenkorb
        </Button>
      </div>
    </div>
  );
};

export default ProductCard;
