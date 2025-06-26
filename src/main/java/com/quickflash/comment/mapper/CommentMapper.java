package com.quickflash.comment.mapper;

import com.quickflash.comment.dto.CommentDto;
import com.quickflash.meetingPost.domain.MeetingPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface CommentMapper {
  List<CommentDto> selectCommentDtoListByPostId(int postId);


}
