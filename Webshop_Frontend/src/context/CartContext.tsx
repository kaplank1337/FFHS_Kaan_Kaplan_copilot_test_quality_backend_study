import React, { createContext, useContext, useState, useCallback, useEffect } from "react";
import { Cart, Product } from "@/types/shop";
import * as api from "@/services/api";

interface CartContextType {
  cart: Cart;
  addProduct: (product: Product) => Promise<void>;
  updateMenge: (productId: number, menge: number) => Promise<void>;
  removeItem: (productId: number) => Promise<void>;
  clearCart: () => Promise<void>;
  itemCount: number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [cart, setCart] = useState<Cart>({ items: [], gesamtsumme: 0 });

  useEffect(() => {
    api.fetchCart().then(setCart).catch(() => {});
  }, []);

  const addProduct = useCallback(async (product: Product) => {
    const updated = await api.addToCart(product.id);
    setCart(updated);
  }, []);

  const updateMenge = useCallback(async (productId: number, menge: number) => {
    if (menge < 1) {
      const updated = await api.removeCartItem(productId);
      setCart(updated);
    } else {
      const updated = await api.updateCartItem(productId, menge);
      setCart(updated);
    }
  }, []);

  const removeItem = useCallback(async (productId: number) => {
    const updated = await api.removeCartItem(productId);
    setCart(updated);
  }, []);

  const clearCartFn = useCallback(async () => {
    await api.clearCart();
    setCart({ items: [], gesamtsumme: 0 });
  }, []);

  const itemCount = cart.items.reduce((sum, i) => sum + i.menge, 0);

  return (
    <CartContext.Provider value={{ cart, addProduct, updateMenge, removeItem, clearCart: clearCartFn, itemCount }}>
      {children}
    </CartContext.Provider>
  );
};

export function useCart() {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error("useCart must be used within CartProvider");
  return ctx;
}
