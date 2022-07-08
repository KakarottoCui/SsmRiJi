package com.blog.controller.admin;

import com.blog.entity.Blog;
import com.blog.entity.PageBean;
import com.blog.lucene.BlogIndex;
import com.blog.service.BlogService;
import com.blog.util.DateJsonValueProcessor;
import com.blog.util.ResponseUtil;
import com.blog.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {

  @Resource private BlogService blogService;
  BlogIndex blogIndex = new BlogIndex();
  /** 新增一条博客 */
  @RequestMapping("/save")
  public String save(Blog blog, HttpServletResponse response) throws Exception {
    int resultTotal = 0;
    if (blog.getId() == null) { // 添加
      resultTotal = blogService.add(blog);
      blogIndex.addIndex(blog);
    } else { // 修改
      resultTotal = blogService.update(blog);
      blogIndex.updateIndex(blog);
    }

    JSONObject result = new JSONObject();
    if (resultTotal > 0) {
      result.put("success", Boolean.valueOf(true));
    } else {
      result.put("success", Boolean.valueOf(false));
    }
    ResponseUtil.write(response, result);
    return null;
  }
  /** 查询博客列表 */
  @RequestMapping("/list")
  public String list(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "rows", required = false) String rows,
      Blog blog,
      HttpServletResponse response)
      throws Exception {
    // 首先要传进分页参数
    PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
    Map<String, Object> map = new HashMap<>();
    map.put("start", pageBean.getStart());
    map.put("size", pageBean.getPageSize());
    // 搜索条件
    map.put("title", StringUtil.formatLike(blog.getTitle()));
    // 分页查询博客信息列表
    List<Blog> blogList = blogService.list(map);
    // 总共多少条数据
    Long total = blogService.getTotal(map);
    // 定义一个返回结果
    JSONObject result = new JSONObject();
    JsonConfig config = new JsonConfig();
    config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
    JSONArray jsonArray = JSONArray.fromObject(blogList, config);
    result.put("rows", jsonArray);
    result.put("total", total);
    ResponseUtil.write(response, result);

    return null;
  }

  @RequestMapping("/findById")
  /** 根据主键查询一条博客信息 */
  public String findById(
      @RequestParam(value = "id", required = false) String id, HttpServletResponse response)
      throws Exception {
    Blog blog = blogService.findById(Integer.parseInt(id));
    JSONObject jsonObject = JSONObject.fromObject(blog);
    ResponseUtil.write(response, jsonObject);
    return null;
  }
  /** 删除博客信息 */
  @RequestMapping("/delete")
  public String delete(
      @RequestParam(value = "ids", required = false) String ids, HttpServletResponse response)
      throws Exception {
    // 从主页获取要删除的字符串，1️⃣，分割开成为数组
    String id[] = ids.split(",");
    for (int i = 0; i < id.length; i++) {

      blogService.delete(Integer.parseInt(id[i]));
      blogIndex.deleteIndex(id[i]);
    }
    JSONObject result = new JSONObject();
    result.put("success", Boolean.valueOf(true));
    ResponseUtil.write(response, result);
    return null;
  }
}
