package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author yy
 * @version 1.0
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void removeWithDish(List<Long> id);
    public void update(List<Long> ids);
    public void update1(List<Long> ids);
}
