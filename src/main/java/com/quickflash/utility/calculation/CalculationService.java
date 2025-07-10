package com.quickflash.utility.calculation;

import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import com.quickflash.meetingPost.dto.OneClickDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CalculationService {
    public Map<String, Integer> convertspeedTopace(double speed) {
        Map<String, Integer> speedMap = new HashMap<>();
        if (speed == 0) {
            speedMap.put("min", 0);
            speedMap.put("sec", 0);
            return speedMap;
        }
        double pace = 60 / speed;
        int min = (int) pace;
        int sec = (int) ((pace - min) * 60);
        speedMap.put("min", min);
        speedMap.put("sec", sec);
        return speedMap;

    }

    public double convertPaceToSpeed(int pace_min, int pace_sec) {

        if (!(pace_min == 0 && pace_sec == 0)) {
            return 3600 / (pace_min * 60 + pace_sec);
        }
        return 0;

    }

    public Map<String, Double> getLatLngForBoundBox(double standard_lat, double standard_lng, double distance) {
        final double earthRadius = 6371.0; // 지구 반지름

        // 위도 각도 차이 계산
        double deltaLat = Math.toDegrees(distance / earthRadius);

        // 경도 각도 차이 계산 (위도 보정 포함)
        double deltaLng = Math.toDegrees(distance / (earthRadius * Math.cos(Math.toRadians(standard_lat))));

        double minLat = standard_lat - deltaLat;
        double maxLat = standard_lat + deltaLat;
        double minLng = standard_lng - deltaLng;
        double maxLng = standard_lng + deltaLng;

        Map<String, Double> bounds = new HashMap<>();
        bounds.put("minLat", minLat);
        bounds.put("maxLat", maxLat);
        bounds.put("minLng", minLng);
        bounds.put("maxLng", maxLng);

        return bounds;
    }

    // boundbox 쿼리로     meetingPost의  위도, 경도 데이터만 가져오게 되면... 나중에 점수계산할때 또 meetingPost에서 파워 속도 운동종류데이터를 가져와야 한다.(불필요한 쿼리) .
    //따라서 쿼리로 dto를 통째로 가져오고 거리계산할때도 dto를 통째로 넣는다(거리계산을 위해 따로 dto에서 위도,경도를 분리하게 되면 메모리, 시간 낭비..)
    public Double calculateDistancesForMeetingPost(double lat, double lng, double standard_lat, double standard_lng) {


        final int EARTH_RADIUS_KM = 6371; // 지구 반지름 (킬로미터)


        double dLat = Math.toRadians(lat - standard_lat);
        double dLng = Math.toRadians(lng - standard_lng);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(standard_lat)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;

        log.info(" Distance +++{}  ", distance);
        return distance;
    }


    public double calculatePowerScore(double Wa, double Wb) {
        double alpha = 10.0;

        // 끄는 사람 Wb 에 따른 드래프팅 절약율 (지수 회귀 기반)
        double powerRatio = 0.4572 * Math.exp(-0.00541 * Wb) + 0.4759;

        // 분자: 1 + e^(-alpha * (1 - savingRate))
        double numerator = 1 + Math.exp(-alpha * (1 - powerRatio));

        // 분모: 1 + e^(-alpha * (Wpost / Wmine - savingRate))
        double denominator = 1 + Math.exp(-alpha * ((Wa / Wb) - powerRatio));

        log.info("calculatePowerScore {}", numerator / denominator);

        return numerator / denominator;
    }


    public double calculateSpeedScore(double Sa, double Sb) {
        double alpha = 10.0;

        // 대략 달리기는 공기저항의 영향이 거의 없으므로, 페이스의 15퍼센트 정도를 한계치로 잡는다.
        double speedRatio = 0.85;

        // 분자: 1 + e^(-alpha * (1 - savingRate))
        double numerator = 1 + Math.exp(-alpha * (1 - speedRatio));

        // 분모: 1 + e^(-alpha * (Wpost / Wmine - savingRate))
        double denominator = 1 + Math.exp(-alpha * ((Sa / Sb) - speedRatio));

        log.info("calculateSpeedScore {}", numerator/denominator);
        // 전체 Score
        return numerator / denominator;


    }

    public double calculateDistanceScore(double distance) {
        final double alpha = 5.0;
        final double default_distance = 5.0;

        double numerator = 1 + Math.exp(-default_distance * alpha);
        double denominator = 1 + Math.exp(alpha * (distance - default_distance));
        return numerator / denominator;
    }

    public double calculateTotalScore(Double powerScore,Double speedScore,Double distanceScore, Double trustOfMember) {
        double totalScore;

        log.info("total Score 에서의 power,speed,distance,trust {} {} {} {}",powerScore,speedScore,distanceScore,trustOfMember);
        //power값, speed 값 없을 때 거리와 신뢰도로만 구한다.
       if((powerScore == null || powerScore == 0.0) && (speedScore == null || speedScore == 0.0)){
           totalScore = 0.7 * distanceScore + 0.3 * trustOfMember;
       }
       else if(powerScore != null && powerScore > 0){
           totalScore = 0.3 * powerScore + 0.4 * distanceScore + 0.3 * trustOfMember;
       }else{
           totalScore = 0.3 * speedScore + 0.4 * distanceScore + 0.3 * trustOfMember;
       }
       return totalScore;
    }


    public OneClickDto calculateSstAndEndTime(double power, int duration, double ftp, int start_time){
        //sst를 구한다. t*(Np/ftp)^2 * 100 /3600
        double sst = duration * (power/ftp)*(power/ftp) * 100 / 3600; //duration 은 초단위
        //total_time 은  duration + 휴식시간 , 시간단위 이므로 Integer
        int rest_time = (int)((double)(24 /  125) * sst) * 60 / 10; // 10분단위
        log.info("rest_time {}", rest_time);

        int total_time = (int)( duration / (60 * 10)) + rest_time; // 10분단위
        int end_time = start_time + total_time; // 10분단위
        Map<String,Object> result = new HashMap<>();
         OneClickDto oneClickDto = new OneClickDto();
        oneClickDto.setEnd_time(start_time);
         oneClickDto.setEnd_time(end_time);
         oneClickDto.setSst(sst);



        return oneClickDto;

    }
    public  List<Integer> optimizeOneClick(List<OneClickDto> oneClickDtoList ){



//        List<OneClickDto> oneClickDtoList = List.of(
//                OneClickDto.builder().id(7).start_time(4).end_time(5).height(1).sst(7.88).build(),
//                OneClickDto.builder().id(5).start_time(5).end_time(9).height(1).sst(3.43).build(),
//                OneClickDto.builder().id(15).start_time(11).end_time(16).height(2).sst(17.47).build(),
//                OneClickDto.builder().id(16).start_time(8).end_time(18).height(0).sst(17.88).build(),
//                OneClickDto.builder().id(12).start_time(15).end_time(20).height(2).sst(16.54).build(),
//                OneClickDto.builder().id(6).start_time(18).end_time(23).height(3).sst(6.99).build(),
//                OneClickDto.builder().id(11).start_time(21).end_time(24).height(1).sst(5.28).build(),
//                OneClickDto.builder().id(4).start_time(24).end_time(26).height(0).sst(3.69).build(),
//                OneClickDto.builder().id(18).start_time(25).end_time(30).height(3).sst(4.96).build(),
//                OneClickDto.builder().id(10).start_time(29).end_time(36).height(1).sst(8.16).build(),
//                OneClickDto.builder().id(1).start_time(36).end_time(38).height(2).sst(1.62).build(),
//                OneClickDto.builder().id(20).start_time(28).end_time(39).height(0).sst(11.79).build(),
//                OneClickDto.builder().id(14).start_time(40).end_time(48).height(3).sst(6.02).build(),
//                OneClickDto.builder().id(8).start_time(45).end_time(50).height(1).sst(11.92).build(),
//                OneClickDto.builder().id(17).start_time(48).end_time(59).height(1).sst(2.75).build(),
//                OneClickDto.builder().id(13).start_time(59).end_time(70).height(4).sst(9.73).build(),
//                OneClickDto.builder().id(2).start_time(72).end_time(79).height(3).sst(4.75).build(),
//                OneClickDto.builder().id(19).start_time(80).end_time(83).height(2).sst(5.8).build(),
//                OneClickDto.builder().id(9).start_time(88).end_time(95).height(4).sst(6.94).build(),
//                OneClickDto.builder().id(3).start_time(90).end_time(98).height(3).sst(18.36).build()
//        );

        Random random = new Random();



//
//        List<OneClickDto> oneClickDtoList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            double sst = Math.round((random.nextDouble() * 400) * 100.0) / 100.0; // 0.00 ~ 20.00
//            int start_time = random.nextInt(1000); // 0 ~ 90
//            int end_time = start_time + random.nextInt(400) + 1; // start_time ~ start_time + 10
//            int height = random.nextInt(150); // 0 ~ 4
//            int postId = i + 1; // 1 ~ 20
//
//            OneClickDto oneClickDto = OneClickDto.builder()
//                    .start_time(start_time)
//                    .end_time(end_time)
//                    .height(height)
//                    .id(postId)
//                    .sst(sst)
//
//                    .build();
//            post.put("sst", sst);
//            post.put("start_time", start_time);
//            post.put("end_time", end_time);
//            post.put("height", height);
//            post.put("postId", postId);

//            currentPostList.add(post);
//            oneClickDtoList.add(oneClickDto  );
//        }





      //  log.info("currentPostList {}", oneClickDtoList);
        oneClickDtoList.sort(Comparator.comparingInt(oneClickDto -> oneClickDto.getEnd_time()));
        log.info("currentPostList {}", oneClickDtoList);













        final int height_set = 1000;
        List<TreeSet<Integer>> treeOfEndTimeInSameHeight = new ArrayList<>();
        for (int i = 0; i <= height_set; i++) {
            treeOfEndTimeInSameHeight.add(new TreeSet<>());
        }
        double [][] dp = new double[2000][1200];
        int [][][] prev = new int[2000][1200][3];
        int [][] getPostId = new int [2000][1200];

        for(OneClickDto oneClickDto : oneClickDtoList) {


            double sst = (double) oneClickDto.getSst();
            int start_time = (int) oneClickDto.getStart_time();
            int end_time = (int) oneClickDto.getEnd_time();
            int height = (int) oneClickDto.getHeight();
            int postId = (int) oneClickDto.getId();


            for (int h = 0; h <= height_set; h++) {
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
                //    log.info("\n postId {}",postId);
                //    log.info(" index1 {}", index1);
                 //   log.info(" h : {}",h);
                 //   log.info("total_height {}",total_height);
                 //   log.info("end_time {}",end_time);
                    dp[end_time][total_height] = dp[index1][h] + sst;
                //    log.info("sst  {}" ,  dp[end_time][total_height]);
                    getPostId[end_time][total_height] = postId;





                    prev[end_time][total_height][0] = index1;
                    prev[end_time][total_height][1] = h;
               //     log.info("prev[end_time][total_height][0]  {} {}" ,end_time, prev[end_time][total_height][0]);
             //       log.info("prev[end_time][total_height][1] {} {}" ,total_height, prev[end_time][total_height][1]);
                    treeOfEndTimeInSameHeight.get(total_height).add(end_time);
                }
            }
        }
        //treeSetWhensameHeight.get(1000) 에서 가장 끝에 있는 end_time 가져온다.
        int end = treeOfEndTimeInSameHeight.get(height_set).last();
      //  log.info("end {}", end);
        List<Integer> postList = new ArrayList<>();
        int h = height_set;

        while(true){
            postList.add(getPostId[end][h]);
//            log.info("postList {}" , postList);
//            log.info("prev[end][h][0] {}" , prev[end][h][0]);
//            log.info("prev[end][h][1] {}" , prev[end][h][1]);
            if(prev[end][h][0]  == 0  ) {
                break;
            }
            int end_before = end;
            int h_before = h;
            end = prev[end_before][h_before][0];
            h = prev[end_before][h_before][1];
//            log.info("end, h {}{}" , end, h);


        }


        Collections.reverse(postList);
        log.info("postList {}", postList);
        return postList;
    }

}
