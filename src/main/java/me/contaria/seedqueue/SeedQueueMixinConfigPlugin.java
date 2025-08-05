package me.contaria.seedqueue;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import me.contaria.speedrunapi.mixin_plugin.SpeedrunMixinConfigPlugin;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

/**
 * SeedQueues Mixin Config Plugin, extends SpeedrunAPI's plugin to inherit its functionality.
 * Only used for compatibility with Sodium Mac, will be redundant once Sodium is updated to not need the seperate mac version anymore.
 */
public class SeedQueueMixinConfigPlugin extends SpeedrunMixinConfigPlugin implements MixinCanceller {
    private static final boolean MAC_SODIUM = FabricLoader.getInstance().isModLoaded("sodiummac");

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Sodium Mac "Compat"
        // Create RenderCache Async doesn't apply on Sodium Mac
        if (MAC_SODIUM) {
            if (mixinClassName.startsWith(this.mixinPackage + ".compat.sodium.profiling")) {
                return false;
            }
            if (mixinClassName.equals(this.mixinPackage + ".compat.sodium.ClientChunkManagerMixin") || mixinClassName.equals(this.mixinPackage + ".compat.sodium.ChunkBuilder$WorkerRunnableMixin2")) {
                return false;
            }
        }
        return super.shouldApplyMixin(targetClassName, mixinClassName);
    }

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        // SleepBackgrounds Thread Executor mixin has been observed to hurt performance when SeedQueue is active
        // since SeedQueue spawns many more executors
        return mixinClassName.equals("com.redlimerl.sleepbackground.mixin.MixinThreadExecutor");
    }
}
