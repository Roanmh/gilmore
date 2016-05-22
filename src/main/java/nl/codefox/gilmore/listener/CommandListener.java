package nl.codefox.gilmore.listener;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import nl.codefox.gilmore.Gilmore;
import nl.codefox.gilmore.command.GilmoreCommand;
import nl.codefox.gilmore.command.CustomCommand;

public class CommandListener extends ListenerAdapter 
{

    private List<GilmoreCommand> commands = new ArrayList<>();
    
    public CommandListener registerCommand(GilmoreCommand command)
    {
        commands.add(command);
        return this;
    }

    public List<GilmoreCommand> getCommands()
    {
        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) 
    {
        if(event.getAuthor() != Gilmore.getJDA().getSelfInfo())
        {
            String[] args = event.getMessage().getRawContent().split(" ");
            
            GilmoreCommand command = null;
            for(GilmoreCommand c : commands)
            {
                if(c.getAliases().contains(args[0]))
                {
                    command = c;
                    break;
                }
            }

            if(command != null)
            {
                command.process(args, event);
            }
            else if(CustomCommand.commandExists(args[0]))
            {
                String commandDesc = CustomCommand.getCommand(args[0]);
                event.getChannel().sendMessage(String.format("[%s] %s", event.getAuthor().getAsMention(), commandDesc));
            }
        }
    }
}
