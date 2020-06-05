package de.hska.eshopapi;


import de.hska.eshopapi.cachemodels.CategoryViewList;
import de.hska.eshopapi.cachemodels.ProductViewList;
import de.hska.eshopapi.cachemodels.RoleList;
import de.hska.eshopapi.cachemodels.UserViewList;
import de.hska.eshopapi.model.Role;
import de.hska.eshopapi.viewmodels.CategoryView;
import de.hska.eshopapi.viewmodels.ProductView;
import de.hska.eshopapi.viewmodels.UserView;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class CacheConfig {

    private CacheManager cacheManager;

    public CacheConfig(){
        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("categoryViewCacheAll", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, CategoryViewList.class, ResourcePoolsBuilder.heap(1)).build())
                .withCache("categoryViewCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(UUID.class, CategoryView.class, ResourcePoolsBuilder.heap(100)) .build())
                .withCache("productViewCacheAll", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, ProductViewList.class, ResourcePoolsBuilder.heap(1)) .build())
                .withCache("productViewCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(UUID.class, ProductView.class, ResourcePoolsBuilder.heap(100)) .build())
                .withCache("userViewCacheAll", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, UserViewList.class, ResourcePoolsBuilder.heap(1)) .build())
                .withCache("userViewCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(UUID.class, UserView.class, ResourcePoolsBuilder.heap(100)) .build())
                .withCache("roleCacheAll", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, RoleList.class, ResourcePoolsBuilder.heap(1)) .build())
                .withCache("roleCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(UUID.class, Role.class, ResourcePoolsBuilder.heap(100)) .build())
                .build(true);
    }

    @Bean
    public CacheManager cacheManager() {
        return cacheManager;
    }

    @Bean
    public Cache<Long, CategoryViewList> categoryViewCacheAll() {
        return cacheManager.getCache("categoryViewCacheAll", Long.class, CategoryViewList.class);
    }
    @Bean
    public Cache<UUID, CategoryView> categoryViewCache() {
        return cacheManager.getCache("categoryViewCache", UUID.class, CategoryView.class);
    }

    @Bean
    public Cache<Long, ProductViewList> productViewCacheAll() {
        return cacheManager.getCache("productViewCacheAll", Long.class, ProductViewList.class);
    }
    @Bean
    public Cache<UUID, ProductView> productViewCache() {
        return cacheManager.getCache("productViewCache", UUID.class, ProductView.class);
    }

    @Bean
    public Cache<Long, UserViewList> userViewCacheAll() {
        return cacheManager.getCache("userViewCacheAll", Long.class, UserViewList.class);
    }
    @Bean
    public Cache<UUID, UserView> userViewCache() {
        return cacheManager.getCache("userViewCache", UUID.class, UserView.class);
    }

    @Bean
    public Cache<Long, RoleList> roleCacheAll() {
        return cacheManager.getCache("roleCacheAll", Long.class, RoleList.class);
    }
    @Bean
    public Cache<UUID, Role> roleCache() {
        return cacheManager.getCache("roleCache", UUID.class, Role.class);
    }

}

