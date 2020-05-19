# optaplanner-step-by-step

## Job Scheduling
The goal is to schedule tasks.
* Step 1: tasks have duration, due date, and a penalty to be applied for each day late. Only one task at a time.
* Step 2: now tasks may depend on other tasks. When it has dependencies, a task must start only when every dependencies are done. Workers can work on several tasks in parallel, but only when they have required capabilities. The global duration of the schedule is minimized.
* Step 3: workers have days off (holidays). Some tasks are locked in the schedule (can't be moved).
