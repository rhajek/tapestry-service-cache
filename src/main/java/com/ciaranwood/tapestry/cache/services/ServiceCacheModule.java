package com.ciaranwood.tapestry.cache.services;

import com.ciaranwood.tapestry.cache.services.advice.CacheMethodDecorator;
import com.ciaranwood.tapestry.cache.services.impl.BlockingCacheFactory;
import com.ciaranwood.tapestry.cache.services.impl.CacheWriterClassFactoryImpl;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.PreventServiceDecoration;

@PreventServiceDecoration
public final class ServiceCacheModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(CacheFactory.class, BlockingCacheFactory.class);
        binder.bind(CacheWriterClassFactory.class, CacheWriterClassFactoryImpl.class);
    }

    public static CacheMethodDecorator buildCacheMethodDecorator(@Builtin AspectDecorator decorator,
                                                                 @Local CacheFactory cacheFactory) {
        return new CacheMethodDecoratorImpl(decorator, cacheFactory);
    }

    @Match("*")
    @Order("after:*")
    public static <T> T decorateForCaching(T delegate, CacheMethodDecorator decorator, ServiceResources resources) {
        return decorator.build(delegate, resources);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(ServiceCacheConstants.BLOCKING_CACHE_TIMEOUT, "30000");
        configuration.add(ServiceCacheConstants.EHCACHE_CONFIGURATION_FILE, "/META-INF/ehcache-default.xml");
    }

}
