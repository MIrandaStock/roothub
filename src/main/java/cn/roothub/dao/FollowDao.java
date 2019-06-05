package cn.roothub.dao;

import cn.roothub.entity.Follow;
import cn.roothub.entity.Topic;
import cn.roothub.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowDao {

	/**
	 * 我关注的人
	 * @param start
	 * @param limit
	 * @param uid
	 * @return
	 */
	List<User> select(@Param("start")Integer start,@Param("limit")Integer limit,@Param("uid")Integer uid);
	
	/**
	 * 关注人的主题，不包括被屏蔽的
	 * @param start
	 * @param limit
	 * @param uid
	 * @return
	 */
	List<Topic> selectTopic(@Param("start")Integer start,@Param("limit")Integer limit,@Param("uid")Integer uid);
	
	/**
	 * 关注我的人
	 * @param start
	 * @param limit
	 * @param uid
	 * @return
	 */
	List<User> selectByFid(@Param("start")Integer start,@Param("limit")Integer limit,@Param("fid")Integer fid);
	
	/**
	 * 添加关注
	 * @param follow
	 * @return
	 */
	int insert(Follow follow);
	
	/**
	 * 取消关注
	 * @param uid
	 * @param fid
	 * @return
	 */
	int delete(@Param("uid")Integer uid,@Param("fid")Integer fid);
	
	/**
	 * 统计用户关注的数量
	 * @param uid
	 * @return
	 */
	int countByUid(@Param("uid")Integer uid);
	
	/**
	 * 统计用户被关注的数量
	 * @param fid
	 * @return
	 */
	int countByFid(@Param("fid")Integer fid);
	
	/**
	 * 判断是否已关注 0：否 1：是
	 * @param uid
	 * @param fid
	 * @return
	 */
	int isFollow(@Param("uid")Integer uid,@Param("fid")Integer fid);
	
	/**
	 * 关注的人的主题的数量,不包括被屏蔽的
	 * @param uid
	 * @return
	 */
	int countTopic(@Param("uid")Integer uid);
}
