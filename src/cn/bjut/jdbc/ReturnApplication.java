package  cn.bjut.jdbc;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ReturnApplication extends JPanel {
    private JTable returnTable;
    private DefaultTableModel tableModel;

    public ReturnApplication() throws SQLException {
        initUI();
        refreshReturnTable();
        setDefaultFont();
    }

    private void setDefaultFont() {
        // Set the default font for all components
        Font font = new Font("微软雅黑", Font.PLAIN, 18);
        for (Component component : getComponents()) {
            setComponentFont(component, font);
        }
    }

    private void setComponentFont(Component component, Font font) {
        // Recursively set font for all components and their children
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
        createReturnTable();
        JScrollPane scrollPane = new JScrollPane(returnTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");

        setButtonAction(approveButton, e -> {
            try {
                approveSelectedReturn();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        setButtonAction(rejectButton, e -> {
            try {
                rejectSelectedReturn();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createReturnTable() {
        String[] columns = {"ID", "Order ID", "Request Time", "Reason", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        returnTable = new JTable(tableModel);
        // Customize the cell renderer if needed
        returnTable.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        returnTable.setRowHeight(55);
    }

    private void refreshReturnTable() {
        try {
            List<ReturnRequest> returnRequests = getReturnRequests();
            tableModel.setRowCount(0); // Clear existing data

            for (ReturnRequest returnRequest : returnRequests) {
                Object[] rowData = {
                        returnRequest.getR_id(),
                        returnRequest.getO_id(),
                        returnRequest.getRequest_time(),
                        returnRequest.getReason(),
                        returnRequest.getStatus()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<ReturnRequest> getReturnRequests() throws SQLException {
        return new DataControlReturn().getReturnRequests();
    }

    private void approveSelectedReturn() throws SQLException {
        processReturn("审核通过");
    }

    private void rejectSelectedReturn() throws SQLException {
        processReturn("审核不通过");
    }

    private void processReturn(String action) throws SQLException {
        int selectedRow = returnTable.getSelectedRow();
        if (selectedRow != -1) {
            int returnRequestId = (int) returnTable.getValueAt(selectedRow, 0);

            // Perform processing for the selected return request (e.g., update status)
            // You need to implement the corresponding logic in DataControlReturn class
            new DataControlReturn().processReturnRequest(returnRequestId, action);

            refreshReturnTable(); // Refresh the table after processing
        } else {
            JOptionPane.showMessageDialog(this, "Please select a return request to process.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setButtonAction(JButton button, ActionListener listener) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Return Requests Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            try {
                frame.getContentPane().add(new ReturnApplication());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
