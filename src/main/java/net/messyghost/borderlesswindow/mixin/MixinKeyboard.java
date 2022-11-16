package net.messyghost.borderlesswindow.mixin;

import net.messyghost.borderlesswindow.BorderlessWindow;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At("HEAD"))
    protected void onKey(long window, int key, int scancode, int action, int modify, CallbackInfo cbInfo) {
        if(BorderlessWindow.SWITCH.matchesKey(key, scancode) && window == MinecraftClient.getInstance().getWindow().getHandle() && action == GLFW.GLFW_PRESS) {
            BorderlessWindow.tryToggle();
        }
    }
}
