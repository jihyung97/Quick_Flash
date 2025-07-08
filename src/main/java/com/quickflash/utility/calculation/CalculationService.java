package com.quickflash.utility.calculation;

import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    // postinfo 에는  게시글의 아이디, sst값, 시작 시간, 종료 시간, 획득 고도 가 들어있다
    public List<Integer> sstOptimizeAlgorithm(List<Map<Integer,Double>> postInfoList) {


    }
}
