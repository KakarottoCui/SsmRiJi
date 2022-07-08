package com.blog.controller;

import com.blog.entity.Blog;
import com.blog.entity.Comment;
import com.blog.service.BlogService;
import com.blog.service.CommentService;
import com.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** 前台用户提交评论 */
@Controller
@RequestMapping("/comment")
public class CommentController {
  @Resource private CommentService commentService;
  @Resource private BlogService blogService;

  @RequestMapping({"/save"})
  public String save(
      Comment comment,
      @RequestParam("imageCode") String imageCode,
      HttpServletRequest request,
      HttpServletResponse response,
      HttpSession session)
      throws Exception {
    // 从session获取到验证码
    String sRand = (String) session.getAttribute("sRand");
    JSONObject result = new JSONObject();
    int resultTotal = 0;
    if (!imageCode.equals(sRand)) {
      result.put("success", Boolean.FALSE);
      result.put("errorInfo", "验证码填写错误");
    } else {
      String userIp = request.getRemoteUser(); // 用户地址
      comment.setUserIp(userIp);
      if (comment.getId() == null) {
        resultTotal = commentService.add(comment);
        // 给对应的博客留言记录数加一
        Blog blog = blogService.findById(comment.getBlog().getId());
        blog.setReplyHit(blog.getReplyHit() + 1);
        blogService.update(blog);
      }
    }
    if (resultTotal > 0) {
      result.put("success", Boolean.TRUE);
    }
    ResponseUtil.write(response, result);
    return null;
  }
}
