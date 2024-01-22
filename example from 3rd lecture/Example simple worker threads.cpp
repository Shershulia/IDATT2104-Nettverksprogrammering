# include <functional>
# include <iostream>
# include <list>
# include <mutex>
# include <thread>
# include <vector>
using namespace std;
list<function<void()>> tasks;
mutex tasks_mutex; // tasks mutex needed
void post_tasks() {
    for (int i = 0; i < 10; i++) {
        unique_lock<mutex> lock(tasks_mutex);
        tasks.emplace_back([i] {
            cout << "task " << i
                 << " runs in thread "
                 << this_thread::get_id()
                 << endl;
        });
    }
}
void run_tasks_in_worker_threads() {
    vector<thread> worker_threads;
    for (int i = 0; i < 4; i++) {
        worker_threads.emplace_back([] {
            while (true) {
                function<void()> task;
                {
                    unique_lock<mutex> lock(tasks_mutex);
// TODO: use conditional variable
                    if (!tasks.empty()) {
                        task = *tasks.begin(); // Copy task for later use
                        tasks.pop_front(); // Remove task from list
                    }
                }
                if (task)
                    task(); // Run task outside of mutex lock
            }
        });
    }
    for (auto &thread : worker_threads) {
        thread.join();
    }
}
int main() {
    post_tasks();
    run_tasks_in_worker_threads();
}
