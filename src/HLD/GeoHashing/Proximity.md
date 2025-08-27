# GeoHashing & Quad Tree in Uber-like Systems

## 1. GeoHashing Algorithm

### 🔹 What is GeoHashing?
- GeoHashing is a **spatial indexing algorithm** that encodes latitude and longitude into a **short alphanumeric string**.
- It divides the world into **grid cells**, where nearby locations share similar prefixes in their geohash.

### 🔹 How it Works
1. Start with latitude range `[-90, 90]` and longitude range `[-180, 180]`.
2. Iteratively divide ranges into halves.
3. Assign binary values (0 for lower half, 1 for upper half).
4. Interleave latitude and longitude bits.
5. Convert the final binary into **Base32 encoding** → GeoHash string.

### 🔹 Example
- Coordinates `(37.7749, -122.4194)` → `9q8yy` (San Francisco).
- Prefix `9q8` covers a grid area around SF → all points with `9q8` are in close proximity.

### 🔹 Pros & Cons
✅ Easy to compute.  
✅ Prefix matching helps in finding nearby locations.  
❌ Uneven cell size near poles (distortion).  
❌ Fixed cell size → may not suit variable density.

### 🔹 Java Example: Generate Geohash
```java
public class GeoHash {
    private static final String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    public static String encode(double lat, double lon, int precision) {
        boolean isEven = true;
        int bit = 0, ch = 0;
        StringBuilder geohash = new StringBuilder();

        double[] latRange = {-90.0, 90.0};
        double[] lonRange = {-180.0, 180.0};

        while (geohash.length() < precision) {
            double mid;
            if (isEven) {
                mid = (lonRange[0] + lonRange[1]) / 2;
                if (lon > mid) {
                    ch |= 1 << (4 - bit);
                    lonRange[0] = mid;
                } else {
                    lonRange[1] = mid;
                }
            } else {
                mid = (latRange[0] + latRange[1]) / 2;
                if (lat > mid) {
                    ch |= 1 << (4 - bit);
                    latRange[0] = mid;
                } else {
                    latRange[1] = mid;
                }
            }

            isEven = !isEven;
            if (bit < 4) {
                bit++;
            } else {
                geohash.append(BASE32.charAt(ch));
                bit = 0;
                ch = 0;
            }
        }
        return geohash.toString();
    }

    public static void main(String[] args) {
        System.out.println(encode(37.7749, -122.4194, 7)); // Example: 9q8yyk8
    }
}


```
### 🔹 Finding Nearby Locations
- To find nearby drivers, compute the geohash of the rider's location.
- Retrieve drivers with geohashes that share the same prefix (e.g., first 5 characters).
- This reduces the search space significantly.  
- Use a database like **Redis** with GEO commands for efficient spatial queries.
```sql
GEOADD drivers 37.7749 -122.4194 driver1
GEORADIUS drivers 37.7749 -122.4194 5 km WITHDIST
```

## 2. Quad Tree Algorithm
# Quad Tree

## 🔹 What is a Quad Tree?
A **tree-based spatial data structure** that recursively divides a **2D space** into 4 quadrants.

Each node represents a bounding box:
- **NE (Northeast)**
- **NW (Northwest)**
- **SE (Southeast)**
- **SW (Southwest)**

---

## 🔹 How it Works
1. Start with the **entire map as the root node**.
2. Insert points into the node.
3. If a node exceeds capacity → **split into 4 children**.
4. Query by checking relevant quadrants.

---

## 🔹 Example
- **Root** → entire city map.
- **Children** → 4 quadrants of the city.
- Each quadrant splits further if **density increases**.

---

## 🔹 Pros & Cons
### ✅ Pros
- Efficient **range queries** (e.g., find all drivers within a radius).
- Handles **variable density** better than fixed grid.

### ❌ Cons
- More **complex to implement** compared to GeoHash.
- Requires **tree balancing overhead**.  

``` Java
import java.util.*;

class Point {
    double x, y;
    Point(double x, double y) { this.x = x; this.y = y; }
}

class Boundary {
    double x, y, w, h;
    Boundary(double x, double y, double w, double h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }
    boolean contains(Point p) {
        return (p.x >= x-w && p.x <= x+w && p.y >= y-h && p.y <= y+h);
    }
}

class QuadTree {
    private Boundary boundary;
    private int capacity;
    private List<Point> points;
    private boolean divided;
    private QuadTree ne, nw, se, sw;

    QuadTree(Boundary boundary, int capacity) {
        this.boundary = boundary;
        this.capacity = capacity;
        this.points = new ArrayList<>();
        this.divided = false;
    }

    public void subdivide() {
        double x = boundary.x, y = boundary.y, w = boundary.w/2, h = boundary.h/2;
        ne = new QuadTree(new Boundary(x+w, y-h, w, h), capacity);
        nw = new QuadTree(new Boundary(x-w, y-h, w, h), capacity);
        se = new QuadTree(new Boundary(x+w, y+h, w, h), capacity);
        sw = new QuadTree(new Boundary(x-w, y+h, w, h), capacity);
        divided = true;
    }

    public boolean insert(Point p) {
        if (!boundary.contains(p)) return false;
        if (points.size() < capacity) {
            points.add(p);
            return true;
        }
        if (!divided) subdivide();
        return (ne.insert(p) || nw.insert(p) || se.insert(p) || sw.insert(p));
    }
}

public List<Point> query(Boundary range, List<Point> found) {
    if (!boundary.intersects(range)) return found;
    for (Point p : points) {
        if (range.contains(p)) found.add(p);
    }
    if (divided) {
        ne.query(range, found);
        nw.query(range, found);
        se.query(range, found);
        sw.query(range, found);
    }
    return found;
}

public static void main(String[] args) {
    Boundary boundary = new Boundary(0, 0, 200, 200);
    QuadTree qt = new QuadTree(boundary, 4);

    // Insert points (drivers)
    qt.insert(new Point(50, 50));
    qt.insert(new Point(60, 60));
    qt.insert(new Point(70, 70));
    qt.insert(new Point(80, 80));
    qt.insert(new Point(90, 90));

    // Query range
    Boundary range = new Boundary(75, 75, 30, 30);
    List<Point> found = new ArrayList<>();
    qt.query(range, found);

    System.out.println("Found points: " + found.size());
}

```
## 🔹 3. GeoHashing vs Quad Tree in Uber-like Systems

| Feature              | GeoHashing                  | Quad Tree                       |
| -------------------- | --------------------------- | ------------------------------- |
| **Structure**        | Grid-based, string encoding | Tree-based spatial partitioning |
| **Querying**         | Prefix-based lookups        | Recursive quadrant search       |
| **Complexity**       | Easy to compute             | More complex, adaptive          |
| **Density Handling** | Fixed cell size             | Variable cell density           |
| **Use Case**         | Fast nearby driver lookup   | Dynamic clustering of drivers   |



## 4\. Real-World Use Cases

### 🚗 Ride-Hailing (Uber, Lyft)
- Find nearest drivers to a passenger.
- Balance load by region.

### 🍔 Food Delivery (Zomato, DoorDash)
- Assign orders to nearest delivery partner.

### 📱 Social Apps (Facebook, Instagram, Tinder)
- Show nearby people or events.

### 📍 Proximity Services
- Location-based advertisements.
- Emergency services dispatch.
- IoT devices nearest neighbor detection.
