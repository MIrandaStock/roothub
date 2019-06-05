package cn.roothub.web.admin;

import cn.roothub.config.SiteConfig;
import cn.roothub.dto.PageDataBody;
import cn.roothub.dto.Result;
import cn.roothub.entity.Reply;
import cn.roothub.entity.Topic;
import cn.roothub.exception.ApiAssert;
import cn.roothub.service.NodeService;
import cn.roothub.service.ReplyService;
import cn.roothub.service.TopicService;
import cn.roothub.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * <p></p>
 * @author: miansen.wang
 * @date: 2019-03-02
 */
@Controller
@RequestMapping("/admin/topic")
public class TopicAdminController {

	@Autowired
	private TopicService topicService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private UserService userService;
	
	/**
	 * 话题列表
	 * @param startDate
	 * @param endDate
	 * @param author
	 * @param model
	 * @param p
	 * @return
	 */
	@RequiresPermissions("topic:list")
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public String list(String startDate, String endDate,String author, Model model,@RequestParam(defaultValue = "1") Integer p) {
		if (StringUtils.isEmpty(startDate)) startDate = null;
	    if (StringUtils.isEmpty(endDate)) endDate = null;
	    if (StringUtils.isEmpty(author)) author = null;
		PageDataBody<Topic> page = topicService.pageForAdmin(author, startDate, endDate, p, 10);
		model.addAttribute("page", page);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
	    model.addAttribute("author", author);
	    return "admin/topic/list";
	}
	
	/**
	 * 置顶或者取消置顶
	 * @param id
	 * @return
	 */
	@RequiresPermissions("topic:top")
	@RequestMapping(value = "/top",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> top(@RequestParam("id") Integer id){
		Topic topic = topicService.findById(id);
		topic.setTop(!topic.getTop());
		topic.setUpdateDate(new Date());
		topicService.updateTopic(topic);
		return new Result<>(true, "更新成功！");
	}
	
	/**
	 * 加精或者取消加精
	 * @param id
	 * @return
	 */
	@RequiresPermissions("topic:good")
	@RequestMapping(value = "/good",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> good(@RequestParam("id") Integer id){
		Topic topic = topicService.findById(id);
		topic.setGood(!topic.getGood());
		topic.setUpdateDate(new Date());
		topicService.updateTopic(topic);
		return new Result<>(true, "更新成功！");
	}


	/**
	 * 屏蔽或者取消屏蔽
	 */
//	@RequiresPermissions("topic:showStatus")
	@RequestMapping(value = "/showStatus",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> showStatus(@RequestParam("id") Integer id){
		Topic topic = topicService.findById(id);
		topic.setShowStatus(!topic.getShowStatus());
		topicService.updateTopic(topic);

		//同时屏蔽相关评论
		List<Reply> replyList=replyService.findByTopicId(id);
		for(int i=0;i<replyList.size();i++){
			replyList.get(i).setIsShow(!replyList.get(i).getIsShow());
			replyService.update(replyList.get(i));
		}
		return new Result<>(true, "更新成功！");
	}


	/**
	 * 删除话题
	 * @param id
	 * @return
	 */
	@RequiresPermissions("topic:delete")
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@ResponseBody
	public Result<String> delete(@RequestParam("id") Integer id){
		if (topicService.deleteByTopicId(id) && replyService.deleteByTopicId(id)){
			return new Result<>(true, "删除成功！");
		}else{
			return new Result<>(false, "删除失败！");
		}

	}
	
	/**
	 * 编辑话题页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("topic:edit")
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam("id") Integer id,Model model) {
		model.addAttribute("topic", topicService.findById(id));
		model.addAttribute("nodes", nodeService.findAll(null, null));
		model.addAttribute("vEnter", "\n");
		return "/admin/topic/edit";
	}
	
	/**
	 * 编辑话题接口
	 * @param id
	 * @param title
	 * @param content
	 * @param nodeTitle
	 * @return
	 */
	@RequiresPermissions("topic:edit")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public Result<String> edit(@RequestParam("id") Integer id,@RequestParam("title") String title,
					  		   @RequestParam("content") String content,@RequestParam("nodeTitle") String nodeTitle){
		ApiAssert.notEmpty(title, "标题不能为空");
		ApiAssert.notEmpty(nodeTitle, "节点不能为空");
		Topic topic = topicService.findById(id);
		topic.setTitle(title);
		topic.setContent(content);
		topic.setNodeTitle(nodeTitle);
		topic.setUpdateDate(new Date());
		topicService.updateTopic(topic);
		return new Result<>(true, "编辑成功！");
	}
}
