package command;

import utility.Receiver;

public class Add extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.add();
    }

}
