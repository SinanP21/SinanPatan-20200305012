import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLogin extends JDialog{
    private JTextField txtMail;
    private JPasswordField txtPassword;
    private JButton btnAdminLogin;
    private JPanel AdminLoginPanel;


    public AdminLogin(){
        //super(parent);
        setTitle("Admin Login");
        setContentPane(AdminLoginPanel);
        setPreferredSize(new Dimension(700, 700));
        pack();
        setResizable(false);
        setModal(true);
        //setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        btnAdminLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtMail.getText();
                String password = String.valueOf(txtPassword.getPassword());

                admin = getAuthenticateUser(email, password);

                if (admin != null){
                    AdminScreen adminScreen = new AdminScreen();
                    adminScreen.setVisible(true);

                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(AdminLogin.this,
                            "Email or Password Invalid",
                            "try again",
                            JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        });

    }



    public static Admin admin;
    private Admin getAuthenticateUser(String email, String password){
        Admin admin = null;

        final String DB_URL = "jdbc:mysql://localhost/20200305012";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                admin = new Admin();
                admin.email = resultSet.getString("email");
                admin.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return admin;
    }

    public static void main(String[] args) {
        AdminLogin adminLogin = new AdminLogin();
        adminLogin.setVisible(true);


        if (admin != null) {

            AdminScreen adminScreen = new AdminScreen();
            adminScreen.setVisible(true);
        }
        else {
            System.out.println("Auth canceled");


        }

    }

}
