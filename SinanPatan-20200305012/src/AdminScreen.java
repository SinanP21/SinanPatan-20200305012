import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminScreen extends JFrame {

    private JPanel adminPanel;
    private JTable table1;

    private JTextField txtSDate;
    private JTextField txtEDate;
    private JTextField txtBrandName;
    private JTextField txtModel;
    private JButton btnDelete;
    private JButton btnupdate;


    private Connection connection;

    public AdminScreen() {
        setTitle("Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setContentPane(adminPanel);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        connectToDatabase(); // Bağlantıyı oluştur
        loadStockDataFromDatabase(); // Tabloyu yükle

        btnupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String start = txtSDate.getText();
                String end = txtEDate.getText();
                String brand = txtBrandName.getText();
                String model = txtModel.getText();
                try {
                    // Veritabanına veri ekleme işlemleri
                    String query = "INSERT INTO rent (`StartDate`, `EndDate`, `BrandName`, `Model`) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, start);
                    statement.setString(2, end);
                    statement.setString(3, brand);
                    statement.setString(4, model);
                    statement.executeUpdate();

                    // Verilerin yüklenmesi
                    loadStockDataFromDatabase();

                    // Alanların temizlenmesi
                    txtSDate.setText("");
                    txtEDate.setText("");
                    txtBrandName.setText("");
                    txtModel.setText("");

                    JOptionPane.showMessageDialog(null, "Data saved successfully.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error occurred while saving data.");
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                    return;
                }
                String startDate = table1.getValueAt(selectedRow, 0).toString();
                try {
                    String query = "DELETE FROM rent WHERE `StartDate` = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, startDate);
                    statement.executeUpdate();
                    loadStockDataFromDatabase();
                    JOptionPane.showMessageDialog(null, "Data deleted successfully.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    private void loadStockDataFromDatabase() {
        // Veritabanından stok verilerini yükleme işlemleri burada yapılır
        try {
            String query = "SELECT * FROM rent";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = {"StartDate", "EndDate", "BrandName", "Model"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String start = resultSet.getString("StartDate");
                String end = resultSet.getString("EndDate");
                String brand = resultSet.getString("BrandName");
                String model1 = resultSet.getString("Model");
                Object[] rowData = {start, end, brand, model1};
                model.addRow(rowData);
            }
            table1.setModel(model);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/20200305012?useSSL=false", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    public static void main(String[] args) {
        //AdminScreen adminScreen = new AdminScreen();
        //adminScreen.setVisible(true);
    }
    }



