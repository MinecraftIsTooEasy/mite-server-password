package net.wensc.mitemod.serverpassword.util;

import net.minecraft.ServerPlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
    public static String newline = new String(System.getProperty("line.separator").getBytes());
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void  writeLog(ServerPlayer serverPlayer, String message) {
        File file = new File("player-password.log");
        try
        {
            FileWriter e = new FileWriter(file, true);
            StringBuffer sb = new StringBuffer();

            sb.append("["+simpleDateFormat.format(Long.valueOf((new Date().getTime())))+"] ["+serverPlayer.getEntityName()+"]" + message + newline);

            e.append(sb.toString());
            e.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getDimName(int dim) {
        return dim == 0 ? "主世界" : dim == -2 ? "地下世界" : dim == -1 ? "地狱" : dim == 1 ? "末地" : "未知世界";
    }
}
