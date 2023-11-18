package  cn.bjut.jdbc;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AuditProductFrame extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;

    public AuditProductFrame() throws SQLException {
        initUI();
        refreshProductTable();
        setDefaultFont();
    }

    private void setDefaultFont() {
        Font font = new Font("微软雅黑", Font.PLAIN, 18);

        for (Component component : getComponents()) {
            setComponentFont(component, font);
        }
    }

    private void setComponentFont(Component component, Font font) {
        if (component instanceof JTextArea || component instanceof JLabel || component instanceof JButton) {
            component.setFont(font);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setComponentFont(child, font);
            }
        }
    }

    private void initUI() throws SQLException {
        setLayout(new BorderLayout());

        // Create table
        createProductTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        JButton deleteButton = new JButton("Delete");
        JButton oneClickApproveButton = new JButton("One-click Approve");
        JButton oneClickRejectButton = new JButton("One-click Reject");

        setButtonAction(approveButton, e -> {
            try {
                if(JOptionPane.showConfirmDialog(this, "Are you sure to approve this product?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {
                    approveSelectedProduct();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        setButtonAction(rejectButton, e -> {
            try {
                if(JOptionPane.showConfirmDialog(this, "Are you sure to reject this product?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {rejectSelectedProduct();}
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        setButtonAction(deleteButton, e -> {
            try {
                if(JOptionPane.showConfirmDialog(this, "Are you sure to delete this product?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {
                    deleteSelectedProduct();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        setButtonAction(oneClickApproveButton, e -> {
            try {
                int result = JOptionPane.showConfirmDialog(this, "Are you sure to approve all products?", "Confirm", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    oneClickApprove();
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        setButtonAction(oneClickRejectButton, e -> {

            try {
                int result = JOptionPane.showConfirmDialog(this, "Are you sure to reject all products?", "Confirm", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    oneClickReject();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(oneClickApproveButton);
        buttonPanel.add(oneClickRejectButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createProductTable() {
        String[] columns = {"ID", "Product Name", "Price", "Quantity", "desc", "Audit Status"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel);
        productTable.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());

        // Set font for the entire table
        productTable.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        productTable.setRowHeight(55);
    }

    private void refreshProductTable() {
        try {
            List<Product> products = getProductofNoAudit();
            tableModel.setRowCount(0); // Clear existing data

            for (Product product : products) {
                Object[] rowData = {
                        product.getP_id(),
                        product.getP_name(),
                        product.getP_price(),
                        product.getP_quantity(),
                        product.getP_desc(),
                        product.getP_audiStatus()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Product> getProductofNoAudit() throws SQLException {
        // Implement your database retrieval logic here
        // This method should return a list of products with "待审核" status
        return new DataControlProduct().getProductofNoAudit();
    }

    private void approveSelectedProduct() throws SQLException {
        handleProductAction("审核通过", "Please select a product to approve.");
    }

    private void rejectSelectedProduct() throws SQLException {
        handleProductAction("审核不通过", "Please select a product to reject.");
    }

    private void deleteSelectedProduct() throws SQLException {
        handleProductAction("delete", "Please select a product to delete.");
    }

    private void handleProductAction(String action, String errorMessage) throws SQLException {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) productTable.getValueAt(selectedRow, 0);

            switch (action) {
                case "审核通过":
                case "审核不通过":
                    new DataControlProduct().updateProductStatus(productId, action);
                    break;
                case "delete":
                    new DataControlProduct().deleteProduct(productId);
                    break;
            }

            refreshProductTable(); // Refresh the table after the update or deletion
        } else {
            JOptionPane.showMessageDialog(this, errorMessage, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void oneClickApprove() throws SQLException {
        List<Product> products = getProductofNoAudit();

        for (Product product : products) {
            new DataControlProduct().updateProductStatus(product.getP_id(), "审核通过");
        }

        refreshProductTable(); // Refresh the table after the bulk approval
    }

    private void oneClickReject() throws SQLException {
        List<Product> products = getProductofNoAudit();

        for (Product product : products) {
            new DataControlProduct().updateProductStatus(product.getP_id(), "审核不通过");
        }

        refreshProductTable(); // Refresh the table after the bulk rejection
    }

    private void setButtonAction(JButton button, ActionListener listener) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        });
    }

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        private Font font;

        public StatusCellRenderer() {
            this.font = new Font("微软雅黑", Font.PLAIN, 18); // Set your desired font and size
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setFont(font);

            // Customize status cell color
            String status = (String) value;
            if ("Active".equals(status)) {
                label.setForeground(Color.GREEN);
            } else if ("Inactive".equals(status)) {
                label.setForeground(Color.RED);
            }

            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Product Audit Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            try {
                frame.getContentPane().add(new AuditProductFrame());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
