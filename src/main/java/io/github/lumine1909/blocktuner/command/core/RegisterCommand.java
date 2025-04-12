package io.github.lumine1909.blocktuner.command.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterCommand {

    String name();

    String permission() default "blocktuner.command";

    String[] aliases() default {};
}
