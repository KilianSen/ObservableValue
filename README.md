# ObservableValue

A modern, thread-safe Java library for implementing the Observer pattern on common data structures. Create reactive, event-driven applications with fine-grained control over multithreading.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## Overview

ObservableValue provides a set of classes that allow other objects to observe changes to their contents. It is designed from the ground up for multithreaded environments, using thread-safe collections and providing developers with precise control over which thread the change listeners are executed on.

### Features

*   **`ObservableValue<T>`**: Wraps a single object. Get notified when the value is replaced.
*   **`ObservableList<T>`**: A list that fires events when elements are added, removed, or updated.
*   **`ObservableMap<K, V>`**: A map that fires events when entries are put, removed, or the map is cleared.
*   **Thread-Safe**: Uses `AtomicReference`, `CopyOnWriteArrayList`, and `ConcurrentHashMap` internally to ensure safe access across multiple threads.
*   **Flexible Threading**: Choose to run your listeners on the same thread that triggered the change or on a dedicated background thread for each listener. This is perfect for offloading work or updating UIs safely.
*   **Detailed Change Events**: Listeners receive a detailed change definition object containing the old and new values, the index or key of the change, and the type of change that occurred.

## Installation

To use ObservableValue in your Maven project, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>de.kiliansen.lib.ObservableValue</groupId>
    <artifactId>ObservableValue</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### `ObservableValue<T>`

Use `ObservableValue` to monitor changes to a single value, like a setting, a status, or a user's selection.

```java
import de.kiliansen.lib.ObservableValue.value.ObservableValue;

// Create an observable value with an initial string
ObservableValue<String> currentStatus = new ObservableValue<>("Offline");

// Add a listener to react to changes.
// This listener will run on a separate thread.
currentStatus.onChange((oldValue, newValue) -> {
    System.out.println("Status changed from '" + oldValue + "' to '" + newValue + "' on thread: " + Thread.currentThread().getName());
}, false);

System.out.println("Setting status on thread: " + Thread.currentThread().getName());
// Change the value, which will trigger the listener
currentStatus.set("Online");

// Output:
// Setting status on thread: main
// Status changed from 'Offline' to 'Online' on thread: ObservableValue-Thread-1
```

### `ObservableList<T>`

Use `ObservableList` to monitor additions, removals, and updates to a list of items.

```java
import de.kiliansen.lib.ObservableValue.list.ObservableList;
import de.kiliansen.lib.ObservableValue.list.ObservableListChangeDef;

// Create an observable list
ObservableList<String> userList = new ObservableList<>();

// Add a listener to log all changes.
// This listener will run on the same thread that modifies the list.
userList.onChange(change -> {
    switch (change.type()) {
        case ADD:
            System.out.println("User added: " + change.newValue() + " at index " + change.index());
            break;
        case REMOVE:
            System.out.println("User removed: " + change.oldValue() + " from index " + change.index());
            break;
        case UPDATE:
            System.out.println("User at index " + change.index() + " changed from " + change.oldValue() + " to " + change.newValue());
            break;
        case CLEAR:
            System.out.println("User list cleared.");
            break;
    }
}, true);

// Add items to the list
userList.add("Alice");
userList.add("Bob");

// Remove an item
userList.remove("Alice");

// Update an item
userList.set(0, "Robert");

// Output:
// User added: Alice at index 0
// User added: Bob at index 1
// User removed: Alice from index 1
// User at index 0 changed from Bob to Robert
```

### `ObservableMap<K, V>`

Use `ObservableMap` to monitor changes to a key-value store.

```java
import de.kiliansen.lib.ObservableValue.map.ObservableMap;

// Create an observable map
ObservableMap<String, String> userPreferences = new ObservableMap<>();

// Add a listener for changes
userPreferences.onChange(change -> {
    switch (change.type()) {
        case PUT:
            System.out.println("Preference '" + change.key() + "' set to '" + change.newValue() + "'. Old value was '" + change.oldValue() + "'.");
            break;
        case REMOVE:
            System.out.println("Preference '" + change.key() + "' removed. Last value was '" + change.oldValue() + "'.");
            break;
        case CLEAR:
            System.out.println("All preferences cleared.");
            break;
    }
}, true);

// Modify the map
userPreferences.put("theme", "dark");
userPreferences.put("language", "en");
userPreferences.put("theme", "light"); // Update existing key
userPreferences.remove("language");

// Output:
// Preference 'theme' set to 'dark'. Old value was 'null'.
// Preference 'language' set to 'en'. Old value was 'null'.
// Preference 'theme' set to 'light'. Old value was 'dark'.
// Preference 'language' removed. Last value was 'en'.
```

## Threading Model

A key feature of this library is the ability to control listener execution threads via the `useSameThread` parameter in the `onChange` methods.

*   `useSameThread = true`: The listener is executed synchronously on the same thread that caused the value to change. This is useful for simple, fast operations where you need immediate consistency.

*   `useSameThread = false`: The listener is executed asynchronously on a dedicated background thread. The library manages a thread pool to handle these listeners. This is ideal for long-running tasks, I/O operations, or UI updates in frameworks that require changes to be made on a specific thread (by using the listener to delegate back to a UI thread).

## Building from Source

To build the project from the source code, you need to have Java and Maven installed.

1.  Clone the repository:
    ```sh
    git clone <repository-url>
    ```
2.  Navigate to the project directory:
    ```sh
    cd ObservableValue
    ```
3.  Build the project using Maven:
    ```sh
    mvn clean install
    ```
This will compile the source code, run tests, and install the artifact into your local Maven repository.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
