package com.quickflash;

import com.quickflash.meetingPost.dto.OneClickDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
@Slf4j
class QuickflashApplicationTests {
	@Test
	public List<Integer> 원클릭(List<OneClickDto> oneClickDtoList  , int height_goal ){



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













		final int height_set = height_goal;
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

		//목표한 고도에 도달하는 경우가 없으면 빈 list 반환
		if(treeOfEndTimeInSameHeight.get(height_set) == null || treeOfEndTimeInSameHeight.get(height_set).isEmpty()){
			return new ArrayList<>();
		}
		int end = treeOfEndTimeInSameHeight.get(height_set).last();
		//  log.info("end {}", end);
		List<Integer> postList = new ArrayList<>();
		int h = height_set;


		log.info("treeSet", treeOfEndTimeInSameHeight);
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
