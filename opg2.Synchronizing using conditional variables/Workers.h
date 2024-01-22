//
// Created by krol3 on 1/25/2023.
//


#include <iostream>
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
    condition_variable cv,lc;
    bool dont_stop = true;

    Workers(int x){
        number_workers=x;
    }

    /**
     * Here we have consumer - productor relation ship
     */
    void start(){
        for (int i = 0; i < number_workers; i++) {
            thread_workers.emplace_back([this] {
                while (dont_stop) {
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(tasks_lock);
                    //    cout<<"Lock releases"<<endl;

                        cv.wait(lock,
                                [this]() { return !tasks.empty() || !dont_stop;}); //spurious wake (if tasks is not empty lock unblocks)
                      //  cout<<"Lock acquired"<<endl;

                        if(!tasks.empty()){
                            task = *tasks.begin(); // Copy task for later use
                            tasks.pop_front(); // Remove task from list
                            }
                    }
                    if (task) {
                        task();// Run task outside of mutex lock
                        cv.notify_one();
                      //      this_thread::sleep_for(1s);
                        }

                }lc.notify_one();
            });
        }

    }

    void post(function<void()> func){
        {
            unique_lock<mutex> lock(tasks_lock);
            tasks.emplace_back(func);
        }
        cv.notify_all();
    }
    void join_all(){
        stop();
        {
            unique_lock<mutex> lock(tasks_lock);
            lc.wait(lock);
        }

        for (auto &thread : thread_workers) {
            if (thread.joinable()) {
                thread.join();
            }
        }
        cout << "flag" << " ";

    }
    void stop(){
        {
            unique_lock<mutex> lock(tasks_lock);
            dont_stop= false;
        }

        cv.notify_all();
    }

}

;