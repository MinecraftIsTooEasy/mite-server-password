package net.wensc.mitemod.serverpassword.trans.entity;

import net.minecraft.*;
import net.wensc.mitemod.serverpassword.enums.PasswordStatus;
import net.wensc.mitemod.serverpassword.util.LogWriter;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends EntityPlayer implements ICrafting {
    public String password = "";

    public PasswordStatus passwordStatus = PasswordStatus.NOTSETPWD;

    public int passwordTimer = 0;

    public MixinServerPlayer(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Inject(method = "travelToDimension", at = @At("HEAD"), cancellable = true)
    public void preventTravelToDimension(int par1, CallbackInfo callbackInfo) {
        if(this.passwordStatus != PasswordStatus.SAMEPWD) {
            callbackInfo.cancel();
        } else {
            LogWriter.writeLog(ReflectHelper.dyCast(ServerPlayer.class, this), "正在跨世界传送," + "坐标：" + LogWriter.getDimName(this.worldObj.getDimensionId()) + "("+(int)this.posX+","+(int)this.posY+","+(int)this.posZ+")" + ",目标世界：" + LogWriter.getDimName(par1));
        }
    }

    @Inject(method = "setPositionAndUpdate", at = @At("HEAD"), cancellable = true)
    public void preventPositionUpdate(double par1, double par3, double par5, CallbackInfo callbackInfo){
        if(this.passwordStatus != PasswordStatus.SAMEPWD) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    public void attackEntityFrom(Damage damage, CallbackInfoReturnable<EntityDamageResult> callbackInfoReturnable) {
        if(this.passwordStatus != PasswordStatus.SAMEPWD) {
            callbackInfoReturnable.setReturnValue(null);
            callbackInfoReturnable.cancel();
        }
    }


    @Inject(method = "clonePlayer", at = @At("RETURN"))
    public void injectClonePlayer(EntityPlayer par1EntityPlayer, boolean par2, CallbackInfo callbackInfo) {
        this.password = par1EntityPlayer.getAsEntityPlayerMP().password;
        this.passwordStatus = par1EntityPlayer.getAsEntityPlayerMP().passwordStatus;
        this.passwordTimer = par1EntityPlayer.getAsEntityPlayerMP().passwordTimer;
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void injectBeforeUpdate(CallbackInfo callbackInfo) {
        if(this.passwordStatus == PasswordStatus.IPDIFFERENT) {
            this.passwordTimer = 400;
            this.passwordStatus = PasswordStatus.IPDIFFWAIT;
        }
        if(this.passwordTimer > 0 && this.passwordStatus == PasswordStatus.IPDIFFWAIT) {
            this.passwordTimer--;
            if(this.passwordTimer <= 0) {
                (ReflectHelper.dyCast(ServerPlayer.class, this)).playerNetServerHandler.kickPlayer("输入密码超时");
            }
        }
        if(this.passwordStatus != PasswordStatus.SAMEPWD) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    public void injectReadNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo callbackInfo) {
        if (par1NBTTagCompound.hasKey("password")) {
            this.password = par1NBTTagCompound.getString("password");
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    public void injectWriteNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo callbackInfo) {
        par1NBTTagCompound.setString("password", this.password);
    }


    @Shadow
    public INetworkManager getNetManager() {
        return null;
    }

    @Shadow
    public void sendChatToPlayer(ChatMessage chatMessage) {

    }

    @Shadow
    public boolean canCommandSenderUseCommand(int i, String s) {
        return false;
    }

    @Shadow
    public ChunkCoordinates getCommandSenderPosition() {
        return null;
    }

    @Shadow
    public void updateCraftingInventory(Container container, List list) {

    }

    @Shadow
    public void sendSlotContents(Container container, int i, ItemStack itemStack) {

    }

    @Shadow
    public void sendProgressBarUpdate(Container container, int i, int i1) {

    }
}
