#include <iostream>

using namespace std;

int main() {
    string message = "Hello to the world of cpp!";
    cout << message << endl;
    string message2 = message.replace(22, 3, "CPP");
    cout << message2 << endl;
}