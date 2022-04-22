package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yy
 * @version 1.0
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);


        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count=this.count(lambdaQueryWrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖中，无法删除!");
        }
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1=new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper1);
    }

    @Override
    public void update(List<Long> ids) {
        List<Setmeal> setmealList = this.listByIds(ids);
//        log.info(setmealList.toString());
        for (Setmeal setmeal : setmealList) {
            setmeal.setStatus(0);
//            log.info(setmeal.toString());
        }
        this.updateBatchById(setmealList);
    }

    @Override
    public void update1(List<Long> ids) {
        List<Setmeal> setmealList = this.listByIds(ids);
//        log.info(setmealList.toString());
        for (Setmeal setmeal : setmealList) {
            setmeal.setStatus(1);
//            log.info(setmeal.toString());
        }
        this.updateBatchById(setmealList);
    }
}
