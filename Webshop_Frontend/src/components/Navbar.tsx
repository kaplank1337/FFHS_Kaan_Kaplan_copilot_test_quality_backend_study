import { Link, useLocation } from "react-router-dom";
import { ShoppingCart, Store } from "lucide-react";
import { useCart } from "@/context/CartContext";

const Navbar = () => {
  const { itemCount } = useCart();
  const location = useLocation();

  return (
    <header className="sticky top-0 z-50 border-b border-border bg-card/80 backdrop-blur-md">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <Link to="/" className="flex items-center gap-2 text-xl font-semibold text-foreground">
          <Store className="h-6 w-6 text-primary" />
          <span>Shop</span>
        </Link>

        <nav className="flex items-center gap-6">
          <Link
            to="/"
            className={`text-sm font-medium transition-colors hover:text-primary ${
              location.pathname === "/" ? "text-primary" : "text-muted-foreground"
            }`}
          >
            Produkte
          </Link>
          <Link
            to="/warenkorb"
            className={`relative flex items-center gap-1.5 text-sm font-medium transition-colors hover:text-primary ${
              location.pathname === "/warenkorb" ? "text-primary" : "text-muted-foreground"
            }`}
          >
            <ShoppingCart className="h-5 w-5" />
            Warenkorb
            {itemCount > 0 && (
              <span className="absolute -right-3 -top-2 flex h-5 w-5 items-center justify-center rounded-full bg-primary text-[11px] font-bold text-primary-foreground">
                {itemCount}
              </span>
            )}
          </Link>
        </nav>
      </div>
    </header>
  );
};

export default Navbar;
