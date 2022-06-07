#include <QTextStream>
#include <QString>
#include <QtCore>
#include <QCoreApplication>
#include <windows.h>


#include "dbconnector.h"
#include "session.h"


#include <memory>


int main(int argc, char *argv[])
{
    QString strPath;
    wchar_t path[1024];

    ::GetModuleFileName(NULL, path, 1024);
    strPath = QString::fromWCharArray(path);
    // starting session

    std::auto_ptr<Session> session(new Session);

    // check parameters if '-a' parameter so enable AddUser function
    if (argc > 1 && !QString(argv[1]).compare("-a"))
            session->EnableAddUserFunctionality(true);

    QString database_path = strPath.mid(0, strPath.lastIndexOf('\\') + 1) + "mydatabase.sqlite";
    if (session->start(database_path))
        while(session->getCommandResult()) {/*command loop*/}

    out << "Session is closed!\n";
    return 0;
}
