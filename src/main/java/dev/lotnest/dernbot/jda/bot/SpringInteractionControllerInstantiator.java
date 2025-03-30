package dev.lotnest.dernbot.jda.bot;

import com.github.kaktushose.jda.commands.dispatching.instance.InteractionControllerInstantiator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringInteractionControllerInstantiator implements InteractionControllerInstantiator {
    private final ApplicationContext applicationContext;

    @Override
    public <T> T instance(Class<T> aClass, Context context) {
        return applicationContext.getBean(aClass);
    }
}
