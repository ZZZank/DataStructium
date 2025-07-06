package zank.mods.datastructium.mods.minecraft;

import net.minecraft.nbt.*;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZZZank
 */
public final class DataStructiumCompoundTagType implements TagType<CompoundTag> {
    public CompoundTag load(DataInput input, int depth, NbtAccounter accounter) throws IOException {
        accounter.accountBits(384L);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }

        Map<String, Tag> map = new HashMap<>();

        byte type;
        while ((type = CompoundTag.readNamedTagType(input, accounter)) != 0) {
            // DataStructium: deduplicate key
            String name = CompoundTag.readNamedTagName(input, accounter).intern();

            accounter.accountBits(224 + 16L * name.length());
            accounter.accountBits(32L);

            Tag tag = CompoundTag.readNamedTagData(TagTypes.getType(type), name, input, depth + 1, accounter);

            if (map.put(name, tag) != null) {
                accounter.accountBits(288L);
            }
        }

        return new CompoundTag(map);
    }

    public String getName() {
        return "COMPOUND";
    }

    public String getPrettyName() {
        return "TAG_Compound";
    }
}
