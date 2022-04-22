package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yy
 * @version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1=dishService.count(lambdaQueryWrapper);
        if(count1>0){
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper2 =new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Setmeal::getCategoryId,id);
        int count2=setmealService.count(lambdaQueryWrapper2);
        if(count2>0){
            throw new CustomException("当前分类项关联了套餐，不能删除");
        }
        super.removeById(id);

    }
}
