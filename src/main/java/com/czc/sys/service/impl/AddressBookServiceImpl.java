package com.czc.sys.service.impl;

import com.czc.sys.entity.AddressBook;
import com.czc.sys.mapper.AddressBookMapper;
import com.czc.sys.service.IAddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

}
