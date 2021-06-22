package utility;

import command.*;

import java.util.HashMap;


/**
 * This class contains map with commands which can be execute
 */
public class Invoker {
    private final HashMap<String, CommandInterface> commands;
    private final Receiver receiver;

    public Invoker(Receiver receiver) {
        commands = new HashMap<>();
        this.receiver = receiver;
    }

    /**
     * Initialize commands map
     */
    public void initMap() {
        commands.put("info", new Info(receiver));
        commands.put("show", new Show(receiver));
        commands.put("add", new Add(receiver));
        commands.put("update", new Update(receiver));
        commands.put("remove_by_id", new RemoveById(receiver));
        commands.put("clear", new Clear(receiver));
        commands.put("add_if_max", new AddIfMax(receiver));
        commands.put("remove_greater", new RemoveGreater(receiver));
        commands.put("remove_lower", new RemoveLower(receiver));
        commands.put("group_counting_by_position", new GroupCountingByPosition(receiver));
        commands.put("count_less_than_start_date", new CountLessThanStartDate(receiver));
        commands.put("filter_greater_than_start_date", new FilterGreaterThanStartDate(receiver));
    }

    public void exe(String name, String arg) {
        if (commands.containsKey(name)) {
            commands.get(name).exe(arg);
        } else {
            System.out.println("Input is incorrect.");
        }
    }

}
