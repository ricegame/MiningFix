package ricedotwho.mf;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import ricedotwho.mf.utils.itemUtils;
import ricedotwho.mf.utils.tablistUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ricedotwho.mf.mf.sendMessage;
import static ricedotwho.mf.mf.sendMessageWithPrefix;


public class mfCommands extends CommandBase {
	static Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public int getRequiredPermissionLevel() {
	    return 0;
	}
	
	@Override
	public String getCommandName() {
		return "mf";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "mf <setting>";
	}
	
	@Override
	public List getCommandAliases() {
		List<String> commandAliases = new ArrayList();
		commandAliases.add("miningfix");
		return commandAliases;
	}
	
	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;

    }
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		
		switch(args.length) {
		case 0:
			mf.config.openGui();
			break;

		case 1:
			if(args[0].equalsIgnoreCase("sbId")) {
				ItemStack heldItem = mc.thePlayer.getHeldItem();
				sendMessageWithPrefix("sbID: " + itemUtils.getSkyBlockItemID(heldItem));
			}
			else {
				mf.sendMessageUnknown(args[0]);
			}
			break;

		case 2:
			if(args[0].equalsIgnoreCase("dev")) {
				if (args[1].equalsIgnoreCase("tooltip")) {
					ItemStack item = mc.thePlayer.getHeldItem();
					if (item == null) return;
					sendMessage(item.getTooltip(mc.thePlayer, false).toString());
				} else if (args[1].equalsIgnoreCase("tablist")) {
					sendMessage(tablistUtils.readTabList().toString());
				} else if (args[1].equalsIgnoreCase("inv")) {
					sendMessage(Arrays.toString(mc.thePlayer.inventory.armorInventory));
				}
			} else {
				mf.sendMessageUnknown(args[0]);
			}
			break;

		case 3:
			break;
		default:
			mf.sendMessageUnknown(args[0]);
			break;
		}

	}
}
