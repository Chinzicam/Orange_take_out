package com.czc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.common.Result;
import com.czc.sys.entity.Employee;
import com.czc.sys.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    /**
     * 登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        //对密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //以下分别是对账号，密码，状态比对查询
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);
        if (emp == null) {
            return Result.error("用户不存在");
        }
        if (!emp.getPassword().equals(password)) {
            return Result.error("密码错误");
        }
        if (emp.getStatus() == 0) {
            return Result.error("该用户已被禁用");
        }
        //登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    /**
     * 退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增的员工信息：{}", employee.toString());
        //设置默认密码为123456，并采用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置createTime和updateTime
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //根据session来获取创建人的id
        Long empId = (Long) request.getSession().getAttribute("employee");
        //并设置
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        //存入数据库
        employeeService.save(employee);
        return Result.success("添加员工成功");
    }

    /**
     * 员工管理 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!(name == null || "".equals(name)), Employee::getName, name);
        //排除管理员
        wrapper.ne(Employee::getUsername,"admin");
        wrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    /**
     * 更新员工状态
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee={}", employee);
        //获取当前用户id
        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(id);
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息..");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return Result.success(employee);
        }
        return Result.error("未查询到该员工信息");
    }

}
