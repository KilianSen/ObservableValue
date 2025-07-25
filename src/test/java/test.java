import de.kiliansen.lib.ObservableValue.list.ObservableList;
import de.kiliansen.lib.ObservableValue.map.ObservableMap;
import de.kiliansen.lib.ObservableValue.value.ObservableValue;

public class test {

    public static void main(String[] args) {
        ObservableMap<Integer, String> map = new ObservableMap<>();
        ObservableList<Integer> list = new ObservableList<>();
        ObservableValue<String> value = new ObservableValue<>();
        map.onPut((k, v) -> {
            // Print the key and value when a new entry is put into the map with the current thread
            System.out.println("Put: " + k + " -> " + v + " in thread: " + Thread.currentThread().getName());
        }, false);

        list.onChangeDef(change -> {
            // Print the change type and value when the list changes
            System.out.println("List change: " + change.listChangeType() + " with value: " + change.newValue() + " in thread: " + Thread.currentThread().getName());
        }, false);

        value.onChangeDef(newValue -> {
            // Print the new value when the observable value changes
            System.out.println("Value changed to: " + newValue + " in thread: " + Thread.currentThread().getName());
        }, false);


        while (true) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put((int) (Math.random() * 100), "Value " + Math.random());
            list.add((int) (Math.random() * 100));
            value.set("Value " + Math.random());
        }
    }
}
