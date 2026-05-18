# Webshop Frontend

Ein modernes React-Frontend für einen kleinen Webshop. Die Anwendung zeigt eine Produktübersicht und einen Warenkorb. Sie ist so aufgebaut, dass sie später einfach an eine Spring-Boot-REST-API angebunden werden kann.

## Technologien

- **React 18** mit TypeScript
- **Vite** als Build-Tool
- **Tailwind CSS** für das Styling
- **React Router** für die Navigation
- **shadcn/ui** als UI-Komponentenbibliothek
- **TanStack Query** für Server-State-Management
- **Vitest** für Unit-Tests

## Lokale Entwicklung

### Voraussetzungen

- **Node.js** (Version 18 oder höher)
- **npm** oder **bun** (bevorzugt: `bun`)

### Schritt-für-Schritt-Anleitung

1. **Repository klonen:**
   ```bash
   git clone <repository-url>
   cd <projektordner>
   ```

2. **Abhängigkeiten installieren:**
   ```bash
   npm install
   ```
   Oder mit Bun:
   ```bash
   bun install
   ```

3. **Entwicklungsserver starten:**
   ```bash
   npm run dev
   ```
   Oder mit Bun:
   ```bash
   bun run dev
   ```

4. **Anwendung öffnen:**
   Der Server läuft standardmäßig unter `http://localhost:8080`

5. **Tests ausführen:**
   ```bash
   npm run test
   ```

6. **Build für Produktion:**
   ```bash
   npm run build
   ```

## Projektstruktur

```
src/
  components/       # Wiederverwendbare UI-Komponenten
    ProductCard.tsx     # Produktkarte auf der Startseite
    CartItemRow.tsx     # Zeile im Warenkorb
    Navbar.tsx          # Navigationsleiste
  context/
    CartContext.tsx     # Globaler Warenkorb-Zustand
  pages/
    Index.tsx           # Startseite mit Produktliste
    CartPage.tsx        # Warenkorb-Seite
    NotFound.tsx        # 404-Fehlerseite
  services/
    api.ts              # REST-API-Client (Mock + Real)
  types/
    shop.ts             # TypeScript-Typdefinitionen
```

## Mock-Modus vs. API-Modus

In `src/services/api.ts` gibt es die Konstante `USE_MOCK`:

- **`USE_MOCK = true`** (Standard): Die Anwendung zeigt fest eingebaute Demo-Produkte an und verwaltet den Warenkorb lokal im Browser-Speicher.
- **`USE_MOCK = false`**: Die Anwendung sendet echte HTTP-Anfragen an die unten dokumentierte REST-API.

Um die Anwendung an deine Spring-Boot-API anzubinden, setze `USE_MOCK` auf `false` und passe gegebenenfalls die `API_BASE`-URL an.

## REST-API-Schnittstellen

Das Frontend erwartet folgende Endpunkte von der Spring-Boot-API. Basis-URL ist konfigurierbar (Standard: `/api`).

### Produkte

#### `GET /api/products`

Liefert alle verfügbaren Produkte.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Wireless Kopfhörer",
    "beschreibung": "Hochwertige Bluetooth-Kopfhörer...",
    "preis": 79.99,
    "bildUrl": "https://example.com/image.jpg"
  }
]
```

### Warenkorb

#### `GET /api/cart`

Liefert den aktuellen Warenkorb des Nutzers.

**Response (200 OK):**
```json
{
  "items": [
    {
      "productId": 1,
      "name": "Wireless Kopfhörer",
      "preis": 79.99,
      "menge": 2,
      "gesamtpreis": 159.98
    }
  ],
  "gesamtsumme": 159.98
}
```

#### `POST /api/cart/items`

Fügt ein Produkt zum Warenkorb hinzu.

**Request-Body:**
```json
{
  "productId": 1
}
```

**Response (200 OK):** Aktualisierter Warenkorb (siehe `GET /api/cart`)

#### `PUT /api/cart/items/{productId}`

Aktualisiert die Menge eines Warenkorb-Artikels.

**Request-Body:**
```json
{
  "menge": 3
}
```

**Response (200 OK):** Aktualisierter Warenkorb

#### `DELETE /api/cart/items/{productId}`

Entfernt ein Produkt aus dem Warenkorb.

**Response (200 OK):** Aktualisierter Warenkorb

#### `DELETE /api/cart`

Leert den gesamten Warenkorb.

**Response (200 OK):** Leerer Warenkorb oder `204 No Content`

### Datentypen

```typescript
interface Product {
  id: number;
  name: string;
  beschreibung: string;
  preis: number;
  bildUrl: string;
}

interface CartItem {
  productId: number;
  name: string;
  preis: number;
  menge: number;
  gesamtpreis: number;
}

interface Cart {
  items: CartItem[];
  gesamtsumme: number;
}
```

## CORS-Konfiguration

Falls das Frontend lokal auf `http://localhost:8080` läuft und die API auf einem anderen Port (z.B. `http://localhost:8081`), muss die Spring-Boot-Anwendung CORS für `http://localhost:8080` erlauben:

```java
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ShopController {
    // ...
}
```
