package net.wensc.mitemod.serverpassword;

import net.wensc.mitemod.serverpassword.trans.TransMarker;
import net.xiaoyu233.fml.AbstractMod;


import net.xiaoyu233.fml.classloading.Mod;
import net.xiaoyu233.fml.config.InjectionConfig;
import org.spongepowered.asm.mixin.MixinEnvironment;

@Mod(MixinEnvironment.Side.SERVER)
public class MITEServerPassword extends AbstractMod {

    public MITEServerPassword() {
    }

    public void preInit() {
    }


    @Override
    public InjectionConfig getInjectionConfig() {
        return InjectionConfig.Builder.of("mite-server-password", TransMarker.class.getPackage(), MixinEnvironment.Phase.INIT).setRequired().build();
    }


    public void postInit() {
        super.postInit();
    }


    public String modId() {
        return "mite-server-password";
    }

    public int modVerNum() {
        return 4;
    }

    public String modVerStr() {
        return "0.0.4";
    }
}
