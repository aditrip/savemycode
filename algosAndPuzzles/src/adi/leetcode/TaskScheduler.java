package adi.leetcode;

import java.util.Arrays;
import java.util.PriorityQueue;

/*
 * https://leetcode.com/problems/task-scheduler/
 * 
 * Given a characters array tasks, representing the tasks a CPU needs to do, where each letter represents a different task. Tasks could be done in any order. Each task is done in one unit of time. For each unit of time, the CPU could complete either one task or just be idle.

However, there is a non-negative integer n that represents the cooldown period between two same tasks (the same letter in the array), that is that there must be at least n units of time between any two same tasks.

Return the least number of units of times that the CPU will take to finish all the given tasks.

 

Example 1:

Input: tasks = ["A","A","A","B","B","B"], n = 2
Output: 8
Explanation: 
A -> B -> idle -> A -> B -> idle -> A -> B
There is at least 2 units of time between any two same tasks.
Example 2:

Input: tasks = ["A","A","A","B","B","B"], n = 0
Output: 6
Explanation: On this case any permutation of size 6 would work since n = 0.
["A","A","A","B","B","B"]
["A","B","A","B","A","B"]
["B","B","B","A","A","A"]
...
And so on.
Example 3:

Input: tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
Output: 16
Explanation: 
One possible solution is
A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> idle -> idle -> A -> idle -> idle -> A
 

Constraints:

1 <= task.length <= 104
tasks[i] is upper-case English letter.
The integer n is in the range [0, 100].
 */
public class TaskScheduler {

    public static void main(String[] args) {
        TaskScheduler obj = new TaskScheduler();
        char[][] tasks = { { 'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E',
                'F', 'G', 'B', 'B', 'B', 'B', 'B', 'B', 'G', 'G', 'G', 'G',
                'G', 'G', 'G', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'G', 'G', 'G',
                'G', 'G', 'G', 'G', 'G', 'K', 'K', 'K', 'K', 'K', 'K', 'K',
                'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K',
                'K', 'K',

                },
                { 'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G' },
                { 'A', 'A', 'A', 'B', 'B', 'B' },
                { 'A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'C', 'D', 'D', 'E' },
                {
                 'A','A','B','B','C','C','D','D','E','E','F','F','G','G','H',
                 'H','I','I','J','J','K','K','L','L','M','M','N','N','O','O',
                 'P','P','Q','Q','R','R','S','S','T','T','U','U','V','V',
                 'W','W','X','X','Y','Y','Z','Z'
                            
                }

        };

        int[] N = { 2, 2, 2, 2, 2 };

        int[] outputs = { 61, 16, 8, 12 };

        char[] tasks2 = { 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K',
                'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K',
                'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K',
                'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K',

        };
        int n = 2;
        for (int i = 0; i < tasks.length; i++) {
            char[] task = tasks[i];
            n = N[i];
            System.out.println(obj.leastInterval(task, n));
        }

    }

    public int leastInterval(char[] tasks, int n) {
        int[] taskFreq = new int[26];

        for (char t : tasks) {
            taskFreq[t - 'A']++;
        }

        Arrays.sort(taskFreq);

        int counter = 0;
        int emptySlots = 0;
        int i = 25;
        int freq = 0;
        int prevFreq = 0;
        while (i>= 0 && taskFreq[i] > 1) {
            freq = taskFreq[i];
            if (freq == prevFreq) {
                counter++;
                emptySlots++;
            }
            if (emptySlots >= freq) {
                emptySlots -= freq;
                prevFreq = freq;
                i--;
                continue;
            } else if (emptySlots > 0) {
                freq -= emptySlots;
                emptySlots = 0;
            }

            if (freq == 1) {
                counter++;
                i--;
                continue;
            } else {
                if (prevFreq == 0) {
                    counter += ((freq - 1) * (n + 1)) + 1;
                    emptySlots = (freq - 1) * n;
                } else {
                    counter += freq;
                }
            }

            prevFreq = freq;
            i--;
        }
        while (i>= 0 && taskFreq[i] > 0) {
            if (emptySlots > 0) {
                emptySlots--;
            } else {
                counter++;
            }
            i--;
        }

        if (emptySlots > 0) {
            /* Can these slots be filled */

        }
        return counter;

    }

    public void qsort(int s, int e, int[] data, int[] index) {
        if (s >= e) {
            return;
        }
        int pivot = s;
        while (pivot < e && data[pivot] == 0) {
            pivot++;
        }
        if (pivot == e) {
            if (data[pivot] == 0) {
                return;
            }
        }
        int i = s;
        int j = e;

        while (i < j) {
            while (i < e && data[i] >= data[pivot]) {
                i++;
            }
            while (j > s && data[j] < data[pivot]) {
                j--;
            }
            if (i < j) {
                swap(i, j, data, index);
            }

        }
        swap(pivot, j, data, index);

        qsort(s, j - 1, data, index);
        qsort(j + 1, e, data, index);

    }

    public void swap(int i, int j, int[] data, int[] index) {
        int idxTemp = index[i];
        index[i] = index[j];
        index[j] = idxTemp;
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    static class Task implements Comparable<Task> {

        public int priority;
        public int taskId;

        public Task(int taskId, int priority) {
            this.taskId = taskId;
            this.priority = priority;
        }

        @Override
        public int compareTo(Task o) {
            if (priority > o.priority) {
                return 1;
            } else if (priority < o.priority) {
                return -1;
            } else {
                return 0;
            }

        }

        @Override
        public String toString() {
            return "taskId:" + (char) (taskId + 'A') + " pri:" + priority;
        }

    }

}
