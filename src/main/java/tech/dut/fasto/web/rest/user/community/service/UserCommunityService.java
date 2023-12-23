package tech.dut.fasto.web.rest.user.community.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.Review;
import tech.dut.fasto.web.rest.user.community.dto.request.ReplyRequestDto;
import tech.dut.fasto.web.rest.user.community.dto.request.ReviewRequestDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReReplyResponseDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReplyResponseDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReviewResponseDto;

import java.util.List;

public interface UserCommunityService {
    ReviewResponseDto createReview(ReviewRequestDto reviewRequestDTO);

    void updateReviewImage(List<String> images, Review review);

    ReplyResponseDto createReply(ReplyRequestDto replyRequestDto, Long reviewId, Long replyId);

    void userLikeOrDislikeReview(Long reviewId);

    void userLikeOrDislikeReply(Long replyId);

    void userLikeOrDislikeReReply(Long reReplyId);

    ReviewResponseDto userUpdateReview(Long reviewId, ReviewRequestDto requestDTO);

    ReplyResponseDto userUpdateReply(Long reviewId, Long replyId, Long reReplyId, ReplyRequestDto replyRequestDTO);

    void deleteReply(Long replyId, Long reReplyId);

    void deleteReview(Long reviewId);

    Page<ReviewResponseDto> getAllTopReview(Long shopId, Pageable pageable);

    Page<ReviewResponseDto> getAllReview(Long shopId, Pageable pageable);

    Page<ReplyResponseDto> getAllTopReply(Long reviewId, Pageable pageable);

    Page<ReplyResponseDto> getAllReply(Long reviewId, Pageable pageable);

    Page<ReReplyResponseDto> getAllTopReReply(Long replyId, Pageable pageable);

    Page<ReReplyResponseDto> getAllReReply(Long replyId, Pageable pageable);

    List<String> getReviewImage(Long reviewId);
}
