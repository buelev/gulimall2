package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.IndexCategoryVO;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * description: IndexController
 * date: 2020-09-22 10:51
 * author: buelev
 * version: 1.0
 */
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private RedissonClient redisson;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //查询以及分类的数据
        List<CategoryEntity> list = categoryService.indexCategory();
        model.addAttribute("indexCategories", list);
        return "index";
    }

    /**
     * 查询二级和三级分类数据
     */
    @GetMapping("index/json/catalog.json")
    @ResponseBody
    public Map<String, List<IndexCategoryVO>> getCategoryJson() {
        return categoryService.getSecondCategoryJson();
    }

    /**
     * 异常不会导致锁释放，但是程序中断会自动释放锁
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        RLock lock = redisson.getLock("hello-lock");
        //枷锁
        lock.lock();
        try {
            System.out.println("加锁成功,执行业务" + System.currentTimeMillis());
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //解锁
//            lock.unlock();
        }
        return "hello";
    }

    /**
     * 闭锁测试
     */
    @GetMapping("/lockdoor")
    @ResponseBody
    public String lockdoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();
        return "放假了，锁门...";
    }

    @GetMapping("gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id) {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();//计数减一
        return id + "班的人都走了...";
    }

    /**
     * 信号量
     */
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.acquire();
        return "OK";
    }

    @GetMapping
    @ResponseBody
    public String go() {
        RSemaphore park = redisson.getSemaphore("park");
        park.release();//释放一个信号量
        return "OK";
    }
}
