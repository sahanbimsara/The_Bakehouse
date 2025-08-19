package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        if (userName.isEmpty() || password.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"please enter username and password").show();
            return;
        }
        String sql = " SELECT * FROM user WHERE username = ?";
        try {
            ResultSet resultSet = CrudUtil.execute(sql,userName);

            if (resultSet.next()){
                String dbPassword = resultSet.getString("password");
                if (dbPassword.equals(password)){
                    // load user data into User Object
                    User user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            dbPassword
                    );
                    //Open My dashboard
                    Stage stage = new Stage();
                    try {
                        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboardForm.fxml"))));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.show();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
