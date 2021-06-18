package utility;

import command.*;

import java.util.HashMap;


/**
 * This class contains map with commands which can be execute
 */
public class Invoker {
    private final HashMap<String, CommandInterface> commands;

    public Invoker() {
        commands = new HashMap<>();
    }

    /**
     * Initialize commands map
     */
    public void initMap() {
        commands.put("info", new Info());
        commands.put("show", new Show());
        commands.put("add", new Add());
        commands.put("update", new Update());
        commands.put("remove_by_id", new RemoveById());
        commands.put("clear", new Clear());
        commands.put("add_if_max", new AddIfMax());
        commands.put("remove_greater", new RemoveGreater());
        commands.put("remove_lower", new RemoveLower());
        commands.put("group_counting_by_position", new GroupCountingByPosition());
        commands.put("count_less_than_start_date", new CountLessThanStartDate());
        commands.put("filter_greater_than_start_date", new FilterGreaterThanStartDate());
    }

    public void exe(String name, String arg) {
        if (commands.containsKey(name)) {
            commands.get(name).exe(arg);
        } else {
            System.out.println("Input is incorrect.");
        }
    }

}
