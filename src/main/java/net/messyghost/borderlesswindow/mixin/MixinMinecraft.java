package net.messyghost.borderlesswindow.mixin;

import net.messyghost.borderlesswindow.BorderlessWindow;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Mixin(MinecraftClient.class)
public class MixinMinecraft {
    @Inject(method = "<init>", at = @At("RETURN"))
    protected void inject_init(CallbackInfo cbInfo) {
        //load config
        Logger logger = BorderlessWindow.LOGGER;
        File configFile = BorderlessWindow.CONFIG_FILE;
        try {
            File configDir = configFile.getParentFile();
            if(!configDir.isDirectory()) {
                configDir.mkdirs();
            }

            if(!configFile.isFile()) {
                FileOutputStream fileOut = new FileOutputStream(configFile);
                fileOut.write("false".getBytes(StandardCharsets.UTF_8));
                fileOut.close();
            }
            else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
                if(reader.readLine().equals("true")) {
                    BorderlessWindow.tryToggle();
                }
                reader.close();
            }
        }
        catch (FileNotFoundException e) {
            logger.error("Failure loading config file.");
        }
        catch (IOException e) {
            logger.error("Failure loading config file.");
        }
    }

    @Inject(method = "stop", at = @At("HEAD"))
    protected void stop(CallbackInfo cbInfo) {
        try {
            FileOutputStream outputStream = new FileOutputStream(BorderlessWindow.CONFIG_FILE);
            outputStream.write(String.valueOf(BorderlessWindow.isToggled()).getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        }
        catch (Exception e) {
            BorderlessWindow.LOGGER.warn("Cannot save config file.");
        }
    }
}
