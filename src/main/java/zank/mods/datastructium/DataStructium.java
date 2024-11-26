package zank.mods.datastructium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ZZZank
 */
@Mod(DataStructium.MOD_ID)
public class DataStructium {
    public static final String MOD_ID = "datastructium";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    public DataStructium() {
        DSConfig.save();
    }
}
