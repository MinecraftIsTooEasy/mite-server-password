package net.wensc.mitemod.serverpassword.trans.block;

import net.minecraft.*;
import net.wensc.mitemod.serverpassword.util.LogWriter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockStrongbox.class, priority = 2000)
public class MixinBlockStrongbox extends Block{
    protected MixinBlockStrongbox(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    public void onBlockAboutToBeBroken(BlockBreakInfo info) {
        EntityPlayer player = info.getResponsiblePlayer();
        if (player != null && !player.inCreativeMode() && info.world.isWorldServer() && !((TileEntityStrongbox)info.tile_entity).isOwner(player)) {
            String POS = "(" + (int)player.posX + "," + (int)player.posY + "," + (int)player.posZ + ")";
            String World = player.getWorld().getDimensionName();
            String message = "已破坏私人箱子,";
            info.world.getAsWorldServer().p().getLogAgent_().logWarning("Player " + player.getEntityName() + " tried to break personal chest of " + ((TileEntityStrongbox)info.tile_entity).owner_name);
            info.world.getAsWorldServer().p().getConfigurationManager().sendChatMsg(ChatMessage.createFromTranslationKey("[Server]:").appendComponent(ChatMessage.createFromTranslationKey(" " + player.getEntityName() + " 破坏了 " + ((TileEntityStrongbox)info.tile_entity).owner_name + " 的金属箱子→" + LogWriter.getDimName(player.getWorld().getDimensionId()) + POS).setColor(EnumChatFormat.RED)));
            message += "坐标=" + LogWriter.getDimName(player.getWorld().getDimensionId()) + POS + ",";
            message += "破坏者=" + player.getEntityName() + ",";
            message += "所有者=" + ((TileEntityStrongbox)info.tile_entity).owner_name;
            LogWriter.writeLog(player.getAsEntityPlayerMP(), message);
        }
        super.onBlockAboutToBeBroken(info);
    }
}
