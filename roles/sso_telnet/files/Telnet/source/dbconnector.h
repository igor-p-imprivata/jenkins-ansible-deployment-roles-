#ifndef DBCONNECTOR_H
#define DBCONNECTOR_H


#include <QtCore>
#include <QtSql/QSqlRelation>
#include <QtSql/QtSql>
#include <QtSql/QSqlDatabase>
#include <QtSql/QSqlDriver>

typedef QVector<QVector<QString> > SelectionResult;

class DBConnector
{
public:

    DBConnector(void);
    ~DBConnector(void);
    bool connect(QString);
    void disconnect(void);
    bool queryExec(QString);
    void querySelect(QString, SelectionResult &);

    bool isConnected();

private:
    QSqlDatabase *sdb;
};

#endif // DBCONNECTOR_H
