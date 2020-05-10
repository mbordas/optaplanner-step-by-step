# optaplanner-step-by-step

## Job Scheduling
The goal is to schedule tasks.
* Step 1: tasks have duration, due date, and a penalty to be applied for each day late. Only one task at a time.
* Step 2: now tasks may depend on other tasks. When it has dependencies, a task must start every dependencies are done. Still one task at a time.
