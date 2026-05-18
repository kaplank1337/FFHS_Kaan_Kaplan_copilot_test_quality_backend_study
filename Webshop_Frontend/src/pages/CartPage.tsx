import { useCart } from "@/context/CartContext";
import CartItemRow from "@/components/CartItemRow";
import { Button } from "@/components/ui/button";
import { ShoppingCart, Trash2, ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";

const CartPage = () => {
  const { cart, clearCart, itemCount } = useCart();

  if (cart.items.length === 0) {
    return (
      <main className="container mx-auto flex flex-col items-center justify-center px-4 py-20 text-center">
        <ShoppingCart className="mb-4 h-16 w-16 text-muted-foreground" />
        <h1 className="mb-2 text-2xl font-bold text-foreground">Dein Warenkorb ist leer</h1>
        <p className="mb-6 text-muted-foreground">Füge Produkte hinzu, um loszulegen.</p>
        <Link to="/">
          <Button className="gap-2">
            <ArrowLeft className="h-4 w-4" />
            Zurück zu den Produkten
          </Button>
        </Link>
      </main>
    );
  }

  return (
    <main className="container mx-auto px-4 py-8">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-foreground">
          Warenkorb ({itemCount} {itemCount === 1 ? "Artikel" : "Artikel"})
        </h1>
        <Button variant="outline" className="gap-2 text-destructive hover:text-destructive" onClick={clearCart}>
          <Trash2 className="h-4 w-4" />
          Warenkorb leeren
        </Button>
      </div>

      <div className="flex flex-col gap-3">
        {cart.items.map((item) => (
          <CartItemRow key={item.productId} item={item} />
        ))}
      </div>

      <div className="mt-6 flex items-center justify-between rounded-lg border border-border bg-card p-6">
        <span className="text-lg font-medium text-muted-foreground">Gesamtsumme</span>
        <span className="text-2xl font-bold text-foreground">{cart.gesamtsumme.toFixed(2)} €</span>
      </div>

      <div className="mt-4">
        <Link to="/">
          <Button variant="outline" className="gap-2">
            <ArrowLeft className="h-4 w-4" />
            Weiter einkaufen
          </Button>
        </Link>
      </div>
    </main>
  );
};

export default CartPage;
