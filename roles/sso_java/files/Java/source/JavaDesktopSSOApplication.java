package sample;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class Main extends Application {

    Stage window;
    Scene scene;
    String current_username = "";
    String current_password = "";
    String current_domain = "";
    String sTempDb = getDatabasePath();
    final String sDriverName = "org.sqlite.JDBC";
    final String sJdbc = "jdbc:sqlite";
    final String sDbUrl = sJdbc + ":" + sTempDb;
    int passwordAttempts = 0;

    public String getDatabasePath(){
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        int lastIndex = path.lastIndexOf('/');
        return path.substring(1, lastIndex) + "/mydatabase.sqlite";
    }

    public boolean isPasswordValid(String password){

        if (password.length() < 6 || password.length() > 32)
            return false;

        char[] allowedSymbols = {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm'
                , 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
                , 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'
                , 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
                , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '$', '?'
                , '_', '-', '@', '!', '.', ',', ':', ';', '{', '}', '(', ')', '%'
        };

        for (char elem : password.toCharArray()) {
            boolean bOK = false;
            for (char symb : allowedSymbols) {
                if (elem == symb)
                    bOK = true;
            }
            if (!bOK)
                return bOK;
        }

        return true;
    }

    public void displayLoginWindow(String errorMessage) {
        VBox mainLayout = new VBox(10);
        GridPane grid = new GridPane();

        PasswordField passwordTextField = new PasswordField();
        TextField loginTextField = new TextField();

        Label loginLabel = new Label("Login");
        Label passwordLabel = new Label("Password");
        Label domainLabel = new Label("Domain");
        ChoiceBox<String> domainChoiceBox = new ChoiceBox<String>();
        ButtonBar buttonBar = new ButtonBar();

        Button OKButton = new Button("OK");
        Button cancelButton = new Button("Exit");

        buttonBar.getButtons().addAll(OKButton, cancelButton);

        Hyperlink addUserLink = new Hyperlink("Create user");

        addUserLink.setOnAction(e -> displayAddUserWindow(""));

        GridPane.setConstraints(loginLabel, 0, 0);
        GridPane.setConstraints(passwordLabel, 0, 1);
        GridPane.setConstraints(loginTextField, 1, 0);
        GridPane.setConstraints(passwordTextField, 1, 1);
        GridPane.setConstraints(domainLabel, 0, 2);
        GridPane.setConstraints(domainChoiceBox, 1, 2);
        GridPane.setConstraints(buttonBar, 1, 3);
        GridPane.setConstraints(addUserLink, 0, 3);

        grid.setAlignment(Pos.CENTER);

        int count = 0;
        try
        {
            String sMakeSelect = "SELECT DISTINCT DOMAIN FROM Users";
            Class.forName(sDriverName);
            Connection conn = DriverManager.getConnection(sDbUrl);
            try {
                Statement statement = conn.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery(sMakeSelect);
                    try {
                        while(resultSet.next())
                        {
                            count++;
                            String sResult = resultSet.getString("DOMAIN");
                            domainChoiceBox.getItems().add(sResult);
                        }
                    } finally {
                        try { resultSet.close(); } catch (Exception ignore) {}
                    }
                } finally {
                    try { statement.close(); } catch (Exception ignore) {}
                }
            } finally {
                try { conn.close(); } catch (Exception ignore) {}
            }
            conn.close();
        }
        catch (Exception e)
        {

        }
        if (count == 0) {
            domainChoiceBox.getItems().add("Empty database!");
            domainChoiceBox.getSelectionModel().select(0);
            domainChoiceBox.setDisable(false);
            OKButton.setDisable(true);
        }
        else
            domainChoiceBox.getSelectionModel().select(0);

        grid.getChildren().addAll(loginLabel, loginTextField, passwordLabel, passwordTextField, domainLabel, domainChoiceBox, buttonBar, addUserLink);

        cancelButton.setOnAction(e -> window.close());
        OKButton.setOnAction(e -> {
            String username = loginTextField.getText().toString();
            String password = passwordTextField.getText().toString();
            String domain = domainChoiceBox.getValue().toString();

            String sMakeSelect = "SELECT * FROM Users WHERE UPPER(LOGIN) LIKE UPPER('" + username + "') AND UPPER(DOMAIN) LIKE UPPER('" + domain + "') AND PASSWORD = '"+ password + "'";
            Boolean bOK = false;

            Statement statement;
            ResultSet resultSet;
            try
            {
                Class.forName(sDriverName);
                Connection conn = DriverManager.getConnection(sDbUrl);
                statement = conn.createStatement();
                resultSet = statement.executeQuery(sMakeSelect);

                while(resultSet.next()) {
                    current_username = resultSet.getString("LOGIN");
                    bOK = true;
                }
                if (bOK)
                {

                    current_password = password;
                    current_domain = domain;
                    passwordAttempts = 0;
                    displayMainWindow("You have successfully logged in");
                }
                else
                {
                    passwordAttempts++;
                    if (passwordAttempts  > 2) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Password Alert");
                        alert.setHeaderText("You have tried to use invalid credentials 3 times. Program will be closed.");
                        alert.showAndWait();
                        window.close();
                        return;
                    }

                    displayLoginWindow("Invalid credentials");
                }
            }
            catch (Exception exc)
            {
                Label error = new Label("Invalid credentials!");
                error.setAlignment(Pos.CENTER);
            }

        });

        final String gridStyle = "-fx-hgap: 10; -fx-vgap: 10;";
        grid.setStyle(gridStyle);
        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-text-fill: red;");
        mainLayout.getChildren().addAll(grid, errorLabel);
        mainLayout.setAlignment(Pos.CENTER);
        scene = new Scene(mainLayout, 400, 300);
        window.setScene(scene);
        window.show();
    }

    public void displayMainWindow(String errorMessage){
        VBox mainLayout = new VBox(10);
        GridPane grid = new GridPane();
        Hyperlink changePasswordLink = new Hyperlink("Change password");
        Hyperlink logOutLink = new Hyperlink("Log out");
        Label usernameLabel = new Label("Hello, " + current_username + "!");
        GridPane.setConstraints(usernameLabel, 0, 0);
        GridPane.setConstraints(changePasswordLink, 0, 1);
        GridPane.setConstraints(logOutLink, 0, 2);

        changePasswordLink.setOnAction(e -> displayChangePasswordWindow(""));
        logOutLink.setOnAction(e -> {
            current_domain = "";
            current_password = "";
            current_username = "";
            displayLoginWindow("");
        });

        grid.getChildren().addAll(logOutLink, changePasswordLink, usernameLabel);

        grid.setAlignment(Pos.CENTER);

        final String gridStyle = "-fx-hgap: 10; -fx-vgap: 10;";

        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.setStyle(gridStyle);
        mainLayout.getChildren().addAll(grid, errorLabel);

        mainLayout.setAlignment(Pos.CENTER);

        scene = new Scene(mainLayout, 400, 300);
        window.setScene(scene);
        window.show();
    }

    public void displayAddUserWindow(String errorMessage) {
        VBox mainVBox = new VBox(10);
        GridPane grid = new GridPane();

        TextField loginTextField = new TextField();
        TextField domainTextField = new TextField();
        PasswordField passwordTextField = new PasswordField();
        PasswordField passwordAgainTextField = new PasswordField();

        Label loginLabel = new Label("Login");
        Label passwordLabel = new Label("Password");
        Label domainLabel = new Label("Domain");
        Label passwordAgainLabel = new Label("Password again");

        ButtonBar buttonBar = new ButtonBar();

        Button OKButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        OKButton.setOnAction(e -> {
            String username = loginTextField.getText().toString();
            String domain = domainTextField.getText().toString();
            String password = passwordTextField.getText().toString();
            String passwordAgain = passwordAgainTextField.getText().toString();

            boolean bOK = true;
            if (username.isEmpty() || domain.isEmpty() || password.isEmpty() || !password.equals(passwordAgain))
                bOK = false;

            if (!isPasswordValid(password)) {
                displayAddUserWindow("Your password does not meet security requirements!");
                return;
            }
            if (bOK) {
                String sMakeSelect = "SELECT * FROM Users WHERE UPPER(LOGIN) LIKE UPPER('" + username + "') AND UPPER(DOMAIN) LIKE UPPER('" + domain + "')";
                Statement statement;
                ResultSet resultSet;
                try {
                    bOK = false;
                    Class.forName(sDriverName);
                    Connection conn = DriverManager.getConnection(sDbUrl);
                    statement = conn.createStatement();
                    resultSet = statement.executeQuery(sMakeSelect);
                    while (resultSet.next()) {
                        bOK = true;
                    }
                    if (bOK) {
                        displayAddUserWindow("This user already exists in current domain");
                    } else {
                        Statement statementInsert;
                        statementInsert = conn.createStatement();
                        String sMakeInsert = "INSERT INTO Users (LOGIN, PASSWORD, DOMAIN) VALUES ('" + username + "', '" + password + "', '" + domain.toUpperCase() + "')";
                        statementInsert.executeUpdate(sMakeInsert);
                        displayLoginWindow("User was created successfully!");
                    }
                } catch (Exception exc) {
                    displayLoginWindow("User might not be created!");
                }
            }
            else{
                displayAddUserWindow("Invalid credentials!");
            }
        });

        cancelButton.setOnAction(e -> displayLoginWindow(""));

        buttonBar.getButtons().addAll(OKButton, cancelButton);

        GridPane.setConstraints(loginLabel, 0, 0);
        GridPane.setConstraints(loginTextField, 1, 0);
        GridPane.setConstraints(domainLabel, 0, 1);
        GridPane.setConstraints(domainTextField, 1, 1);
        GridPane.setConstraints(passwordLabel, 0, 2);
        GridPane.setConstraints(passwordTextField, 1, 2);
        GridPane.setConstraints(passwordAgainLabel, 0, 3);
        GridPane.setConstraints(passwordAgainTextField, 1, 3);
        GridPane.setConstraints(buttonBar, 1, 4);

        grid.getChildren().addAll(loginLabel, loginTextField, domainLabel, domainTextField,passwordLabel, passwordTextField, passwordAgainLabel, passwordAgainTextField, buttonBar);

        grid.setAlignment(Pos.CENTER);

        final String gridStyle = "-fx-hgap: 10; -fx-vgap: 10;";
        grid.setStyle(gridStyle);
        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-text-fill: red;");

        mainVBox.getChildren().addAll(grid, errorLabel);
        errorLabel.setAlignment(Pos.CENTER);
        mainVBox.setAlignment(Pos.CENTER);
        scene = new Scene(mainVBox, 400, 300);

        window.setScene(scene);
        window.show();
    }

    public void displayChangePasswordWindow(String errorMessage){
        VBox mainVBox = new VBox(10);
        GridPane grid = new GridPane();
        PasswordField passwordTextField = new PasswordField();
        PasswordField newPasswordTextField = new PasswordField();
        PasswordField newPasswordAgainTextField = new PasswordField();
        Label passwordLabel = new Label("Old password");
        Label newPasswordLabel = new Label("New password");
        Label newPasswordAgainLabel = new Label("New password again");
        Hyperlink logoutLink = new Hyperlink("Log Out");
        ButtonBar buttonBar = new ButtonBar();
        Button OKButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        buttonBar.getButtons().addAll(OKButton, cancelButton);
        GridPane.setConstraints(passwordLabel, 0, 0);
        GridPane.setConstraints(newPasswordLabel, 0, 1);
        GridPane.setConstraints(newPasswordAgainLabel, 0, 2);
        GridPane.setConstraints(logoutLink, 0, 3);
        GridPane.setConstraints(passwordTextField, 1, 0);
        GridPane.setConstraints(newPasswordTextField, 1, 1);
        GridPane.setConstraints(newPasswordAgainTextField, 1, 2);
        GridPane.setConstraints(buttonBar, 1, 3);

        OKButton.setOnAction(e->{
            String password = passwordTextField.getText().toString();
            String newPassword = newPasswordTextField.getText().toString();
            String newPasswordAgain = newPasswordAgainTextField.getText().toString();

            boolean bOK = true;
            if (!current_password.equals(password) || newPassword.isEmpty() || !newPassword.equals(newPasswordAgain))
                bOK = false;
            if (!isPasswordValid(newPassword)) {
                displayChangePasswordWindow("Password does not meet security requirements");
                return;
            }


            if (bOK) {
                Statement statement;
                try {
                    Class.forName(sDriverName);
                    Connection conn = DriverManager.getConnection(sDbUrl);
                    statement = conn.createStatement();

                    String sMakeUpdate = "UPDATE Users SET password = '" + newPassword + "' WHERE UPPER(LOGIN) LIKE UPPER('" + current_username + "') AND UPPER(DOMAIN) LIKE UPPER('" + current_domain + "')";
                    statement.executeUpdate(sMakeUpdate);
                    displayMainWindow("Password has been changed!");
                    current_password = newPassword;
                } catch (Exception exc) {
                    displayLoginWindow("Password might not be changed!");
                }
            }
            else{
                displayChangePasswordWindow("Invalid credentials");
            }
        });

        logoutLink.setOnAction(e->{
            current_password = "";
            current_domain = "";
            current_username = "";
            displayLoginWindow("");
        });

        cancelButton.setOnAction(e->displayMainWindow(""));

        grid.setAlignment(Pos.CENTER);

        grid.getChildren().addAll(passwordLabel, passwordTextField, newPasswordLabel, newPasswordTextField, newPasswordAgainLabel, newPasswordAgainTextField, logoutLink, buttonBar);

        final String gridStyle = "-fx-hgap: 10; -fx-vgap: 10;";
        grid.setStyle(gridStyle);
        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-text-fill: red;");

        mainVBox.getChildren().addAll(grid, errorLabel);
        errorLabel.setAlignment(Pos.CENTER);
        mainVBox.setAlignment(Pos.CENTER);
        scene = new Scene(mainVBox, 400, 300);

        window.setScene(scene);
        window.show();

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("Java SSO Application");

        displayLoginWindow("");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
