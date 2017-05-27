package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 存放在本地缓存中
 */
public class TokenCache {
    //声明日志
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
   //设置一个常量
    public  static  final String TOKEN_PREFIX="token_";
    //声明静态内存块；LoadingCache：guawa里面的本地缓存
    /**
     * 调用链的模式
     * initialCapacity(1000)设置缓存的初始化容量
     * *maximumSize(10000)设置最大缓存容量，当超过这个值得时候，guawa的缓存会调用LRU算法来移除缓存项
     * expireAfterAccess(12, TimeUnit.HOURS)设置有效期为12小时
     */
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个load方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);

    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            //打印异常堆栈
            logger.error("localCache get error", e);
        }
        return null;
    }

}
