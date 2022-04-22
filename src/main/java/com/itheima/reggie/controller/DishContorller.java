package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yy
 * @version 1.0
 */
@RestController
@RequestMapping("/dish")
public class DishContorller {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody  DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> page1 = new Page<>(page, pageSize);
        Page<DishDto> page2=new Page<>();
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(page1,lambdaQueryWrapper);
        BeanUtils.copyProperties(page1,page2,"records");

        List<Dish> records = page1.getRecords();
        List<DishDto> list= records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);
            String name1 = category.getName();
            dishDto.setCategoryName(name1);
            return dishDto;
        }).collect(Collectors.toList());

        page2.setRecords(list);

        return R.success(page2);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody  DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lambdaQueryWrapper);
        return R.success(list);
    }





}
