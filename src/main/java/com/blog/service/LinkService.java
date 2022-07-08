package com.blog.service;

import com.blog.entity.Link;

import java.util.List;
import java.util.Map;

public interface LinkService {
  /** 根据id查询一条信息 */
  public Link findById(Integer id);
  /** 不固定参数查询l链接列表 */
  public List<Link> list(Map<String, Object> paramMap);
  /** 不固定参数查询l链接类型数量 */
  public Long getTotal(Map<String, Object> paramMap);
  /** 添加一条l链接 */
  public Integer add(Link link);
  /** 修改一条l链接 */
  public Integer update(Link link);
  /** 删除一条l链接 */
  public Integer delete(Integer id);
}
