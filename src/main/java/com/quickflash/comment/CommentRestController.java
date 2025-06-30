package com.quickflash.comment;


import com.quickflash.comment.domain.Comment;
import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.meetingPost.service.Qualification;
import com.quickflash.meetingPost.service.Response;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentRestController {
    private final CommentService commentService;
    private final CommentBO commentBO;
    @PostMapping("/create")
    public Map<String,Object> createComment(HttpSession session,

                                            @RequestParam int postId,

                                            @RequestParam String content,
                                            @RequestParam Boolean isBeforeMeeting



    ){

        Map<String,Object> result = new HashMap<>();
        Integer sessionId = (Integer) session.getAttribute("userId");

        // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
        if(sessionId == null) {
            result.put("result", Response.FAILED.name());
            return result;
        }
        Qualification qualification =commentService.isCommentCreateOk(sessionId,postId);
        log.info("qualification + {}", qualification);
        if(!Qualification.OK.equals(qualification) ){
            result.put("result", Response.FAILED);
            return result;
        }
        try{

          commentBO.addComment(
                  sessionId,postId,content,isBeforeMeeting);
            result.put("result", Response.RESULT_OK.name());
        }catch(Exception e ){
            result.put("result", Response.FAILED.name());
        }
        return result;





    }
}
