package org.betterx.betterend.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;


public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(CommandRegistry::register);
    }

    private static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext commandBuildContext,
            Commands.CommandSelection commandSelection
    ) {}
}

