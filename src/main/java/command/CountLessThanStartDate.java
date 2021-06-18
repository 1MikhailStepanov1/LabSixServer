package command;

import utility.Receiver;

public class CountLessThanStartDate extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.countLessThanStartDate(arg);
    }
}
