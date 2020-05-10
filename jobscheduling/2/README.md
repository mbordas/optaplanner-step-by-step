# Job scheduling step 2

## Goals
* Add some task dependencies: some tasks must be done before other start


## Solving the problem

### Model

We simply adds a dependencies attribute to the Task class. It stores every Task that must be done before this one starts.


### Drools scheduleCalculatorRules.drl

A new rule "taskDependencies" id added. It finds out any couple of Allocations where one depends on the other and where dependency order is broken (dependency task ends after the dependent task starts).


