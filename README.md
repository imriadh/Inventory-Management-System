# Inventory Management System

A simple and user-friendly **Inventory Management System** built using Java Swing for the GUI and Oracle 11g Express Edition as the database. This application allows users to manage products, suppliers, and orders efficiently.

---

## Features

- **Product Management**: Add, view, update, and delete products.
- **Supplier Management**: Add, view, update, and delete suppliers.
- **Order Placement**: Place orders for products and update stock levels.
- **Low Stock Alerts**: Identify products with low stock levels.
- **Total Revenue Calculation**: Calculate total revenue from all orders.
- **Data Persistence**: All data is stored in an Oracle database.

---

## Technologies Used

- **Frontend**: Java Swing (GUI)
- **Backend**: Oracle 11g Express Edition
- **Programming Language**: Java
- **Database Connectivity**: JDBC

---

## Setup Instructions

### Prerequisites

1. Install **Oracle 11g Express Edition**:
   - Download and install [Oracle 11g XE](https://www.oracle.com/database/technologies/xe-prior-releases.html).
   - Set up a database user and schema.

2. Install **Java Development Kit (JDK)**:
   - Ensure you have JDK 8 or later installed.

3. Add **Oracle JDBC Driver**:
   - Download the `ojdbc6.jar` or `ojdbc7.jar` file from the [Oracle JDBC Drivers page](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html).
   - Add the JAR file to your project's classpath.

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/inventory-management-system.git
   cd inventory-management-system
