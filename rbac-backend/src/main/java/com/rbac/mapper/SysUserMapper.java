package com.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper。
 * @author Re-zero
 * @version 1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
