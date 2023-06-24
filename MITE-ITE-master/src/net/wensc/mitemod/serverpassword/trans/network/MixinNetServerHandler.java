package net.wensc.mitemod.serverpassword.trans.network;

import net.minecraft.*;
import net.wensc.mitemod.serverpassword.enums.PasswordStatus;
import net.wensc.mitemod.serverpassword.util.LogWriter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerConnection.class)
public class MixinNetServerHandler {

    @Shadow
    public ServerPlayer playerEntity;

    @Inject(method = "handleSimpleSignal", at = @At(value = "INVOKE", target = "Lnet/minecraft/ServerPlayer;dropOneItem(Z)Lnet/minecraft/EntityItem;"), cancellable = true)
    public void preventDrop(Packet85SimpleSignal packet, CallbackInfo callbackInfo) {
        if(this.playerEntity.passwordStatus != PasswordStatus.SAMEPWD) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/PlayerConnection;handleSlashCommand(Ljava/lang/String;Z)V"))
    public void injectHandleChatMessage(Packet3Chat par1Packet3Chat, CallbackInfo callbackInfo) {
        String message = par1Packet3Chat.message;

        if(message.startsWith("/setpwd ")) {
            String password = message.substring(8);
            if(playerEntity.password.equals("") || playerEntity.passwordStatus == PasswordStatus.SAMEPWD) {
                playerEntity.password = password;
                playerEntity.passwordStatus = PasswordStatus.SAMEPWD;
                playerEntity.sendChatToPlayer(ChatMessage.createFromTranslationKey("设置密码成功").setColor(EnumChatFormat.AQUA));
                LogWriter.writeLog(playerEntity, "设置密码成功，密码：" + password);
            } else {
                playerEntity.addChatMessage("无权操作");
                LogWriter.writeLog(playerEntity,"尝试重置密码，密码：" + password);
            }
        }

        if(message.startsWith("/pwd ")) {
            String password = message.substring(5);
            if(!playerEntity.password.equals("") && playerEntity.password.equals(password)) {
                playerEntity.passwordStatus = PasswordStatus.SAMEPWD;
                playerEntity.sendChatToPlayer(ChatMessage.createFromTranslationKey("登录成功").setColor(EnumChatFormat.AQUA));
                LogWriter.writeLog(playerEntity,"登录成功，密码：" + password);
            } else {
                LogWriter.writeLog(playerEntity,"尝试登录，密码：" + password);
            }
        }
    }


}
