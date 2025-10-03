package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.cca.TMMComponents;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ToggleSnowCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:toggleSnow")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                                    TrainWorldComponent trainWorldComponent = TMMComponents.TRAIN.get(context.getSource().getWorld());
                                    trainWorldComponent.setSnow(!trainWorldComponent.isSnowing());
                                    return 1;
                                }
                        )
        );
    }
}
