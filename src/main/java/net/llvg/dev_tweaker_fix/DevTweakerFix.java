package net.llvg.dev_tweaker_fix;

import com.google.common.primitives.Ints;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An {@link net.minecraftforge.fml.relauncher.IFMLLoadingPlugin} for development environment fix
 * <p>
 * It will search for tweakers in all urls of the classloader
 * </p>
 * <p>
 * First look up the {@code TweakClass} attribute.<br>
 * Following action will not be triggered if it is not found.<br>
 * The result will be stored as {@code tweak}.
 * </p>
 * <p>
 * After the attribute found, look up the {@code TweakOrder} attribute.<br>
 * If it is not found or failed to parse, will fall back to {@code 0}.<br>
 * The result will be stored as {@code order}
 * </p>
 * <p>
 * Then bundle the {@code tweak} and {@code order} as {@link net.llvg.dev_tweaker_fix.TweakInfo}.<br>
 * After that put the bundle into a list.
 * </p>
 * <p>
 * After searching in all urls, sort the list.<br>
 * Then put {@code tweak} of each element in the list into {@code TweakClasses} of {@link net.minecraft.launchwrapper.Launch#blackboard}
 * </p>
 */
public final class DevTweakerFix
  implements
  IFMLLoadingPlugin
{
    private static final Logger logger = LogManager.getLogger("Develop Env Tweaker Fix");
    private static final Object lock = new Object();
    private static volatile boolean invoked = false;
    
    /**
     * <p>
     * A default constructor which should not be called manually.
     * It will be automatically invoked by FML.
     * </p>
     */
    public DevTweakerFix() { /* Do Nothing */ }
    
    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    @Override
    public String getModContainerClass() {
        return null;
    }
    
    @Override
    public String getSetupClass() {
        return null;
    }
    
    @Override
    public void injectData(Map<String, Object> data) {
        synchronized (lock) {
            if (invoked) return;
            invoked = true;
        }
        
        if ("true".equalsIgnoreCase(System.getProperty("mcTweakerDevEnvFix.disabled", "false"))) {
            logger.info("Fix is disabled by property");
            return;
        }
        
        logger.info("Start looking up tweakers");
        Object tweakersRaw = Launch.blackboard.get("TweakClasses");
        
        if (!(tweakersRaw instanceof List)) {
            logger.error("[TweakClasses] in blackboard is {}, but not java.util.List, this is not permitted", tweakersRaw.getClass().getName());
            throw new IllegalArgumentException(
              "[TweakClasses] in blackboard is " + tweakersRaw.getClass().getName() + " with loader " + tweakersRaw.getClass().getClassLoader().getClass() +
              ", but not java.util.List with loader " + List.class.getClassLoader().getClass() + ", this is not permitted"
            );
        }
        
        @SuppressWarnings ("unchecked")
        List<String> tweakers = (List<String>) tweakersRaw;
        
        List<TweakInfo> infos = new ArrayList<>();
        for (URL url : Launch.classLoader.getURLs()) {
            if (
              !"file".equalsIgnoreCase(url.getProtocol()) ||
              !url.toExternalForm().endsWith(".jar")
            ) continue;
            try (JarFile jar = new JarFile(new File(url.toURI()))) {
                Optional.ofNullable(jar.getManifest()).map(Manifest::getMainAttributes).ifPresent(
                  attr -> Optional.ofNullable(attr.getValue("TweakClass")).ifPresent(tweak -> {
                      int order = Optional.ofNullable(attr.getValue("TweakOrder")).map(Ints::tryParse).orElse(0);
                      logger.info("Found tweaker in url '{}', class '{}', order '{}'", url.toExternalForm(), tweak, order);
                      infos.add(new TweakInfo(tweak, order));
                  })
                );
            } catch (IOException | URISyntaxException e) {
                logger.warn("Failure occur while processing url {}", url, e);
            }
        }
        
        infos.stream().sorted().map(TweakInfo::getTweak).forEach(tweakers::add);
        logger.info("Finish looking up tweakers");
    }
    
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
    
}