package com.ch.ebusiness.controller.admin;

import com.ch.ebusiness.annotation.AdminLog;
import com.ch.ebusiness.dto.CacheStatsDTO;
import com.ch.ebusiness.service.MultiLevelCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存管理控制器
 * 提供缓存统计、清理、预热等管理功能
 */
@Controller
@RequestMapping("/admin/cache")
public class CacheManagementController extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(CacheManagementController.class);

    @Autowired
    private MultiLevelCacheService cacheService;

    /**
     * 缓存管理主页面
     */
    @GetMapping("/manage")
    public String cacheManagePage(Model model) {
        List<CacheStatsDTO> stats = cacheService.getCacheStats();
        model.addAttribute("cacheStats", stats);

        // 获取所有缓存键和常用模式
        List<String> allKeys = cacheService.getAllCacheKeys();
        List<String> patterns = cacheService.getCommonCachePatterns();
        model.addAttribute("cacheKeys", allKeys);
        model.addAttribute("cachePatterns", patterns);

        return "admin/cacheManagement";
    }

    /**
     * 获取缓存统计信息（AJAX接口）
     */
    @GetMapping("/stats")
    @ResponseBody
    public Map<String, Object> getCacheStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<CacheStatsDTO> stats = cacheService.getCacheStats();
            result.put("success", true);
            result.put("data", stats);
            logger.info("获取缓存统计信息成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取缓存统计失败: " + e.getMessage());
            logger.error("获取缓存统计失败", e);
        }
        return result;
    }

    /**
     * 清除指定键的缓存
     */
    @PostMapping("/evict")
    @ResponseBody
    @AdminLog(module = "缓存管理", operationType = "DELETE", description = "清除缓存键: #{cacheKey}")
    public Map<String, Object> evictCache(@RequestParam String cacheKey) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查缓存是否存在
            if (!cacheService.exists(cacheKey)) {
                result.put("success", false);
                result.put("message", "缓存键不存在: " + cacheKey);
                logger.warn("尝试清除不存在的缓存键: {}", cacheKey);
                return result;
            }

            cacheService.evict(cacheKey);
            result.put("success", true);
            result.put("message", "缓存清除成功: " + cacheKey);
            logger.info("清除缓存成功: {}", cacheKey);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "缓存清除失败: " + e.getMessage());
            logger.error("清除缓存失败: {}", cacheKey, e);
        }
        return result;
    }

    /**
     * 按模式清除缓存
     */
    @PostMapping("/evictByPattern")
    @ResponseBody
    @AdminLog(module = "缓存管理", operationType = "DELETE", description = "按模式批量清除: #{keyPattern}")
    public Map<String, Object> evictCacheByPattern(@RequestParam String keyPattern) {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.evictByPattern(keyPattern);
            result.put("success", true);
            result.put("message", "批量清除缓存成功: " + keyPattern);
            logger.info("按模式清除缓存成功: {}", keyPattern);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量清除缓存失败: " + e.getMessage());
            logger.error("按模式清除缓存失败: {}", keyPattern, e);
        }
        return result;
    }

    /**
     * 清空所有缓存
     */
    @PostMapping("/evictAll")
    @ResponseBody
    @AdminLog(module = "缓存管理", operationType = "DELETE", description = "清空所有缓存")
    public Map<String, Object> evictAllCache() {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.evictAll();
            result.put("success", true);
            result.put("message", "所有缓存已清空");
            logger.warn("清空所有缓存");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清空缓存失败: " + e.getMessage());
            logger.error("清空所有缓存失败", e);
        }
        return result;
    }

    /**
     * 检查缓存键是否存在
     */
    @GetMapping("/exists")
    @ResponseBody
    public Map<String, Object> checkCacheExists(@RequestParam String cacheKey) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = cacheService.exists(cacheKey);
            long expire = cacheService.getExpire(cacheKey);
            result.put("success", true);
            result.put("exists", exists);
            result.put("expire", expire);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "检查缓存失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 快捷清除操作 - 商品类型缓存
     */
    @PostMapping("/quickClear/goodsTypes")
    @ResponseBody
    @AdminLog(module = "缓存管理", operationType = "DELETE", description = "清除商品类型缓存 [键: index:goods_types]")
    public Map<String, Object> quickClearGoodsTypes() {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.evict("index:goods_types");
            result.put("success", true);
            result.put("message", "商品类型缓存已清除");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清除缓存失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 快捷清除操作 - 首页商品列表
     */
    @PostMapping("/quickClear/lastedGoods")
    @ResponseBody
    @AdminLog(module = "缓存管理", operationType = "DELETE", description = "清除首页商品列表 [模式: index:lasted_goods:*]")
    public Map<String, Object> quickClearLastedGoods() {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.evictByPattern("index:lasted_goods:*");
            result.put("success", true);
            result.put("message", "首页商品列表缓存已清除");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清除缓存失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 快捷清除操作 - 商品详情缓存
     */
    @PostMapping("/quickClear/goodsDetail")
    @ResponseBody
    @AdminLog(module = "缓存管理", operationType = "DELETE", description = "清除商品详情缓存 [模式: goods:detail:*]")
    public Map<String, Object> quickClearGoodsDetail() {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.evictByPattern("goods:detail:*");
            result.put("success", true);
            result.put("message", "商品详情缓存已清除");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清除缓存失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 快捷清除操作 - 搜索结果缓存
     */
    @PostMapping("/quickClear/search")
    @ResponseBody
    @AdminLog(module = "缓存管理", operationType = "DELETE", description = "清除搜索结果缓存 [模式: index:search:*]")
    public Map<String, Object> quickClearSearch() {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.evictByPattern("index:search:*");
            result.put("success", true);
            result.put("message", "搜索结果缓存已清除");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清除缓存失败: " + e.getMessage());
        }
        return result;
    }
}
