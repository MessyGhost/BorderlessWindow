package net.messyghost.borderlesswindow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BorderlessWindow implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Borderless Window");
    public static final FabricKeyBinding SWITCH =
            FabricKeyBinding.Builder.create(Identifier.tryParse("blw:switch"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "key.categories.misc").build();

    public static final File CONFIG_FILE = new File(MinecraftClient.getInstance().runDirectory, "config/blw.cfg");

    @Override
    public void onInitialize() {
        KeyBindingRegistry.INSTANCE.register(SWITCH);
    }

    private static boolean toggled = false;
    private static int x, y, width, height;

    public static boolean isToggled() {
        return toggled;
    }

    public static void tryToggle() {
        Window window = MinecraftClient.getInstance().getWindow();
        if(!window.isFullscreen()) {
            toggled = !toggled;

            if(toggled) {
                width = window.getWidth();
                height = window.getHeight();
                x = window.getX();
                y = window.getY();

                long monitor = GLFW.glfwGetPrimaryMonitor();
                GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);
                GLFW.glfwSetWindowAttrib(window.getHandle(), GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
                GLFW.glfwSetWindowPos(window.getHandle(), 0, 0);
                GLFW.glfwSetWindowSize(window.getHandle(), mode.width(), mode.height());
            }
            else {
                GLFW.glfwSetWindowAttrib(window.getHandle(), GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
                GLFW.glfwSetWindowPos(window.getHandle(), x, y);
                GLFW.glfwSetWindowSize(window.getHandle(), width, height);
            }
        }
    }
}
