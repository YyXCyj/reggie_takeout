package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yy
 * @version 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功！");
    }


    @GetMapping("/page")
    public R<Page> page(int page,int pageSize ){
        Page<Category> paginfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(paginfo,lambdaQueryWrapper);
        return R.success(paginfo);
    }
    @DeleteMapping()
    public R<String> delete(Long ids){
        categoryService.remove(ids);
        return R.success("删除成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody  Category category){

        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }

}
