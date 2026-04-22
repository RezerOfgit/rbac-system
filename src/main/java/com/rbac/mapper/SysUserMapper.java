package com.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Re-zero
 * @version 1.0
 */
@Mapper //这是一个 Mapper 接口，需要交由 Spring 容器管理
public interface SysUserMapper extends BaseMapper<SysUser> {
    //继承了 BaseMapper，insert, deleteById, updateById, selectById 等几十个单表方法！无需写 SQL！
}
