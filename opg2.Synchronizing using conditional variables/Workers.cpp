//
// Created by krol3 on 1/25/2023.
//

#include <list>
#include <vector>
#include <functional>
#include <thread>
# include <mutex>
# include <condition_variable>


using namespace std;

class Workers {
public:
    int number_workers;
    vector<thread> thread_workers;

    list<function<void()>> tasks;
    mutex tasks_lock;
    condition_variable cv;
    bool stop = false;

    Workers(int x){
        number_workers=x;
    }
    void start(){
        for (int i = 0; i < number_workers; i++) {
            thread_workers.emplace_back([this] {
                while (true) {
                    function<void()> task;{
                        unique_lock<mutex> lock(tasks_lock);

                        // TODO: use conditional variable

                        while(stop)
                            cv.wait(lock);

                        //
                        if (!tasks.empty()) {
                            task = *tasks.begin(); // Copy task for later use
                            tasks.pop_front(); // Remove task from list
                        }
                    }
                    if (task)
                        task();// Run task outside of mutex lock
                    else break;
                }
            });
        }

    }

    void post(function<void()> func){
        unique_lock<mutex> lock(tasks_lock);
        tasks.emplace_back(func);
    }

}

;
