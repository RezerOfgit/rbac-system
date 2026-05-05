package com.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色 Mapper。
 * @author Re-zero
 * @version 1.0
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
}
