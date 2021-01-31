#include <windows.h>
#include <iostream>
#include <stdio.h>
#include <fstream>
#include <vector>
#include <stdexcept>
#include <string>
#include <stdlib.h>

using namespace std;

const char* compileCommand = "C:\\Users\\Stoyan\\Downloads\\processing-3.5.4\\processing-java\
                                --force --sketch=C:\\Users\\Stoyan\\Desktop\\Stoyan\\Work\\RayTracer\
                                --output=C:\\Users\\Stoyan\\Desktop\\Stoyan\\Work\\RayTracer\\out --run";

vector<string> getAllFiles(string folder)
{
    vector<string> names;
    string search_path = folder + "/*.java*";
    WIN32_FIND_DATA fd;
    HANDLE hFind = ::FindFirstFile(search_path.c_str(), &fd);

    if(hFind != INVALID_HANDLE_VALUE)
    {
        do
        {
            // read all (real) files in current folder
            // , delete '!' read other 2 default folder . and ..
            if(! (fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) )
                names.push_back(fd.cFileName);
        }
        while(::FindNextFile(hFind, &fd));

        ::FindClose(hFind);
    }

    return names;
}

string remFileExtension(string name)
{
    string out = "";
    for(int i = 0;i<name.size();i++)
    {
        if(name[i]=='.') break;
        out += name[i];
    }

    return out;
}

void convertFile(string name)
{
    ifstream fin("C:\\Users\\Stoyan\\Desktop\\Stoyan\\Work\\RayTracer\\java\\"+name);
    ofstream fout("C:\\Users\\Stoyan\\Desktop\\Stoyan\\Work\\RayTracer\\"+remFileExtension(name)+".pde");

    string line;
    while(getline(fin, line)) fout << line << '\n';
}

extern "C" FILE *_popen(const char *command, const char *mode);
extern "C" FILE *_pclose(FILE *command);

std::string executeCommand(const char* cmd)
{
    char buffer[128];
    std::string result = "";
    FILE* pipe = _popen(cmd, "r");

    if (!pipe) throw std::runtime_error("popen() failed!");
    try
    {
        while (fgets(buffer, sizeof buffer, pipe) != NULL)
        {
            result += buffer;
        }
    }
    catch (...)
    {
        _pclose(pipe);
        throw;
    }

    _pclose(pipe);
    return result;
}

int main()
{
    vector <string> files = getAllFiles("C:\\Users\\Stoyan\\Desktop\\Stoyan\\Work\\RayTracer\\java");
    for(string s: files) convertFile(s);

    executeCommand(compileCommand);
}
