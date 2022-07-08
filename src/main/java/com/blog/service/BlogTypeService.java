package com.blog.service;

import com.blog.entity.BlogType;

import java.util.List;
import java.util.Map;

public interface BlogTypeService {
  /** 无参数查询所有博客类型 */
  public List<BlogType> countList();
  /** 根据id查询一条博客信息 */
  public BlogType findById(Integer id);
  /** 不固定参数查询博客列表 */
  public List<BlogType> list(Map<String, Object> paramMap);
  /** 不固定参数查询博客类型数量 */
  public Long getTotal(Map<String, Object> paramMap);
  /** 添加一条博客 */
  public Integer add(BlogType blogType);
  /** 修改一条博客 */
  public Integer update(BlogType blogType);
  /** 删除一条博客 */
  public Integer delete(Integer id);
}
