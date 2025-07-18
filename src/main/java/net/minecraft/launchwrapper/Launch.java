package net.minecraft.launchwrapper;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Compile Only
 */
@SuppressWarnings("unused")
public class Launch {
    public static URLClassLoader classLoader = new URLClassLoader(new URL[0]);
    public static Map<String, Object> blackboard = new HashMap<>();
}
