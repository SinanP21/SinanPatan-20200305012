import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;
    private JButton btnAdmin;

    public LoginForm(){
        //super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setPreferredSize(new Dimension(500,500));
        pack();
        setResizable(false);
        setModal(true);
        //setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticateUser(email, password);

                if (user != null){
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "try again",
                            JOptionPane.ERROR_MESSAGE);
                            dispose();

                       SignupForm f2 = new SignupForm();
                       f2.setVisible(true);
                }


            }
        });






        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AdminLogin adminLogin = new AdminLogin();
                adminLogin.setVisible(true);

            }
        });

        setVisible(true);

    }



    public static User user;
    private User getAuthenticateUser(String email, String password){
        User user = null;

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
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }



    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        User user  = LoginForm.user;



        if (user != null) {
            System.out.println("Successful Auth of: " + user.name);
            System.out.println("            Email: " + user.email);
            System.out.println("            Phone: " + user.phone);
            System.out.println("            Address: " + user.address);

            RentScreen rentS = new RentScreen();
            rentS.setVisible(true);



        }
        else {
            System.out.println("Auth canceled");


        }

    }


}


