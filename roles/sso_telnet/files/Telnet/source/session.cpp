#include "session.h"

Session::Session()
{
    // preparing stuff
    EnableAddUserFunctionality(false);
    allowedSymbols = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm'
                    , 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
                    , 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'
                    , 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
                    , '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '$', '?'
                    , '_', '-', '@', '!', '.', ',', ':', ';', '{', '}', '(', ')', '%'
                    };

}

Session::~Session(void)
{
    close();
}

void Session::EnableAddUserFunctionality(bool bOK)
{
    addUserFunctionality = bOK;
}

void Session::AddUser()
{
    if (isLogged())
    {
        MainScreen("You need to log out to execute this command!");
        return;
    }

    QString login = ""
            , domain = ""
            , password = ""
            , password_again = "";

    out << "Username: ";
    in >> login;
    out << "Domain: ";
    in >> domain;
    out << "Password: ";
    password = SecureInput();
    out << "Password again: ";
    password_again = SecureInput();

    if (login.isEmpty() || domain.isEmpty() || password.isEmpty() || password.compare(password_again))
    {
        LogInScreen("Invalid credentials!");
        return;
    }
    if (ifUserExists(login, domain))
    {
        LogInScreen("This user already exists!");
        return;
    }
    if (!validatePassword(password))
    {
        LogInScreen("Your password does not meet security requirements!");
        return;
    }

    QString query = "INSERT INTO Users (LOGIN, PASSWORD, DOMAIN) VALUES ('" + login + "', '" + password + "', '" + domain.toUpper() + "')";
    if (connector.queryExec(query) && ifUserExists(login, domain, password))
        LogInScreen("User was created successfully!");
    else
        LogInScreen("User might not be created!");
}

void Session::LogIn()
{
    if (isLogged())
    {
        MainScreen("You are already logged in!");
        return;
    }

    QString login = ""
          , domain = ""
          , password = "";

    out << "Username: ";
    in >> login;
    out << "Domain: ";
    in >> domain;
    out << "Password: ";
    password = SecureInput();


    if (ifUserExists(login, domain, password))
    {
        passwordAttempts[login+domain] = 0;
        current_login = login;
        current_password = password;
        current_domain = domain;
        MainScreen("You have logged in successfully!");
    }
    else
    {
        if (++passwordAttempts[login+domain] > 2)
        {
            out << "You have tried to use invalid credentials 3 times. Program will be closed.\n";
            exit(0);
        }
        LogInScreen("Invalid credentials!");
    }

}

void Session::LogOut()
{
    if (isLogged())
    {
        current_login = "";
        current_password = "";
        current_domain = "";
        LogInScreen("You have logged out successfully!");
    }
    else
    {
        LogInScreen("You need to log in at fisrt!\n");
    }
}

void Session::ChangePassword()
{
    if (!isLogged())
    {
        LogInScreen("You are not logged in!");
        return;
    }

    QString password = ""
          , new_password = ""
          , new_password_again = "";


    out << "Old password: ";
    password = SecureInput();
    out << "New password: ";
    new_password = SecureInput();
    out << "New password again: ";
    new_password_again = SecureInput();

    if (password.compare(current_password) || new_password.isEmpty() || new_password.compare(new_password_again))
    {
        MainScreen("Invalid credentials!");
        return;
    }

    if (!validatePassword(new_password))
    {
        MainScreen("Your password does not meet security requirements!");
        return;
    }

    QString query = "UPDATE Users SET password = '" + new_password + "' WHERE UPPER(LOGIN) LIKE UPPER('" + current_login + "') AND UPPER(DOMAIN) LIKE UPPER('" + current_domain + "')";
    if (connector.queryExec(query) && ifUserExists(current_login, current_domain, new_password))
    {
        current_password = new_password;
        MainScreen("Password has been changed!");
    }
    else
    {
        MainScreen("Password might not be changed!");
    }
}

void Session::LogInScreen(QString error_message)
{
    if (!error_message.isEmpty())
    {
        out << error_message << endl;
        error_message = "";
    }
    if (!addUserFunctionality)
    {
        LogIn();
        return;
    }
    out << "Commands: A - add user, I - Log in, E - Exit\n";
}

void Session::MainScreen(QString error_message)
{

    if (!error_message.isEmpty())
    {
        out << error_message << endl;
        error_message = "";
    }
    out << "Commands: C - Change password, O - Log out, E - Exit\n";

}

bool Session::start(QString& input_database_path)
{
    out << "Welcome to SSO Telnet Application!\n";
    if (connector.connect(input_database_path))
    {
        LogInScreen();
        return true;
    }
    else
    {
        out << "Fatal error! Cannot connect to DB!\n";
        return false;
    }
}

void Session::close()
{
    if(connector.isConnected())
        connector.disconnect();
}

bool Session::validatePassword(const QString& password)
{
    if (password.length() < 6 || password.length() > 32)
        return false;

    // check every sybmol from this password
    for (auto elem : password)
    {
        auto it = std::find(allowedSymbols.begin(), allowedSymbols.end(), elem);
        if (it == allowedSymbols.end())
            return false;
    }
    return true;
}

QString Session::SecureInput()
{
    HANDLE hStdin = GetStdHandle(STD_INPUT_HANDLE);
    DWORD mode;
    GetConsoleMode(hStdin, &mode);
    SetConsoleMode(hStdin,mode & ~ENABLE_ECHO_INPUT);

    QString input;
    in >> input;
    out << "\n";

    SetConsoleMode(hStdin, mode | ENABLE_ECHO_INPUT);

    return std::move(input);
}

bool Session::isLogged()
{
    return current_login != ""; // returns true if username is't empty
}

bool Session::ifUserExists(const QString& login, const QString& domain, QString password)
{
    QString query = "SELECT * FROM Users WHERE UPPER(LOGIN) LIKE UPPER('" + login + "') AND UPPER(DOMAIN) LIKE UPPER('" + domain + "')";

    if (!password.isEmpty())
        query += " AND PASSWORD = '" + password + "'";

    SelectionResult result;
    connector.querySelect(query, result);
    if (findUserInResult(result, login, domain))
        return true;

    return false;
}

bool Session::findUserInResult( SelectionResult& result, const QString& login, const QString& domain, QString password)
{
    for (auto row : result)
    {
        if (!row[0].toUpper().compare(login.toUpper()))
            if (!row[2].toUpper().compare(domain.toUpper()))
                if (!row[1].compare(password) || password.isEmpty())
                    return true; // user is found
    }

    return false; // user was not found in array
}

bool Session::getCommandResult()
{
    out << "Command: ";
    QString input;
    in >> input;
    input = input.toUpper();

    if (!input.compare("C"))
        ChangePassword();
    else if (!input.compare("I"))
        LogIn();
    else if (!input.compare("O"))
        LogOut();
    else if (!input.compare("A"))
        AddUser();
    else if (!input.compare("E"))
        return false; // exiting
    else
    {
        out << "You need to enter a valid command!\n";
    }
        return true; // continue getting commands by returnin true...
}
