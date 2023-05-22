import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RentScreen extends JDialog {
    private JPanel rentPanel;
    private JTextField txtStart;
    private JTextField txtEnd;
    private JButton btnRent;
    private JTextField txtBrandName;
    private JTextField txtCarModel;
    private JTable table1;
    private JButton btnDisplay;

    public RentScreen() {
        setTitle("Rent Screen");
        setContentPane(rentPanel);
        setPreferredSize(new Dimension(700, 700));
        pack();
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerCar();

            }
        });
        btnDisplay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/20200305012?useSSL=false","root","");
                    Statement st = con.createStatement();
                    String query = "SELECT * FROM rent";
                    ResultSet rs = st.executeQuery(query);

                    // DefaultTableModel ile JTable oluştur
                    DefaultTableModel model = new DefaultTableModel();
                    table1.setModel(model);

                    // Sütun başlıklarını ayarla
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        model.addColumn(rsmd.getColumnName(i));
                    }

                    // Verileri JTable'a ekle
                    while (rs.next()) {
                        Object[] rowData = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            rowData[i - 1] = rs.getObject(i);
                        }
                        model.addRow(rowData);
                    }

                    rs.close();
                    st.close();
                    con.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    throw new RuntimeException(ex);
                }



            }
        });
    }

    private void registerCar() {
        String Start = txtStart.getText();
        String End = txtEnd.getText();
        String brandname = txtBrandName.getText();
        String Model = txtCarModel.getText();


        if (Start.isEmpty() || End.isEmpty() || brandname.isEmpty() || Model.isEmpty() ) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }



        if (addUserToDatabase(Start, End, brandname, Model)) {
            JOptionPane.showMessageDialog(this,
                    "User registered successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            //dispose();
            //LoginForm f1 = new LoginForm();
            //f1.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addUserToDatabase(String Start, String End , String brandname, String Model ) {
        final String DB_URL = "jdbc:mysql://localhost:3306/20200305012?useSSL=false";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            String insertQuery = "INSERT INTO rent (StartDate, EndDate, BrandName, Model) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, Start);
            preparedStatement.setString(2, End);
            preparedStatement.setString(3, brandname);
            preparedStatement.setString(4, Model);


            int addedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            conn.close();

            return addedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        //RentScreen rentS = new RentScreen();
        //rentS.setVisible(true);
    }
}


