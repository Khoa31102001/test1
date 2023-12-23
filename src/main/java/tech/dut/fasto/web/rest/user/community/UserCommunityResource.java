package tech.dut.fasto.web.rest.user.community;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.web.rest.user.community.dto.request.ReplyRequestDto;
import tech.dut.fasto.web.rest.user.community.dto.request.ReviewRequestDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReReplyResponseDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReplyResponseDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReviewResponseDto;
import tech.dut.fasto.web.rest.user.community.service.UserCommunityService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/management/community")
@RequiredArgsConstructor
@Validated
public class UserCommunityResource {

    private final UserCommunityService userCommunityService;


    @GetMapping(path = "/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviewByCommunity(
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(required = false) Long shopId
    ) {
        return ResponseEntity.ok(this.userCommunityService.getAllReview(shopId, pageable));
    }

    @GetMapping(path = "/top-reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getAllTopReviewByCommunity(
            @PageableDefault
            Pageable pageable,
            @RequestParam(required = false) Long shopId
    ) {
        return ResponseEntity.ok(this.userCommunityService.getAllTopReview(shopId, pageable));
    }

    @GetMapping(path = "/review-images/{reviewId}")
    public ResponseEntity<List<String>> getAllTopReviewByCommunity(
            @PathVariable(name = "reviewId") Long reviewId
    ) {
        return ResponseEntity.ok(this.userCommunityService.getReviewImage(reviewId));
    }

    @GetMapping(path = "/replies")
    public ResponseEntity<Page<ReplyResponseDto>> getAllReplyByCommunity(
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(name = "reviewId", required = false) Long reviewId
    ) {
        return ResponseEntity.ok(this.userCommunityService.getAllReply(reviewId, pageable));
    }

    @GetMapping(path = "/top-replies")
    public ResponseEntity<Page<ReplyResponseDto>> getAllTopReplyByCommunity(
            @PageableDefault
            Pageable pageable,
            @RequestParam(name = "reviewId", required = false) Long reviewId
    ) {
        return ResponseEntity.ok(this.userCommunityService.getAllTopReply(reviewId, pageable));
    }

    @GetMapping(path = "/re-replies")
    public ResponseEntity<Page<ReReplyResponseDto>> getAllReReplyByCommunity(
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(name = "replyId", required = false) Long replyId
    ) {
        return ResponseEntity.ok(this.userCommunityService.getAllReReply(replyId, pageable));
    }

    @GetMapping(path = "/top-re-replies")
    public ResponseEntity<Page<ReReplyResponseDto>> getAllTopReReplyByCommunity(
            @PageableDefault
            Pageable pageable,
            @RequestParam(name = "replyId", required = false) Long replyId
    ) {
        return ResponseEntity.ok(this.userCommunityService.getAllTopReReply(replyId, pageable));
    }

    @PostMapping(path = "/review/like/{reviewId}")
    public ResponseEntity<Void> userLikeReview(@PathVariable(name = "reviewId") Long reviewId) {
        userCommunityService.userLikeOrDislikeReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/review")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto reviewRequestDTO) {
        return ResponseEntity.ok(userCommunityService.createReview(reviewRequestDTO));
    }

    @PostMapping(path = "/reply")
    public ResponseEntity<ReplyResponseDto> createReply(@RequestParam Long reviewId,
                                                        @RequestParam(required = false) Long replyId,
                                                        @RequestBody @Valid ReplyRequestDto replyRequestDTO) {
        return ResponseEntity.ok(userCommunityService.createReply(replyRequestDTO, reviewId, replyId));
    }

    @DeleteMapping(path = "/review/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable(name = "id") Long reviewId) {
        userCommunityService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/reply")
    public ResponseEntity<Void> deleteReply(
            @RequestParam(required = false) Long replyId,
            @RequestParam(required = false) Long reReplyId) {
        userCommunityService.deleteReply(replyId, reReplyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/reply/like/{replyId}")
    public ResponseEntity<Void> userLikeReply(@PathVariable(name = "replyId") Long replyId) {
        userCommunityService.userLikeOrDislikeReply(replyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/re-reply/like/{rereplyId}")
    public ResponseEntity<Void> userLikeReReply(@PathVariable(name = "rereplyId") Long reReplyId) {
        userCommunityService.userLikeOrDislikeReReply(reReplyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/review")
    public ResponseEntity<ReviewResponseDto> userUpdateReview(@RequestParam(name = "reviewId") Long reviewId, @RequestBody ReviewRequestDto requestDTO) {
        return ResponseEntity.ok(userCommunityService.userUpdateReview(reviewId, requestDTO));
    }

    @PutMapping(path = "/reply")
    public ResponseEntity<ReplyResponseDto> userUpdateReply(@RequestParam Long reviewId,
                                                            @RequestParam(required = false) Long replyId,
                                                            @RequestParam(required = false) Long reReplyId, @RequestBody ReplyRequestDto replyRequestDTO) {
        return ResponseEntity.ok(userCommunityService.userUpdateReply(reviewId, replyId, reReplyId, replyRequestDTO));
    }
}
