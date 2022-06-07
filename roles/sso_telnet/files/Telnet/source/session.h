#ifndef SESSION_H
#define SESSION_H

#include <QVector>
#include <QString>
#include <windows.h>
#include "dbconnector.h"


#define in QTextStream(stdin)
#define out QTextStream(stdout)


class Session
{
public:

    Session();
    ~Session();

    void EnableAddUserFunctionality(bool);

    bool getCommandResult();
    bool start(QString&);
    void close(void);

private:

    //validation
    bool validatePassword(const QString&);
    QVector<QChar> allowedSymbols;


    // logic:
    void AddUser(void);
    void LogIn(void);
    void LogOut(void);
    void ChangePassword(void);

    // screen printing:
    void LogInScreen(QString error_message = "");
    void MainScreen(QString error_message = "");
    void ChangePasswordScreen(QString error_message = "");
    void AddUserScreen(QString error_message = "");

    // helpers
    bool ifUserExists(const QString&, const QString&, QString password = ""); // username domain
    bool findUserInResult(SelectionResult&, const QString&, const QString&, QString password = "");
    bool isLogged(void);

    QString SecureInput();

private:
    // internal variables:
    bool addUserFunctionality;
    QMap<QString, int> passwordAttempts;

    QString current_login;
    QString current_domain;
    QString current_password;
    DBConnector connector;

};

#endif // SESSIONMANAGER_H
