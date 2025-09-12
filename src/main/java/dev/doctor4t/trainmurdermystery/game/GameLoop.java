package dev.doctor4t.trainmurdermystery.game;

import dev.doctor4t.trainmurdermystery.cca.TrainMurderMysteryComponents;
import dev.doctor4t.trainmurdermystery.cca.WorldGameComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.UUID;

public class GameLoop {
    public static void tick(ServerWorld serverWorld) {
        WorldGameComponent game = TrainMurderMysteryComponents.GAME.get(serverWorld);
        if (game.isRunning()) {
            // check hitman win condition (all targets are dead)
            WinStatus winStatus = WinStatus.HITMEN;
            for (UUID uuid : game.getTargets()) {
                if (!isPlayerEliminated(serverWorld, uuid)) {
                    winStatus = WinStatus.NONE;
                }
            }

            // check passenger win condition (all hitmen are dead)
            if (winStatus == WinStatus.NONE) {
                winStatus = WinStatus.PASSENGERS;
                for (UUID uuid : game.getHitmen()) {
                    if (!isPlayerEliminated(serverWorld, uuid)) {
                        winStatus = WinStatus.NONE;
                    }
                }
            }

            // hitman win
            if (winStatus != WinStatus.NONE) {
                for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                    player.sendMessage(Text.translatable("game.win." + winStatus.name().toLowerCase(Locale.ROOT)), true);
                    System.out.println("game.win." + winStatus.name().toLowerCase(Locale.ROOT));
                }
                game.setRunning(false);
            }
        }
    }

    private static boolean isPlayerEliminated(ServerWorld world, UUID targetUuid) {
        PlayerEntity player = world.getPlayerByUuid(targetUuid);
        return player == null || !player.isAlive() || player.isCreative() || player.isSpectator();
    }

    public enum WinStatus {
        NONE, HITMEN, PASSENGERS
    }
}
