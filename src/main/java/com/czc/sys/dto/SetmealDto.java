package com.czc.sys.dto;

import com.czc.sys.entity.Setmeal;
import com.czc.sys.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @author czc
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
