#include <iostream>
#include <thread>
#include <mutex>
#include <list>
#include <vector>
using namespace std;

bool checkPrimaryNumber(int number){
    if (number%2==0) return false;
    for(int i = 3;i<number;i+=2){
        if (number%i==0) return false;
    }return true;

}


list<int> multipleThreads(int fromNumber, int toNumber, int numberThreads){
    list<int> result;
    vector<thread> threads;
    mutex lock;

    for (int i = 0; i < numberThreads; i++) {
        threads.emplace_back([&lock,&fromNumber,&toNumber,&result]{
            for(int j = fromNumber;j<=toNumber;j++){
                lock.lock();
                if (checkPrimaryNumber(j)) result.push_back(j);
                lock.unlock();
                fromNumber=j;
            }
        });
    }

    for(auto &thread:threads){
        thread.join();
    }
    result.sort();
    return result;

}
list<int> multipleThreadsSameResources(int fromNumber, int toNumber, int numberThreads){
    list<int> result;
    thread myThreads[numberThreads];
    vector<thread> threads;
    mutex lock;
    float x = ((toNumber-fromNumber)/numberThreads);
    for (int i = 0; i < numberThreads; i++) {
        if (i == numberThreads - 1) {
            myThreads[numberThreads - 1] = thread([&fromNumber, &x, &toNumber, &result, &lock, &numberThreads] {
                for (int j = fromNumber + (numberThreads - 1) * x; j <= toNumber; j++) {
                    lock.lock();
                    if (checkPrimaryNumber(j)) result.push_back(j);
                    lock.unlock();
                }
            });
            myThreads[numberThreads - 1].join();
        } else {
            myThreads[i] = thread([&fromNumber, &i, &x, &toNumber, &result, &lock] {
                for (int j = fromNumber + i * x; j < fromNumber + i * x + x && j <= toNumber; j++) {
                    lock.lock();
                    if (checkPrimaryNumber(j)) result.push_back(j);
                    lock.unlock();
                }
            });
            myThreads[i].join();
        }
    }
    result.sort();
    return result;

}
void listOutput(list<int> list1){

    for(int &number:list1){
        cout << number << " ";
    }
}
int main() {

    listOutput(multipleThreads(1, 401, 5));
    cout << "!!!" << endl;

    listOutput(multipleThreadsSameResources(1, 401, 5));

    return 0;
}

