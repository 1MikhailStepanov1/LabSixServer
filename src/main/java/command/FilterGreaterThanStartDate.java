package command;

import utility.Receiver;

public class FilterGreaterThanStartDate extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.filterGreaterThanStartDate(arg);
    }
}