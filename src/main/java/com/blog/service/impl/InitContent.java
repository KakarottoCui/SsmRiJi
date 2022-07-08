package com.blog.service.impl;

import com.blog.entity.Blog;
import com.blog.entity.BlogType;
import com.blog.entity.Blogger;
import com.blog.entity.Link;
import com.blog.service.BlogService;
import com.blog.service.BlogTypeService;
import com.blog.service.BloggerService;
import com.blog.service.LinkService;
import com.blog.util.Const;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

@Component
public class InitContent implements ServletContextListener, ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext application = sce.getServletContext();
    // 博客类别
    BlogTypeService blogTypeService =
        (BlogTypeService) applicationContext.getBean("blogTypeService");
    List<BlogType> blogTypeListSelect = blogTypeService.countList();
    // 我真的会谢谢 无参数查询不是显示这错误就是那错误 不知道为什么(mysql版本的问题) 现在直接搞一个参数查询 不传参数就好了 啊哈哈哈
    // 但是这样的话无法查询该类别里面的博客
    application.setAttribute(Const.BLOG_TYPE_LIST_SELECT, blogTypeListSelect);

    // 博主信息
    BloggerService bloggerService = (BloggerService) applicationContext.getBean("bloggerService");
    Blogger blogger = bloggerService.find();
    application.setAttribute(Const.BLOGGER, blogger);
    // 按年月分类的博客数量
    BlogService blogService = (BlogService) applicationContext.getBean("blogService");
    List<Blog> blogCountList = blogService.countList();
    application.setAttribute(Const.BLOG_COUNT_LIST, blogCountList);

    // 友情链接
    LinkService linkService = (LinkService) applicationContext.getBean("linkService");
    // 这里的list传null也可以
    List<Link> linkList = linkService.list(null);
    application.setAttribute(Const.LINK_LIST, linkList);
    // 这里写完之后记得去后台，刷新系统那里再写 以此来更新这些信息
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // TODO Auto-generated method stub

  }
}
