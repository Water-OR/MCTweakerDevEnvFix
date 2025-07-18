package net.minecraftforge.fml.relauncher;

import java.util.Map;

/**
 * Compile Only
 */
@SuppressWarnings("unused")
public interface IFMLLoadingPlugin {
    String[] getASMTransformerClass();
    
    String getModContainerClass();
    
    String getSetupClass();
    
    void injectData(Map<String, Object> data);
    
    String getAccessTransformerClass();
}
