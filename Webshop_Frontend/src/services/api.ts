import { Product, Cart } from "@/types/shop";

const API_BASE = "/api";

// --- Mock Data ---
const mockProducts: Product[] = [
  {
    id: 1,
    name: "Wireless Kopfhörer",
    beschreibung: "Hochwertige Bluetooth-Kopfhörer mit Noise Cancelling und 30h Akkulaufzeit.",
    preis: 79.99,
    bildUrl: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop",
  },
  {
    id: 2,
    name: "Mechanische Tastatur",
    beschreibung: "RGB-beleuchtete mechanische Tastatur mit Cherry MX Switches.",
    preis: 129.99,
    bildUrl: "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400&h=400&fit=crop",
  },
  {
    id: 3,
    name: "USB-C Hub",
    beschreibung: "7-in-1 USB-C Hub mit HDMI, USB 3.0 und SD-Kartenleser.",
    preis: 49.99,
    bildUrl: "https://images.unsplash.com/photo-1625723044792-44de16ccb4e9?w=400&h=400&fit=crop",
  },
  {
    id: 4,
    name: "Laptop-Rucksack",
    beschreibung: "Wasserabweisender Rucksack mit gepolstertem Laptopfach für bis zu 15 Zoll.",
    preis: 59.99,
    bildUrl: "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop",
  },
  {
    id: 5,
    name: "Smartwatch",
    beschreibung: "Fitness-Tracker mit Herzfrequenzmessung, GPS und Schlafanalyse.",
    preis: 199.99,
    bildUrl: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop",
  },
  {
    id: 6,
    name: "Webcam HD",
    beschreibung: "Full-HD Webcam mit Mikrofon und Autofokus für Videokonferenzen.",
    preis: 69.99,
    bildUrl: "https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?w=400&h=400&fit=crop",
  },
];

// --- API Functions ---

export async function fetchProducts(): Promise<Product[]> {
  try {
    const res = await fetch(`${API_BASE}/products`);
    if (!res.ok) throw new Error("Produkte konnten nicht geladen werden");
    return res.json();
  } catch {
    console.warn("Backend nicht erreichbar, verwende Mock-Daten");
    return mockProducts;
  }
}

export async function fetchCart(): Promise<Cart> {
  const res = await fetch(`${API_BASE}/cart`);
  if (!res.ok) throw new Error("Warenkorb konnte nicht geladen werden");
  return res.json();
}

export async function addToCart(productId: number): Promise<Cart> {
  const res = await fetch(`${API_BASE}/cart/items`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ productId }),
  });
  if (!res.ok) throw new Error("Produkt konnte nicht hinzugefügt werden");
  return res.json();
}

export async function updateCartItem(productId: number, menge: number): Promise<Cart> {
  const res = await fetch(`${API_BASE}/cart/items/${productId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ menge }),
  });
  if (!res.ok) throw new Error("Menge konnte nicht aktualisiert werden");
  return res.json();
}

export async function removeCartItem(productId: number): Promise<Cart> {
  const res = await fetch(`${API_BASE}/cart/items/${productId}`, {
    method: "DELETE",
  });
  if (!res.ok) throw new Error("Produkt konnte nicht entfernt werden");
  return res.json();
}

export async function clearCart(): Promise<void> {
  const res = await fetch(`${API_BASE}/cart`, { method: "DELETE" });
  if (!res.ok) throw new Error("Warenkorb konnte nicht geleert werden");
}
