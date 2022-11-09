# Dispatch Patterns

## Dispatch DSL

### Action Name

Action name is a unique name to specify action

```
    <domain>://<action_name>

```
or
```
    class://<action_class_name>

```

### Action Property Assignment
Action Parameter Assignment at runtime is expressed as a function like expression:
```
    property_name=<evaluation-function>(expression)

```
here the <evaluation-function>(expression) function is one of val(expression), res(expression), param(expression) or ref(expression).

- val(expression): assign directly from a string value;
- res(expression): assign from resource, expression should be in uri format such as "classpath://contents/index.html"
- param(expression): assign from parameter at runtime from method parameter, bean property, etc.
- ref(expression): assign from execution context (ActionDispatchSession), for action orchestration patterns such as pipeline, workflow, eventbus...

## Basic Dispatch Pattern

### Action Dispatch Pattern

### Command Dispatch Pattern

### Method Dispatch Pattern


## Pipeline

## EventBus Dispatch Pattern

###


## Action Proxy Pattern