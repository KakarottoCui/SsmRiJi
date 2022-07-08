package com.blog.service;

import com.blog.entity.Blog;

import java.util.List;
import java.util.Map;

public interface BlogService {
  /** 无参数查询博客列表(供首页使用) */
  public List<Blog> countList();

  /** 带参数查询博客列表 */
  public List<Blog> list(Map<String, Object> map);

  /** 带参数查询博客数量 */
  public Long getTotal(Map<String, Object> map);

  /** 根据主键查询一条博客信息 */
  public Blog findById(Integer id);

  /** 添加一条博客 */
  public Integer add(Blog blog);

  /** 修改一条博客 */
  public Integer update(Blog blog);

  /** 根据主键删除一条博客 */
  public Integer delete(Integer id);

  /** 根据类型查询博客数量 */
  public Integer getBlogByTypeId(Integer typeId);

  /** 上一篇 */
  public Blog getLastBlog(Integer id);

  /** 下一篇 */
  public Blog getNextBlog(Integer id);
}
