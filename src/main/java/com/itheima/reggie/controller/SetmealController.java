package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> pageinfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage=new Page<>();

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageinfo,lambdaQueryWrapper);

        BeanUtils.copyProperties(pageinfo,dtoPage,"records");
        List<Setmeal> records = pageinfo.getRecords();

        List<SetmealDto> list= records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);
            if(category!=null){
                String name1 = category.getName();
                setmealDto.setCategoryName(name1);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    @DeleteMapping()
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/0")
    public R<String> update(@RequestParam List<Long> ids){
        setmealService.update(ids);
//        log.info(ids.toString());
        return R.success("停售成功");
    }

    @PostMapping("/status/1")
    public R<String> update1(@RequestParam List<Long> ids){
        setmealService.update1(ids);
//        log.info(ids.toString());
        return R.success("启售成功");
    }









}
