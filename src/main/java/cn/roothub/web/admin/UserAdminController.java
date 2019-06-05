package cn.roothub.web.admin;

import cn.roothub.dto.Result;
import cn.roothub.entity.Reply;
import cn.roothub.entity.Topic;
import cn.roothub.entity.User;
import cn.roothub.service.ReplyService;
import cn.roothub.service.TopicService;
import cn.roothub.service.UserService;
import cn.roothub.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author: miansen.wang
 * @date: 2019-05-01
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private ReplyService replyService;

    /**
     * 用户列表
     *
     * @param username
     * @param email
     * @param p
     * @param model
     * @return
     */
    @RequiresPermissions("user:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String username, String email, @RequestParam(value = "p", defaultValue = "1") Integer p, Model model) {
        if (StringUtils.isEmpty(username)) username = null;
        if (StringUtils.isEmpty(email)) email = null;
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("p", p);
        model.addAttribute("page", userService.pageForAdmin(username, email, p, 25));
        return "/admin/user/list";
    }

    /**
     * 编辑用户界面
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("user:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "/admin/user/edit";
    }

    /**
     * 编辑用户接口
     *
     * @return
     */
    @RequiresPermissions("user:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> edit(User user) {
        userService.updateAdmin(user);
        return new Result<>(true, "编辑成功");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequiresPermissions("user:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> delete(Integer id) {
        userService.deleteAdmin(id);
        return new Result<>(true, "删除成功");
    }

    /**
     * 拉黑用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/blacklist", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> blacklist(@RequestParam("id") Integer id) {
        //先改变用户禁用状态，0代表默认，1代表禁用
        User user = userService.findById(id);
        boolean flag = user.getIsBlock();//记录用户状态
        user.setIsBlock(!flag);//改变用户状态
        userService.updateUser(user);
        //根据用户昵称查找出所有相关话题
        List<Topic> topicList = topicService.findByAuthor(user.getUserName());

        //根据用户状态改变所有话题状态及其评论状态
        //如果flag为true，则用户由禁用(1)改为默认(0)，即将恢复其所有话题和相关评论
        //如果flag为false，则用户由默认(0)改为禁用(1)，即将屏蔽其所有话题和相关评论
        if (topicList.size() > 0) {
            for (int i = 0; i < topicList.size(); i++) {
                topicList.get(i).setShowStatus(flag);
                topicService.updateTopic(topicList.get(i));
                List<Reply> replyList = replyService.findByTopicId(topicList.get(i).getTopicId());
                if (replyList.size()>0){
                    changeReplyStatus(replyList,!flag);
                }
            }
        }
        //改变用户做出的评论的状态
        List<Reply> replyList2 = replyService.findByAuthorId(id);
        if (replyList2.size() > 0) {
            changeReplyStatus(replyList2,!flag);
        }
        return new Result<>(true, "更新成功");
    }
    public void changeReplyStatus(List<Reply> replyList,boolean flag){
        for (int j = 0; j < replyList.size(); j++) {
            replyList.get(j).setIsShow(flag);
            replyService.update(replyList.get(j));
        }
    }

    /**
     * 刷新Token
     *
     * @return
     */
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> refreshToken() {
        return new Result<>(true, StringUtil.getUUID());
    }

    /**
     * 局部日期转换，将 String 类型的时间数据转化为 Date 类型
     *
     * @param binder
     * @param request
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        // 转换日期 注意这里的转化要和传进来的字符串的格式一直 如2015-9-9 就应该为yyyy-MM-dd
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // CustomDateEditor为自定义日期编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
