package com.cineevent.reviewservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MovieReviewResponseDTO extends ReviewResponseDTO{

	private String movieId;

}
