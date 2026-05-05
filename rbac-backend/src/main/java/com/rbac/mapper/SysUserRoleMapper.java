package com.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-角色关联 Mapper。
 * @author Re-zero
 * @version 1.0
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
