package com.blog.controller.admin;

import com.blog.entity.Comment;
import com.blog.entity.PageBean;
import com.blog.service.CommentService;
import com.blog.util.DateJsonValueProcessor;
import com.blog.util.ResponseUtil;
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
@RequestMapping({"admin/comment"})
public class CommentAdminController {
  @Resource private CommentService commentService;

  @RequestMapping({"list"})
  public String list(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "rows", required = false) String rows,
      @RequestParam(value = "state", required = false) String state,
      Comment comment,
      HttpServletResponse response)
      throws Exception {
    // 分页
    PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
    Map<String, Object> map = new HashMap<>();
    // 加载翻页，状态参数
    map.put("start", pageBean.getStart());
    map.put("size", pageBean.getPageSize());
    // 审核状态
    map.put("state", state);
    // 查询评论列表
    List<Comment> commentList = commentService.list(map);
    // 查询评论总数
    Long total = commentService.getTotal(map);
    // 放入json
    JSONObject result = new JSONObject(); //  json对象，就是一个键对应一个值，使用的是大括号{ }，如：{key:value}
    JsonConfig config = new JsonConfig(); // json数组，使用中括号[ ],只不过数组里面的项也是json键值对格式的
    config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyy-MM-dd"));
    JSONArray jsonArray = JSONArray.fromObject(commentList, config);
    result.put("rows", jsonArray);
    result.put("total", total);
    ResponseUtil.write(response, result);
    return null;
  }
  /** 删除一条评论 */
  @RequestMapping({"/delete"})
  public String delete(@RequestParam("ids") String ids, HttpServletResponse response)
      throws Exception {
    // 把传过来的字符串 转换为数组
    String[] id = ids.split(",");
    for (int i = 0; i < id.length; i++) {
      commentService.delete(Integer.parseInt(id[i]));
    }
    JSONObject result = new JSONObject();
    result.put("success", Boolean.TRUE);
    ResponseUtil.write(response, result);
    return null;
  }
  /** 审核评论 */
  @RequestMapping({"/review"})
  public String review(
      @RequestParam("ids") String ids,
      @RequestParam("state") String state,
      HttpServletResponse response)
      throws Exception {
    String[] idsStr = ids.split(",");
    for (int i = 0; i < idsStr.length; i++) {
      Comment comment = new Comment();
      comment.setId(Integer.parseInt(idsStr[i]));
      comment.setState(Integer.parseInt(state));
      commentService.update(comment);
    }
    JSONObject result = new JSONObject();
    result.put("success", Boolean.TRUE);
    ResponseUtil.write(response, result);
    return null;
  }
}
