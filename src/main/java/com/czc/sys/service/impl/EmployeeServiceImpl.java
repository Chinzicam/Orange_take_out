package com.czc.sys.service.impl;

import com.czc.sys.entity.Employee;
import com.czc.sys.mapper.EmployeeMapper;
import com.czc.sys.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
