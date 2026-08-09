package io.github.lumine1909.blocktuner.proxy;

import com.example.proxying.api.ProxyService;
import com.example.proxying.api.annotation.Get;
import com.example.proxying.api.annotation.Proxy;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

@Proxy(Entity.class)
public interface EntityProxy {

    EntityProxy staticAccess = ProxyService.get().register(EntityProxy.class).wrap(null);

    @Get(value = "DATA_SHARED_FLAGS_ID", isStatic = true)
    EntityDataAccessor<Byte> sharedFlagsId();

    @Get(value = "DATA_CUSTOM_NAME", isStatic = true)
    EntityDataAccessor<Optional<Component>> customName();

    @Get(value = "DATA_CUSTOM_NAME_VISIBLE", isStatic = true)
    EntityDataAccessor<Boolean> customNameVisible();

    @Get(value = "DATA_NO_GRAVITY", isStatic = true)
    EntityDataAccessor<Boolean> noGravity();
}
