#include "dbconnector.h"

DBConnector::DBConnector() {}

DBConnector::~DBConnector()
{
    disconnect();
    if (sdb)
        delete sdb;
}

bool DBConnector::connect(QString path)
{
    sdb = new QSqlDatabase();
    *sdb = QSqlDatabase::addDatabase("QSQLITE");
    sdb->setDatabaseName(path);
    if (sdb->open())
        return true;

    return false;
}

void DBConnector::disconnect()
{
    if (sdb && sdb->isOpen())
        sdb->close();
}

bool DBConnector::queryExec(QString stringQuery)
{
    QSqlQuery query;
    query.prepare(stringQuery);
    return query.exec();
}

void DBConnector::querySelect(QString query_string, QVector<QVector<QString> >& result)
{
    QSqlQuery query;

    query.prepare(query_string);
    query.exec();

    int num_of_columns_in_the_table = query.record().count();
    while(query.next())
    {
        result.push_back(QVector<QString>());
        for(int i = 0 ; i < num_of_columns_in_the_table; i++)
            result[result.size()-1].push_back(query.value(i).toString());
    }

}

bool DBConnector::isConnected()
{
    return sdb->isOpen();
}
