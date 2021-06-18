package command;

import utility.Receiver;

public class GroupCountingByPosition extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.groupCountingByPosition();
    }
}
