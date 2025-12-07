package com.eeos.common.web.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

	/** 페이지를 구성하는 일정 수의 크기 */
	private int size;

	/** 데이터를 가져온 페이지 번호 */
	private int page;

	/** size 크기에 맞춰 페이징했을 때 나오는 총 페이지 개수 */
	private int totalPage;

    /** Contents */
	private List<T> contents;

}
