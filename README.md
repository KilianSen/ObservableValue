# ObservableValue

This directory contains a standalone implementation of the Observer pattern, tailored for UI development and other scenarios where you need to react to changes in data. It provides a set of classes to create observable values and collections.

## Core Concepts

The primary goal of this implementation is to provide a simple, yet powerful way to create data models that can be observed for changes. When the value of an `ObservableValue` changes, all registered listeners are notified.

This is particularly useful in GUI applications to automatically update the view when the underlying data model changes, without having to write boilerplate code for UI updates.

## Files

### `IObservableValue<T>`

This is the main interface for all observable values. It defines the core methods for setting and getting the value, and for managing listeners.

- `set(T value)`: Sets a new value. Listeners are only notified if the new value is different from the old one.
- `get()`: Returns the current value.
- `onChange(Consumer<T> listener)`: Registers a listener that will be called when the value changes. It returns an `AutoCloseable` which can be used to unregister the listener.

### `BaseObservableValue<T>`

This is an abstract base class that provides the fundamental implementation of `IObservableValue`. It manages a list of listeners and handles the notification logic, including multi-threading. Listeners can be configured to run on a separate thread or on the same thread that triggered the change.

### `ObservableValue<T>`

This is the standard, concrete implementation of an observable value. It extends `BaseObservableValue` and implements `IOVDiffable`, which allows listeners to receive both the old and the new value upon a change.

### `ObservableList<E>`

An observable list that wraps a standard `java.util.List`. It implements the `List<E>` interface, so it can be used as a drop-in replacement for a regular `List`. Any modification to the list (add, remove, etc.) will trigger a notification to the listeners.

### `ObservableMap<K, V>`

An observable map that wraps a standard `java.util.Map`. It implements the `Map<K, V>` interface and behaves similarly to `ObservableList`. Any change to the map will notify the listeners.

## Usage Example

Here is a simple example of how to use `ObservableValue`:

```java
// Create an observable value with an initial value
ObservableValue<String> name = new ObservableValue<>("John");

// Add a listener to observe changes
AutoCloseable subscription = name.onChange(newName -> {
    System.out.println("Name changed to: " + newName);
});

// Change the value, which will trigger the listener
name.set("Jane"); // Output: Name changed to: Jane

// The value is the same, the listener will not be triggered
name.set("Jane"); // No output

// Unregister the listener
try {
    subscription.close();
} catch (Exception e) {
    e.printStackTrace();
}

// This change will not be observed
name.set("John"); // No output
```

## `ObservableList` Usage

`ObservableList` is useful when you need to observe changes to a list of items, for example, in a UI list view.

```java
// Create an observable list
ObservableList<String> fruits = new ObservableList<>();

// Add a listener
fruits.onChange(list -> {
    System.out.println("Fruits list changed: " + list);
});

// Modify the list
fruits.add("Apple"); // Output: Fruits list changed: [Apple]
fruits.add("Banana"); // Output: Fruits list changed: [Apple, Banana]
fruits.remove("Apple"); // Output: Fruits list changed: [Banana]
```

## `ObservableMap` Usage

`ObservableMap` is ideal for scenarios where you need to track a collection of key-value pairs.

```java
// Create an observable map
ObservableMap<String, Integer> scores = new ObservableMap<>();

// Add a listener
scores.onChange(map -> {
    System.out.println("Scores changed: " + map);
});

// Modify the map
scores.put("Alice", 10); // Output: Scores changed: {Alice=10}
scores.put("Bob", 20); // Output: Scores changed: {Alice=10, Bob=20}
scores.put("Alice", 15); // Output: Scores changed: {Alice=15, Bob=20}
```

## Threading

By default, listeners are notified on a separate thread from a dedicated thread pool. This is done to avoid blocking the thread that triggers the change, which is often the main UI thread.

You can, however, specify that a listener should be executed on the same thread that called the `set()` method by passing `true` as the second argument to `onChange()`:

```java
myObservable.onChange(newValue -> {
    // This will run on a background thread
}, false); // false is the default

myObservable.onChange(newValue -> {
    // This will run on the same thread that called set()
}, true);
```

This is important when the listener needs to perform UI updates, which in many frameworks must be done on the main UI thread. If the `set()` is called from the main thread, using `useSameThread = true` ensures the listener also runs on the main thread.
