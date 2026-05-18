export interface Product {
  id: number;
  name: string;
  beschreibung: string;
  preis: number;
  bildUrl: string;
}

export interface CartItem {
  productId: number;
  name: string;
  preis: number;
  menge: number;
  gesamtpreis: number;
}

export interface Cart {
  items: CartItem[];
  gesamtsumme: number;
}
