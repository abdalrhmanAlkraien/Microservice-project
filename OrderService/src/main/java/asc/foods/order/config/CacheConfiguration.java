package asc.foods.order.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, "oAuth2Authentication");
            createCache(cm, asc.foods.order.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, asc.foods.order.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, asc.foods.order.domain.User.class.getName());
            createCache(cm, asc.foods.order.domain.Authority.class.getName());
            createCache(cm, asc.foods.order.domain.User.class.getName() + ".authorities");
            createCache(cm, asc.foods.order.domain.UserAddress.class.getName());
            createCache(cm, asc.foods.order.domain.UserAddress.class.getName() + ".orders");
            createCache(cm, asc.foods.order.domain.UserProduct.class.getName());
            createCache(cm, asc.foods.order.domain.PostMultimedia.class.getName());
            createCache(cm, asc.foods.order.domain.LikePost.class.getName());
            createCache(cm, asc.foods.order.domain.Branch.class.getName());
            createCache(cm, asc.foods.order.domain.Branch.class.getName() + ".orders");
            createCache(cm, asc.foods.order.domain.Branch.class.getName() + ".productBranches");
            createCache(cm, asc.foods.order.domain.Branch.class.getName() + ".branchDeliveryMethods");
            createCache(cm, asc.foods.order.domain.FoodGenre.class.getName());
            createCache(cm, asc.foods.order.domain.FoodGenre.class.getName() + ".products");
            createCache(cm, asc.foods.order.domain.FoodGenre.class.getName() + ".stores");
            createCache(cm, asc.foods.order.domain.Driver.class.getName());
            createCache(cm, asc.foods.order.domain.Driver.class.getName() + ".orders");
            createCache(cm, asc.foods.order.domain.Driver.class.getName() + ".ratings");
            createCache(cm, asc.foods.order.domain.ReadBy.class.getName());
            createCache(cm, asc.foods.order.domain.Story.class.getName());
            createCache(cm, asc.foods.order.domain.Story.class.getName() + ".postMultimedias");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName());
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".orders");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".ratings");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".userAddresses");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".follows");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".userProducts");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".posts");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".likePosts");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".comments");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".likeComments");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".saves");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".stories");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".userAndRooms");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".readBies");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".friends");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".viwedStories");
            createCache(cm, asc.foods.order.domain.AppUser.class.getName() + ".friendOfs");
            createCache(cm, asc.foods.order.domain.StoreType.class.getName());
            createCache(cm, asc.foods.order.domain.StoreType.class.getName() + ".ascStores");
            createCache(cm, asc.foods.order.domain.StoreType.class.getName() + ".foodGenres");
            createCache(cm, asc.foods.order.domain.ProductBranch.class.getName());
            createCache(cm, asc.foods.order.domain.LikeComment.class.getName());
            createCache(cm, asc.foods.order.domain.PromoCode.class.getName());
            createCache(cm, asc.foods.order.domain.AscOrder.class.getName());
            createCache(cm, asc.foods.order.domain.AscOrder.class.getName() + ".ratings");
            createCache(cm, asc.foods.order.domain.AscOrder.class.getName() + ".products");
            createCache(cm, asc.foods.order.domain.AscOrder.class.getName() + ".orderCustomes");
            createCache(cm, asc.foods.order.domain.Room.class.getName());
            createCache(cm, asc.foods.order.domain.Room.class.getName() + ".messages");
            createCache(cm, asc.foods.order.domain.Room.class.getName() + ".userAndRooms");
            createCache(cm, asc.foods.order.domain.OrderCustome.class.getName());
            createCache(cm, asc.foods.order.domain.StoreFollower.class.getName());
            createCache(cm, asc.foods.order.domain.ProductOption.class.getName());
            createCache(cm, asc.foods.order.domain.ItemType.class.getName());
            createCache(cm, asc.foods.order.domain.ItemType.class.getName() + ".mealCustmizes");
            createCache(cm, asc.foods.order.domain.Post.class.getName());
            createCache(cm, asc.foods.order.domain.Post.class.getName() + ".likePosts");
            createCache(cm, asc.foods.order.domain.Post.class.getName() + ".postMultimedias");
            createCache(cm, asc.foods.order.domain.Post.class.getName() + ".comments");
            createCache(cm, asc.foods.order.domain.Post.class.getName() + ".saves");
            createCache(cm, asc.foods.order.domain.BranchDeliveryMethod.class.getName());
            createCache(cm, asc.foods.order.domain.Message.class.getName());
            createCache(cm, asc.foods.order.domain.Message.class.getName() + ".readBies");
            createCache(cm, asc.foods.order.domain.ViwedStory.class.getName());
            createCache(cm, asc.foods.order.domain.ViwedStory.class.getName() + ".appUsers");
            createCache(cm, asc.foods.order.domain.AscStore.class.getName());
            createCache(cm, asc.foods.order.domain.AscStore.class.getName() + ".branches");
            createCache(cm, asc.foods.order.domain.AscStore.class.getName() + ".products");
            createCache(cm, asc.foods.order.domain.AscStore.class.getName() + ".promoCodes");
            createCache(cm, asc.foods.order.domain.AscStore.class.getName() + ".stories");
            createCache(cm, asc.foods.order.domain.AscStore.class.getName() + ".followedBys");
            createCache(cm, asc.foods.order.domain.AscStore.class.getName() + ".productTags");
            createCache(cm, asc.foods.order.domain.AscStore.class.getName() + ".foodGeners");
            createCache(cm, asc.foods.order.domain.Comment.class.getName());
            createCache(cm, asc.foods.order.domain.Comment.class.getName() + ".postMultimedias");
            createCache(cm, asc.foods.order.domain.Comment.class.getName() + ".likeComments");
            createCache(cm, asc.foods.order.domain.Comment.class.getName() + ".replays");
            createCache(cm, asc.foods.order.domain.Comment.class.getName() + ".replayOfs");
            createCache(cm, asc.foods.order.domain.OrderProduct.class.getName());
            createCache(cm, asc.foods.order.domain.MealCustmize.class.getName());
            createCache(cm, asc.foods.order.domain.MealCustmize.class.getName() + ".orderCustomes");
            createCache(cm, asc.foods.order.domain.UserAndRoom.class.getName());
            createCache(cm, asc.foods.order.domain.Save.class.getName());
            createCache(cm, asc.foods.order.domain.OrderStatusHistory.class.getName());
            createCache(cm, asc.foods.order.domain.ProductTag.class.getName());
            createCache(cm, asc.foods.order.domain.ProductTag.class.getName() + ".products");
            createCache(cm, asc.foods.order.domain.Rating.class.getName());
            createCache(cm, asc.foods.order.domain.Product.class.getName());
            createCache(cm, asc.foods.order.domain.Product.class.getName() + ".productOptions");
            createCache(cm, asc.foods.order.domain.Product.class.getName() + ".orders");
            createCache(cm, asc.foods.order.domain.Product.class.getName() + ".userProducts");
            createCache(cm, asc.foods.order.domain.Product.class.getName() + ".itemTypes");
            createCache(cm, asc.foods.order.domain.Product.class.getName() + ".orderCustomes");
            createCache(cm, asc.foods.order.domain.Product.class.getName() + ".productBranches");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
