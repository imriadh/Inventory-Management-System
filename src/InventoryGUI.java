import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class InventoryGUI {
    private JFrame frame;
    private JTextArea outputArea;

    public InventoryGUI() {
        // Create the main frame
        frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // Create components
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 4, 10, 10)); // 2 rows, 4 columns with spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addProductButton = createStyledButton("Add Product");
        JButton viewProductsButton = createStyledButton("View Products");
        JButton updateProductButton = createStyledButton("Update Product");
        JButton deleteProductButton = createStyledButton("Delete Product");
        JButton placeOrderButton = createStyledButton("Place Order");
        JButton viewOrdersButton = createStyledButton("View Orders");
        JButton lowStockButton = createStyledButton("Low Stock");
        JButton totalRevenueButton = createStyledButton("Total Revenue");
        // Add Supplier Button
        JButton addSupplierButton = createStyledButton("Add Supplier");
        buttonPanel.add(addSupplierButton);

        // View Suppliers Button
        JButton viewSuppliersButton = createStyledButton("View Suppliers");
        buttonPanel.add(viewSuppliersButton);

        // Update Supplier Button
        JButton updateSupplierButton = createStyledButton("Update Supplier");
        buttonPanel.add(updateSupplierButton);

        // Delete Supplier Button
        JButton deleteSupplierButton = createStyledButton("Delete Supplier");
        buttonPanel.add(deleteSupplierButton);

        // Add action listeners
        addSupplierButton.addActionListener(e -> addSupplierDialog());
        viewSuppliersButton.addActionListener(e -> viewAllSuppliers());
        updateSupplierButton.addActionListener(e -> updateSupplierDialog());
        deleteSupplierButton.addActionListener(e -> deleteSupplierDialog());

        // Add action listeners
        addProductButton.addActionListener(e -> addProductDialog());
        viewProductsButton.addActionListener(e -> viewAllProducts());
        updateProductButton.addActionListener(e -> updateProductDialog());
        deleteProductButton.addActionListener(e -> deleteProductDialog());
        placeOrderButton.addActionListener(e -> placeOrderDialog());
        viewOrdersButton.addActionListener(e -> viewOrders());
        lowStockButton.addActionListener(e -> findLowStockProducts());
        totalRevenueButton.addActionListener(e -> calculateTotalRevenue());

        // Add buttons to the panel
        buttonPanel.add(addProductButton);
        buttonPanel.add(viewProductsButton);
        buttonPanel.add(updateProductButton);
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(viewOrdersButton);
        buttonPanel.add(lowStockButton);
        buttonPanel.add(totalRevenueButton);

        // Output area for displaying results
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        // Add panels to the frame
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    // Add Product Dialog
    private void addProductDialog() {
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DBConnection.getConnection()) {
                String sql = "INSERT INTO Products (Name, Description, Price, Quantity) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nameField.getText());
                statement.setString(2, descriptionField.getText());
                statement.setDouble(3, Double.parseDouble(priceField.getText()));
                statement.setInt(4, Integer.parseInt(quantityField.getText()));
                statement.executeUpdate();

                outputArea.setText("Product added successfully!");
            } catch (SQLException | NumberFormatException e) {
                outputArea.setText("Error adding product: " + e.getMessage());
            }
        }
    }

    // View All Products
    private void viewAllProducts() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Products";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            StringBuilder result = new StringBuilder("ID\tName\tPrice\tQuantity\n");
            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String name = resultSet.getString("Name");
                double price = resultSet.getDouble("Price");
                int quantity = resultSet.getInt("Quantity");

                result.append(productID).append("\t")
                        .append(name).append("\t")
                        .append(price).append("\t")
                        .append(quantity).append("\n");
            }
            outputArea.setText(result.toString());
        } catch (SQLException e) {
            outputArea.setText("Error fetching products: " + e.getMessage());
        }
    }

    // Update Product Dialog
    private void updateProductDialog() {
        JTextField productIDField = new JTextField();
        JTextField quantityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Product ID:"));
        panel.add(productIDField);
        panel.add(new JLabel("New Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DBConnection.getConnection()) {
                String sql = "UPDATE Products SET Quantity = ? WHERE ProductID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(quantityField.getText()));
                statement.setInt(2, Integer.parseInt(productIDField.getText()));
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    outputArea.setText("Product updated successfully!");
                } else {
                    outputArea.setText("Product not found!");
                }
            } catch (SQLException | NumberFormatException e) {
                outputArea.setText("Error updating product: " + e.getMessage());
            }
        }
    }

    // Delete Product Dialog
    private void deleteProductDialog() {
        JTextField productIDField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        panel.add(new JLabel("Product ID:"));
        panel.add(productIDField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Delete Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DBConnection.getConnection()) {
                String checkSql = "SELECT COUNT(*) FROM Orders WHERE ProductID = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, Integer.parseInt(productIDField.getText()));
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    outputArea.setText("Cannot delete product as it has associated orders.");
                    return;
                }

                String deleteSql = "DELETE FROM Products WHERE ProductID = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, Integer.parseInt(productIDField.getText()));
                int rowsDeleted = deleteStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    outputArea.setText("Product deleted successfully!");
                } else {
                    outputArea.setText("Product not found!");
                }
            } catch (SQLException | NumberFormatException e) {
                outputArea.setText("Error deleting product: " + e.getMessage());
            }
        }
    }

    // Place Order Dialog
    private void placeOrderDialog() {
        JTextField productIDField = new JTextField();
        JTextField quantityField = new JTextField();
    
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Product ID:"));
        panel.add(productIDField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
    
        int result = JOptionPane.showConfirmDialog(null, panel, "Place Order",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DBConnection.getConnection()) {
                String checkSql = "SELECT Quantity FROM Products WHERE ProductID = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, Integer.parseInt(productIDField.getText()));
                ResultSet resultSet = checkStatement.executeQuery();
    
                if (resultSet.next()) {
                    int availableQuantity = resultSet.getInt("Quantity");
                    int orderQuantity = Integer.parseInt(quantityField.getText());
    
                    if (availableQuantity >= orderQuantity) {
                        String updateSql = "UPDATE Products SET Quantity = Quantity - ? WHERE ProductID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                        updateStatement.setInt(1, orderQuantity);
                        updateStatement.setInt(2, Integer.parseInt(productIDField.getText()));
                        updateStatement.executeUpdate();
    
                        String orderSql = "INSERT INTO Orders (ProductID, Quantity) VALUES (?, ?)";
                        PreparedStatement orderStatement = connection.prepareStatement(orderSql);
                        orderStatement.setInt(1, Integer.parseInt(productIDField.getText()));
                        orderStatement.setInt(2, orderQuantity);
                        orderStatement.executeUpdate();
    
                        outputArea.setText("Order placed successfully!");
                    } else {
                        outputArea.setText("Insufficient stock!");
                    }
                } else {
                    outputArea.setText("Product not found!");
                }
            } catch (SQLException | NumberFormatException e) {
                outputArea.setText("Error placing order: " + e.getMessage());
            }
        }
    }

    // View Orders
    private void viewOrders() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT O.OrderID, P.Name, O.Quantity, O.OrderDate " +
                    "FROM Orders O JOIN Products P ON O.ProductID = P.ProductID";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            StringBuilder result = new StringBuilder("OrderID\tProduct\tQuantity\tOrderDate\n");
            while (resultSet.next()) {
                int orderID = resultSet.getInt("OrderID");
                String productName = resultSet.getString("Name");
                int quantity = resultSet.getInt("Quantity");
                Date orderDate = resultSet.getDate("OrderDate");

                result.append(orderID).append("\t")
                        .append(productName).append("\t")
                        .append(quantity).append("\t")
                        .append(orderDate).append("\n");
            }
            outputArea.setText(result.toString());
        } catch (SQLException e) {
            outputArea.setText("Error fetching orders: " + e.getMessage());
        }
    }

    private void addSupplierDialog() {
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Supplier",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DBConnection.getConnection()) {
                String sql = "INSERT INTO Suppliers (Name, Contact, Email) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nameField.getText());
                statement.setString(2, contactField.getText());
                statement.setString(3, emailField.getText());
                statement.executeUpdate();

                outputArea.setText("Supplier added successfully!");
            } catch (SQLException e) {
                outputArea.setText("Error adding supplier: " + e.getMessage());
            }
        }
    }

    private void viewAllSuppliers() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Suppliers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            StringBuilder result = new StringBuilder("ID\tName\tContact\tEmail\n");
            while (resultSet.next()) {
                int supplierID = resultSet.getInt("SupplierID");
                String name = resultSet.getString("Name");
                String contact = resultSet.getString("Contact");
                String email = resultSet.getString("Email");

                result.append(supplierID).append("\t")
                        .append(name).append("\t")
                        .append(contact).append("\t")
                        .append(email).append("\n");
            }
            outputArea.setText(result.toString());
        } catch (SQLException e) {
            outputArea.setText("Error fetching suppliers: " + e.getMessage());
        }
    }

    private void updateSupplierDialog() {
        JTextField supplierIDField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Supplier ID:"));
        panel.add(supplierIDField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Supplier",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DBConnection.getConnection()) {
                String sql = "UPDATE Suppliers SET Name = ?, Contact = ?, Email = ? WHERE SupplierID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nameField.getText());
                statement.setString(2, contactField.getText());
                statement.setString(3, emailField.getText());
                statement.setInt(4, Integer.parseInt(supplierIDField.getText()));
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    outputArea.setText("Supplier updated successfully!");
                } else {
                    outputArea.setText("Supplier not found!");
                }
            } catch (SQLException | NumberFormatException e) {
                outputArea.setText("Error updating supplier: " + e.getMessage());
            }
        }
    }

    private void deleteSupplierDialog() {
        JTextField supplierIDField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        panel.add(new JLabel("Supplier ID:"));
        panel.add(supplierIDField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Delete Supplier",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = DBConnection.getConnection()) {
                String checkSql = "SELECT COUNT(*) FROM Products WHERE SupplierID = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, Integer.parseInt(supplierIDField.getText()));
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    outputArea.setText("Cannot delete supplier as they supply products.");
                    return;
                }

                String deleteSql = "DELETE FROM Suppliers WHERE SupplierID = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, Integer.parseInt(supplierIDField.getText()));
                int rowsDeleted = deleteStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    outputArea.setText("Supplier deleted successfully!");
                } else {
                    outputArea.setText("Supplier not found!");
                }
            } catch (SQLException | NumberFormatException e) {
                outputArea.setText("Error deleting supplier: " + e.getMessage());
            }
        }
    }

    // Find Low-Stock Products
    private void findLowStockProducts() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT Name, Description, Price, Quantity FROM Products WHERE Quantity < 10";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            StringBuilder result = new StringBuilder("Name\tDescription\tPrice\tQuantity\n");
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                double price = resultSet.getDouble("Price");
                int quantity = resultSet.getInt("Quantity");

                result.append(name).append("\t")
                        .append(description).append("\t")
                        .append(price).append("\t")
                        .append(quantity).append("\n");
            }
            outputArea.setText(result.toString());
        } catch (SQLException e) {
            outputArea.setText("Error finding low-stock products: " + e.getMessage());
        }
    }

    // Calculate Total Revenue
    private void calculateTotalRevenue() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT SUM(P.Price * O.Quantity) AS TotalRevenue " +
                    "FROM Products P JOIN Orders O ON P.ProductID = O.ProductID";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                double totalRevenue = resultSet.getDouble("TotalRevenue");
                outputArea.setText("Total Revenue: $" + totalRevenue);
            } else {
                outputArea.setText("No revenue data available.");
            }
        } catch (SQLException e) {
            outputArea.setText("Error calculating total revenue: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryGUI::new);
    }
}