package com.quickflash.ComplexPage;

import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.MeetingPostDtoMaker;
import com.quickflash.meetingPost.service.MeetingPostService;
import com.quickflash.utility.calculation.CalculationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main-page")
@Slf4j
public class MainPageController {

    private final MeetingPostBO meetingPostBO;
    private final MeetingPostService meetingPostService;
    private final MeetingPostDtoMaker meetingPostDtoMaker;
    private final CalculationService calculationService;

    //localhost:8080/main-page/before-meeting
    @RequestMapping("/before-meeting")
    public String MainPageBeforeMeeting(
            HttpSession session
            , Model model
    ) {
        Integer userId = (Integer) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        String userLoginId = (String) session.getAttribute("userLoginId");
        Double lat = (Double) session.getAttribute("lat");
        Double lng = (Double) session.getAttribute("lng");

        log.info("lat {}", lat);
        log.info("lng {}", lng);

        if (lat != null && lng != null) {
            Map<Integer, MeetingPostForOrderDto> meetingPostMap = meetingPostBO.getPostIdsSelectedByBoundBox(calculationService.getLatLngForBoundBox(lat, lng, 10));
            log.info("meetingPostByBoundBox At MainPagecontroller {}", meetingPostMap);
            Set<Integer> keySet = meetingPostMap.keySet();
//            for (int key : keySet) {
//                MeetingPostForOrderDto meetingPost = meetingPostMap.get(key);
//                double distance = calculationService.calculateDistancesForMeetingPost(meetingPost.getLatitude(), meetingPost.getLongitude(), lat, lng);
//                log.info("distance Of meetingPost Id : {}  , {} 거리", key, distance);
//            }
//            log.info("CalculateScoreForMeetingPostOrder {}", meetingPostService.getPostIdsOrderByTotalScore(meetingPostMap, userId, lat, lng));


            model.addAttribute("meetingPostList", meetingPostDtoMaker.generateMeetingPostThumbnailDtoListByScore(lat,lng,userId));
        }

        //userId,userName,userLoginId
        if (userId != null && userName != null && userLoginId != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("userName", userName);
            userInfo.put("userLoginId", userLoginId);
            model.addAttribute("userInfo", userInfo);


        }
















        //반드시 start 시간과 끝나는 시간은 달라야 함.


        Random random = new Random();
        List<Map<String, Object>> currentPostList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            double sst = Math.round((random.nextDouble() * 20) * 100.0) / 100.0; // 0.00 ~ 20.00
            int start_time = random.nextInt(91); // 0 ~ 90
            int end_time = start_time + random.nextInt(11) + 1; // start_time ~ start_time + 10
            int height = random.nextInt(5); // 0 ~ 4
            int postId = i + 1; // 1 ~ 20

            Map<String, Object> post = new HashMap<>();
            post.put("sst", sst);
            post.put("start_time", start_time);
            post.put("end_time", end_time);
            post.put("height", height);
            post.put("postId", postId);

            currentPostList.add(post);
        }


//        post.put("start_time", 2);
//        post.put("end_time", 7);
//        post.put("postId", 1);
//        post.put("sst", 3.0);
//        post.put("height", 4);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 3);
//        post.put("end_time", 8);
//        post.put("postId", 2);
//        post.put("sst", 3.0);
//        post.put("height", 2);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 2);
//        post.put("end_time", 9);
//        post.put("postId", 3);
//        post.put("sst", 5.0);
//        post.put("height", 5);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 8);
//        post.put("end_time", 12);
//        post.put("postId", 4);
//        post.put("sst", 3.0);
//        post.put("height", 2);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 10);
//        post.put("end_time", 14);
//        post.put("postId", 5);
//        post.put("sst", 4.0);
//        post.put("height", 3);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 9);
//        post.put("end_time", 15);
//        post.put("postId", 6);
//        post.put("sst", 5.0);
//        post.put("height", 4);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 14);
//        post.put("end_time", 16);
//        post.put("postId", 7);
//        post.put("sst", 2.0);
//        post.put("height", 3);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 15);
//        post.put("end_time", 18);
//        post.put("postId", 8);
//        post.put("sst", 3.0);
//        post.put("height", 2);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 17);
//        post.put("end_time", 20);
//        post.put("postId", 9);
//        post.put("sst", 2.0);
//        post.put("height", 2);
//        currentPostList.add(post);
//
//        post = new HashMap<>();
//        post.put("start_time", 18);
//        post.put("end_time", 20);
//        post.put("postId", 10);
//        post.put("sst", 2.0);
//        post.put("height", 3);






        log.info("currentPostList {}", currentPostList);
        currentPostList.sort(Comparator.comparingInt(post1 -> (int) post1.get("end_time")));
        log.info("currentPostList {}", currentPostList);













      final int height_set = 10;
        List<TreeSet<Integer>> treeOfEndTimeInSameHeight = new ArrayList<>();
        for (int i = 0; i <= height_set; i++) {
            treeOfEndTimeInSameHeight.add(new TreeSet<>());
        }
        double [][] dp = new double[200][1200];
        int [][][] prev = new int[200][1200][3];
        int [][] getPostId = new int [200][1200];

        for(Map<String, Object> post1: currentPostList) {


            double sst = (double) post1.get("sst");
            int start_time = (int) post1.get("start_time");
            int end_time = (int) post1.get("end_time");
            int height = (int) post1.get("height");
            int postId = (int) post1.get("postId");


            for (int h = 0; h < height_set; h++) {
                TreeSet<Integer> treesetOfPrevHeight = treeOfEndTimeInSameHeight.get(h);
               Integer index1 = 0;
                Integer index2 = 0;
              //  log.info("treesetOfPrevHeight {}", treesetOfPrevHeight);

                //treesetSsameHeight 에서 start_time 과 가장 근접한 종료시각을 가져온다. index1에 설정
                if (treesetOfPrevHeight != null && !treesetOfPrevHeight.isEmpty()) {
                    index1 = treesetOfPrevHeight.floor(start_time);
                    if(index1 == null){
                        index1 = 0;
                    }
                }
          //
                //   treesetSameHeight 가 0 이면  index = 0

                // tresetsameheight가 없다는 것은 누적고도가 h이고 종료시각이 currentPost의 시작시각 보다 이전인 게시글이 없다는 것인데 h도 0 이 아니
                // 라면 밑의 점화식 dp 를 실행할 수 없다. h가 0일때 게시글이 없으면 첫게시글이므로 점화식 실행 가능.
                if ((index1 == 0  ) && h != 0) {
                    continue;
                }

                int total_height = h + height;
                if (total_height >= height_set) {
                    total_height = height_set;
                }
                //treeSetWhenSameHeight.get(h + height) 에서 end_time과 가장 근접한 종료시각을 가져온다.그장 treeSet의 가장 마지막 값이다! index2로 설정 마찬가지로 tree없으면 index2 는 0
                TreeSet<Integer> treesetOfTotalHeight = treeOfEndTimeInSameHeight.get(total_height);
                if (treesetOfTotalHeight != null && !treesetOfTotalHeight.isEmpty()) {
                    index2 = treesetOfTotalHeight.lower(end_time);
                    if(index2 == null){
                        index2 =  0;
                    }
                }

                double caclulated = dp[index1][h] + sst;

                // 높이가 1000 이상이면 1000으로 취급한다.

                //기존의 dp[end_time][total_height] 보다 커야 업데이트
                if ((caclulated > dp[index2][total_height]) && (caclulated > dp[end_time][total_height])) {
                    log.info("\n postId {}",postId);
                    log.info(" index1 {}", index1);
                    log.info(" h : {}",h);
                    log.info("total_height {}",total_height);
                    log.info("end_time {}",end_time);
                    dp[end_time][total_height] = dp[index1][h] + sst;
                    log.info("sst  {}" ,  dp[end_time][total_height]);
                    getPostId[end_time][total_height] = postId;





                    prev[end_time][total_height][0] = index1;
                    prev[end_time][total_height][1] = h;
                    log.info("prev[end_time][total_height][0]  {} {}" ,end_time, prev[end_time][total_height][0]);
                    log.info("prev[end_time][total_height][1] {} {}" ,total_height, prev[end_time][total_height][1]);
                    treeOfEndTimeInSameHeight.get(total_height).add(end_time);
                }
            }
        }
        //treeSetWhensameHeight.get(1000) 에서 가장 끝에 있는 end_time 가져온다.
        int end = treeOfEndTimeInSameHeight.get(height_set).last();
        log.info("end {}", end);
        List<Integer> postList = new ArrayList<>();
        int h = height_set;

        while(true){
            postList.add(getPostId[end][h]);
            log.info("postList {}" , postList);
            log.info("prev[end][h][0] {}" , prev[end][h][0]);
            log.info("prev[end][h][1] {}" , prev[end][h][1]);
            if(prev[end][h][0]  == 0  ) {
                break;
            }
            int end_before = end;
            int h_before = h;
            end = prev[end_before][h_before][0];
            h = prev[end_before][h_before][1];
            log.info("end, h {}{}" , end, h);


        }
        Collections.reverse(postList);
        log.info("postList {}", postList);


        return "main_page/beforeMeeting";


    }
}


