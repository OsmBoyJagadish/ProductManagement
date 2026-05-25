# ProjectManagement
-----------------------


## Description :

A Spring Boot REST API for product ordering and cart management using JWT Authentication and Role-Based Authorization.

The application supports:
- JWT Authentication
- Admin and User Roles
- Product Management
- Cart System
- Order Placement
- Inventory Management
- Transactional Orders


## MySQL Database used.

Main Tables:

- users
Columns:
id, email, name, password, role

- products
Columns:
id, created_at, description, name, price, quantity, status enum('ACTIVE','INACTIVE'), updated_at

- cart
Columns:
id, user_id

- cart_items
Columns:
id, quantity, cart_id, product_id

- orders
Columns:
id, created_at, status enum('CANCELLED','CONFIRMED','DELIVERED','PENDING'), total_amount, user_id

- order_items
Columns:
id, price_at_order, quantity, order_id, product_id


## API Endpoints

------------------------------------------------------------------------------
| Method | Endpoint                       | Access | Description                   |


|--------|--------------------------------|--------|-------------------------------|

| POST   | /api/auth/register             | Public | Register as USER              |

| POST   | /api/auth/register/admin       | Public | Register as ADMIN             |

| POST   | /api/auth/login                | Public | Login                         |

| POST   | /api/products                  | ADMIN  | Add new product               |

| PUT    | /api/products/{id}             | ADMIN  | Update price / quantity       |

| PATCH  | /api/products/{id}/enable      | ADMIN  | Enable product                |

| PATCH  | /api/products/{id}/disable     | ADMIN  | Disable product               |

| GET    | /api/products/all              | ADMIN  | View all products             |

| GET    | /api/products                  | USER   | View active products          |

| GET    | /api/products/{id}             | USER   | Get product by ID             |

| GET    | /api/cart                      | USER   | View cart                     |

| POST   | /api/cart/items                | USER   | Add item to cart              |

| PUT    | /api/cart/items/{id}           | USER   | Update cart item quantity     |

| DELETE | /api/cart/items/{id}           | USER   | Remove item from cart         |

| DELETE | /api/cart                      | USER   | Clear entire cart             |

| POST   | /api/orders/place              | USER   | Place order from cart         |

| GET    | /api/orders                    | USER   | View order history            |

| GET    | /api/orders/{id}               | USER   | Get order by ID               |

------------------------------------------------------------------------------------


## Testing Flow

1. Register ADMIN
2. Login as ADMIN
3. Add Products
4. Update Products
5. Get all products
6. Register USER
7. Login as USER
8. Browse Products
9. GEt active Products
10. View Cart
11. Update Cart
12. Clear entire cart
13. 7. Add to Cart
14. Place Order
15. Order History
16. GEt specific order







