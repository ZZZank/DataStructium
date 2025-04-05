package zank.mods.datastructium.pools;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.val;
import org.lwjgl.opengl.GL20;

import java.util.Objects;
import java.util.function.ToIntFunction;

public final class ShaderProgramCache {

    private final int id;
    private final Object2IntMap<CharSequence> uniforms = new Object2IntOpenHashMap<>();
    private transient final ToIntFunction<CharSequence> uniformLocationMapper;

    public ShaderProgramCache(int id) {
        this.id = id;
        uniformLocationMapper = name -> GL20.glGetUniformLocation(id, name);
    }

    public int uniform(CharSequence name) {
        return this.uniforms.computeIntIfAbsent(name, uniformLocationMapper);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof ShaderProgramCache cache) {
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
        return "ShaderProgramCache<" + this.id + ">";
    }
}