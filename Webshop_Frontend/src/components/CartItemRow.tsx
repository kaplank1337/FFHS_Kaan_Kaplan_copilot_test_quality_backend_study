import { CartItem } from "@/types/shop";
import { useCart } from "@/context/CartContext";
import { Minus, Plus, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";

interface CartItemRowProps {
  item: CartItem;
}

const CartItemRow = ({ item }: CartItemRowProps) => {
  const { updateMenge, removeItem } = useCart();

  return (
    <div className="flex items-center gap-4 rounded-lg border border-border bg-card p-4">
      <div className="flex-1 min-w-0">
        <h3 className="font-medium text-foreground truncate">{item.name}</h3>
        <p className="text-sm text-muted-foreground">{item.preis.toFixed(2)} € pro Stück</p>
      </div>

      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8"
          onClick={() => updateMenge(item.productId, item.menge - 1)}
          disabled={item.menge <= 1}
        >
          <Minus className="h-3.5 w-3.5" />
        </Button>
        <span className="w-8 text-center text-sm font-semibold text-foreground">{item.menge}</span>
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8"
          onClick={() => updateMenge(item.productId, item.menge + 1)}
        >
          <Plus className="h-3.5 w-3.5" />
        </Button>
      </div>

      <div className="w-24 text-right">
        <p className="font-semibold text-foreground">{item.gesamtpreis.toFixed(2)} €</p>
      </div>

      <Button
        variant="ghost"
        size="icon"
        className="h-8 w-8 text-destructive hover:text-destructive"
        onClick={() => removeItem(item.productId)}
      >
        <Trash2 className="h-4 w-4" />
      </Button>
    </div>
  );
};

export default CartItemRow;
