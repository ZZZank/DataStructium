package zank.mods.datastructium.pools;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.val;
import org.lwjgl.opengl.GL20;

import java.util.Objects;

public final class ShaderProgramCache {

    private final int id;
    private final Object2IntMap<CharSequence> uniforms = new Object2IntOpenHashMap<>();

    public ShaderProgramCache(int id) {
        this.id = id;
    }

    public int uniform(CharSequence uniformName) {
        return this.uniforms.computeIfAbsent(
            uniformName,
            value -> GL20.glGetUniformLocation(this.id, uniformName)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof ShaderProgramCache) {
            val cache = (ShaderProgramCache) obj;
            return Objects.equals(this.id, cache.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 * this.id + this.uniforms.size();
    }

    @Override
    public String toString() {
        return "SPC<" + this.id + ">";
    }
}