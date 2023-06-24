package net.wensc.mitemod.serverpassword.trans;

import net.minecraft.*;
import net.wensc.mitemod.serverpassword.enums.PasswordStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinServerConfigurationManager {
    @Inject(method = "playerLoggedIn", at = @At("HEAD"))
    public void playerLoggedIn(ServerPlayer par1EntityPlayerMP, CallbackInfo callbackInfo) {
        if(par1EntityPlayerMP.password.equals("")) {
            par1EntityPlayerMP.sendChatToPlayer(ChatMessage.createFromTranslationKey("请设置初始密码：/setpwd 密码").setColor(EnumChatFormat.DARK_PURPLE));
        } else {
            par1EntityPlayerMP.sendChatToPlayer(ChatMessage.createFromTranslationKey("此账号已注册，请20S内输入密码登录：/pwd 密码").setColor(EnumChatFormat.DARK_PURPLE));
            par1EntityPlayerMP.passwordStatus = PasswordStatus.IPDIFFERENT;
        }
    }
}
