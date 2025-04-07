# Inventory Management System

A simple and user-friendly **Inventory Management System** built using Java Swing for the GUI and Oracle 21c as the database. This application allows users to manage products, suppliers, and orders efficiently.

---

## Features

- **Product Management**: Add, view, update, and delete products.
- **Supplier Management**: Add, view, update, and delete suppliers.
- **Order Placement**: Place orders for products and update stock levels.
- **Low Stock Alerts**: Identify products with low stock levels.
- **Total Revenue Calculation**: Calculate total revenue from all orders.
- **Data Persistence**: All data is stored in an Oracle 21c database.

---

## Technologies Used

- **Frontend**: Java Swing (GUI)
- **Backend**: Oracle 21c
- **Programming Language**: Java
- **Database Connectivity**: JDBC

---

## Setup Instructions

### Prerequisites

1. Install **Oracle 21c**:
   - Download and install [Oracle 21c](https://www.oracle.com/database/technologies/oracle21c.html).
   - Set up a database user and schema.

2. Install **Java Development Kit (JDK)**:
   - Ensure you have JDK 11 or later installed.

3. Add **Oracle JDBC Driver**:
   - Download the `ojdbc10.jar` or `ojdbc8.jar` file from the [Oracle JDBC Drivers page](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html).
   - Add the JAR file to your project's classpath.

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/inventory-management-system.git
   cd inventory-management-system
