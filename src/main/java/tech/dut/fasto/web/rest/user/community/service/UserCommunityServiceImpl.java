package tech.dut.fasto.web.rest.user.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.*;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.dto.NotificationDto;
import tech.dut.fasto.common.repository.*;
import tech.dut.fasto.common.service.NotificationService;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.user.community.dto.request.ReplyRequestDto;
import tech.dut.fasto.web.rest.user.community.dto.request.ReviewRequestDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReReplyResponseDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReplyResponseDto;
import tech.dut.fasto.web.rest.user.community.dto.response.ReviewResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommunityServiceImpl implements UserCommunityService {
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImagesRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final ReReplyRepository reReplyRepository;
    private final MessageService messageService;
    private final UserFavoriteReplyRepository userFavoriteReplyRepository;
    private final UserFavoriteReviewRepository userFavoriteReviewRepository;
    private final UserFavoriteReReplyRepository userFavoriteReReplyRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDTO) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        ReviewResponseDto responseDTO = new ReviewResponseDto();
        Review review = new Review();

        Shop shop = shopRepository.findById(reviewRequestDTO.getShopId()).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.favorite.shop.create.failed"),
                messageService.getMessage("error.authenticate.unauthorized.user")));
        review.setShop(shop);
        review.setUser(user);

        BeanUtils.copyProperties(reviewRequestDTO, review);
        reviewRepository.save(review);
        updateReviewImage(reviewRequestDTO.getReviewImages(), review);

        BeanUtils.copyProperties(review, responseDTO);
        responseDTO.setUserId(user.getId());
        responseDTO.setShopId(shop.getId());
        return responseDTO;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void updateReviewImage(List<String> images, Review review) {
        List<ReviewImages> reviewImages = new ArrayList<>();
        for (String image : images) {
            ReviewImages reviewImage = new ReviewImages();
            reviewImage.setReview(review);
            reviewImage.setImagesUrl(image);
            reviewImages.add(reviewImage);
        }
        reviewImageRepository.saveAll(reviewImages);
    }

    @Override
    @Transactional
    public List<String> getReviewImage(Long reviewId) {
        return reviewImageRepository.getImagesByReview(reviewId);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public ReplyResponseDto createReply(ReplyRequestDto replyRequestDto, Long reviewId, Long replyId) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Review review = reviewRepository.findByIdAndDeleteFlagIsFalse(reviewId).orElseThrow(
                () -> new FastoAlertException(messageService.getMessage("error.code.reply.create.failed"),
                        messageService.getMessage("error.review.not.found")));
        ReplyResponseDto replyResponseDto;
        if (null == replyId) {
            Reply reply = new Reply();
            reply.setReview(review);
            reply.setUser(user);
            reply.setContent(replyRequestDto.getContent());
            reply = replyRepository.saveAndFlush(reply);

            List<String> tokens = replyRepository.getAllDeviceTokenWithoutUser(user.getId(), reviewId);
            if (!user.equals(review.getUser())) {
                tokens.addAll(user.getDeviceTokenInfos().stream().map(DeviceTokenInfo::getToken).toList());
            }
            NotificationDto notificationDto = new NotificationDto();
            String comment = String.format(messageService.getMessage("title.create.reply"), user.getUserInformation().getLastName(), review.getTitle());
            notificationDto.setBody(comment);
            notificationService.sendMultipleNotification(tokens, notificationDto);
            replyResponseDto = new ReplyResponseDto();
            replyResponseDto.setId(reply.getId());
            replyResponseDto.setContent(replyRequestDto.getContent());
            replyResponseDto.setReviewId(reviewId);

        } else {
            Reply reply = replyRepository.findById(replyId).orElseThrow(
                    () -> new FastoAlertException(messageService.getMessage("error.code.re.reply.create.failed"),
                            messageService.getMessage("error.reply.not.found")));
            if (Boolean.FALSE.equals(reply.getReview().getDeleteFlag())) {
                ReReply reReply = new ReReply();
                reReply.setContent(replyRequestDto.getContent());
                reReply.setUser(user);
                reReply.setReply(reply);
                reReply.setReview(review);
                reReply = reReplyRepository.saveAndFlush(reReply);


                List<String> tokens = reReplyRepository.getAllDeviceTokenWithoutUser(user.getId(), replyId);
                if (!user.equals(reply.getUser())) {
                    tokens.addAll(user.getDeviceTokenInfos().stream().map(DeviceTokenInfo::getToken).toList());
                }
                NotificationDto notificationDto = new NotificationDto();
                String comment = String.format(messageService.getMessage("title.create.re.reply"), user.getUserInformation().getLastName(), reply.getUser().getUserInformation().getLastName());
                notificationDto.setBody(comment);
                notificationService.sendMultipleNotification(tokens, notificationDto);
                replyResponseDto = new ReplyResponseDto();
                reply.setId(replyId);
                replyResponseDto.setReReplyId(reReply.getId());
                replyResponseDto.setContent(replyRequestDto.getContent());
                replyResponseDto.setReviewId(reviewId);
            } else {
                throw new FastoAlertException(messageService.getMessage("error.code.favorite.shop.create.failed"),
                        messageService.getMessage("error.reply.not.found"));
            }

        }
        return replyResponseDto;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void userLikeOrDislikeReview(Long reviewId) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.like.or.dislike.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.like.or.dislike.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Review review = reviewRepository.findById(reviewId.longValue()).orElseThrow(
                () -> new FastoAlertException(messageService.getMessage("error.code.review.like.or.dislike.failed"),
                        messageService.getMessage("error.authenticate.unauthorized.user"))
        );
        Optional<UserFavoriteReview> oUserFavoriteReview = userFavoriteReviewRepository.findByUserIdAndReviewId(user.getId(), reviewId.longValue());
        UserFavoriteReview userFavoriteReview;
        if (oUserFavoriteReview.isPresent()) {
            UserFavoriteReview userFavoriteReview1 = oUserFavoriteReview.get();
            userFavoriteReviewRepository.delete(userFavoriteReview1);

        } else {
            userFavoriteReview = new UserFavoriteReview();
            userFavoriteReview.setReview(review);
            userFavoriteReview.setUser(user);
            userFavoriteReviewRepository.saveAndFlush(userFavoriteReview);

            List<String> tokens = userFavoriteReviewRepository.getAllDeviceTokenWithoutUser(user.getId(), reviewId);
            if (!user.equals(review.getUser())) {
                tokens.addAll(user.getDeviceTokenInfos().stream().map(DeviceTokenInfo::getToken).toList());
            }
            NotificationDto notificationDto = new NotificationDto();
            String comment = String.format(messageService.getMessage("title.like.or.dislike.review"), user.getUserInformation().getLastName(), review.getTitle());
            notificationDto.setBody(comment);

            notificationService.sendMultipleNotification(tokens, notificationDto);
        }

    }

    @Override
    @Transactional
    public ReviewResponseDto userUpdateReview(Long reviewId, ReviewRequestDto requestDTO) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.update.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.update.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Long userId = user.getId();
        Review review = reviewRepository.findByIdAndDeleteFlagIsFalse(reviewId)
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.update.failed"), messageService.getMessage("error.review.not.found")));

        ReviewResponseDto reviewResponseDto;
        if (review.getUser().getId().equals(userId)) {
            Shop shop = shopRepository.findByIdAndUserStatus(requestDTO.getShopId(), UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.update.failed"), messageService.getMessage("error.shop.not.found")));
            review.setShop(shop);
            review.setContent(requestDTO.getContent());
            review.setTitle(requestDTO.getTitle());
            reviewRepository.save(review);
            reviewResponseDto = new ReviewResponseDto();
            reviewResponseDto.setId(reviewId);
            reviewResponseDto.setContent(requestDTO.getContent());
            reviewResponseDto.setTitle(requestDTO.getTitle());
            reviewResponseDto.setShopId(requestDTO.getShopId());
            reviewImageRepository.deleteByReviewId(review.getId());
            updateReviewImage(requestDTO.getReviewImages(), review);
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.review.update.failed"),
                    messageService.getMessage("error.review.not.found"));
        }
        return reviewResponseDto;
    }

    @Override
    @Transactional
    public ReplyResponseDto userUpdateReply(Long reviewId, Long replyId, Long reReplyId, ReplyRequestDto replyRequestDTO) throws FastoAlertException {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.update.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.update.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Long userId = user.getId();
        Review review = reviewRepository.findByIdAndDeleteFlagIsFalse(reviewId)
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.update.failed"), messageService.getMessage("error.review.not.found")));

        ReplyResponseDto replyResponseDto = new ReplyResponseDto();
        if (reReplyId == null) {
            Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.update.failed"), messageService.getMessage("error.review.not.found")));
            if (reply.getUser().getId().equals(userId)) {
                reply.setContent(replyRequestDTO.getContent());
                replyRepository.save(reply);
                replyResponseDto.setId(reply.getId());
                replyResponseDto.setContent(replyResponseDto.getContent());
                replyResponseDto.setReviewId(reviewId);
            } else {
                throw new FastoAlertException(messageService.getMessage("error.code.reply.update.failed"),
                        messageService.getMessage("error.reply.not.found"));
            }
        } else {
            ReReply reReply = reReplyRepository.findById(reReplyId).orElseThrow(
                    () ->
                            new FastoAlertException(messageService.getMessage("error.code.reply.update.failed"),
                                    messageService.getMessage("error.re.reply.not.found")));
            if (reReply.getUser().getId().equals(userId)) {
                reReply.setContent(replyRequestDTO.getContent());
                reReplyRepository.save(reReply);
                replyResponseDto.setId(replyId);
                replyResponseDto.setReReplyId(reReply.getId());
                replyResponseDto.setContent(replyResponseDto.getContent());
                replyResponseDto.setReviewId(reviewId);
            } else {
                throw new FastoAlertException(messageService.getMessage("error.code.reply.update.failed"),
                        messageService.getMessage("error.reply.not.found"));
            }

        }
        return replyResponseDto;
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId, Long reReplyId) {
        Reply reply = replyRepository.findByIdAndReviewDeleteFlagIsFalse(replyId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.delete.failed"), messageService.getMessage("error.reply.not.found")));

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Long userId = user.getId();

        if (reReplyId == null) {
            if (reply.getUser().getId().equals(userId)) {
                replyRepository.delete(reply);
            } else {
                throw new FastoAlertException(messageService.getMessage("error.code.reply.delete.failed"),
                        messageService.getMessage("error.reply.not.found"));
            }
        } else {
            ReReply reReply = reReplyRepository.findByIdAndReviewDeleteFlagIsFalse(reReplyId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.delete.failed"), messageService.getMessage("error.review.not.found")));
            if (reReply.getUser().getId().equals(userId)) {
                reReplyRepository.delete(reReply);
            } else {
                throw new FastoAlertException(messageService.getMessage("error.code.reply.delete.failed"),
                        messageService.getMessage("error.re.reply.not.found"));
            }
        }
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.delete.failed"), messageService.getMessage("error.review.not.found")));
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Long userId = user.getId();
        if (review.getUser().getId().equals(userId)) {
//            userFavoriteReviewRepository.updateStatusByReviewId(reviewId, true);
            review.setDeleteFlag(true);
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.review.delete.failed"),
                    messageService.getMessage("error.review.not.found"));
        }
    }

    @Override
    @Transactional
    public Page<ReviewResponseDto> getAllTopReview(Long shopId, Pageable pageable) {
        return reviewRepository.getAllTopReviewByShopId(shopId, pageable);
    }

    @Override
    @Transactional
    public Page<ReviewResponseDto> getAllReview(Long shopId, Pageable pageable) {

        Page<ReviewResponseDto> reviewResponseDtos = reviewRepository.getAllReview(shopId, pageable);
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.review.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        for(ReviewResponseDto item : reviewResponseDtos){
            if(userFavoriteReviewRepository.existsByReviewIdAndUserId(item.getId(), user.getId())){
                item.setIsFavorite(true);
            }else{
                item.setIsFavorite(false);
            }
        }
        return reviewResponseDtos;
    }

    @Override
    @Transactional
    public Page<ReplyResponseDto> getAllTopReply(Long reviewId, Pageable pageable) {
        return replyRepository.getAllTopReplyByReviewId(reviewId, pageable);
    }

    @Override
    @Transactional
    public Page<ReplyResponseDto> getAllReply(Long reviewId, Pageable pageable) {
        return replyRepository.getAllReply(reviewId, pageable);
    }

    @Override
    @Transactional
    public Page<ReReplyResponseDto> getAllTopReReply(Long replyId, Pageable pageable) {
        return reReplyRepository.getAllTopReReplyByReplyId(replyId, pageable);
    }

    @Override
    @Transactional
    public Page<ReReplyResponseDto> getAllReReply(Long replyId, Pageable pageable) {
        return reReplyRepository.getAllReReply(replyId, pageable);
    }

    @Override
    @Transactional
    public void userLikeOrDislikeReply(Long replyId) throws FastoAlertException {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.like.or.dislike.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.reply.like.or.dislike.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Reply reply = replyRepository.findByIdAndReviewDeleteFlagIsFalse(replyId).orElseThrow(
                () ->
                        new FastoAlertException(messageService.getMessage("error.code.reply.like.or.dislike.failed"),
                                messageService.getMessage("error.reply.not.found")));
        Optional<UserFavoriteReply> optionalUserFavoriteReply = userFavoriteReplyRepository.findByUserIdAndReplyId(user.getId(), reply.getId());
        if (optionalUserFavoriteReply.isEmpty()) {
            UserFavoriteReply userFavoriteReply = new UserFavoriteReply();
            userFavoriteReply.setUser(user);
            userFavoriteReply.setReply(reply);
            userFavoriteReplyRepository.save(userFavoriteReply);
            List<String> tokens = replyRepository.getAllDeviceTokenWithoutUser(user.getId(), replyId);
            if (!user.equals(reply.getUser())) {
                tokens.addAll(user.getDeviceTokenInfos().stream().map(DeviceTokenInfo::getToken).toList());
            }
            NotificationDto notificationDto = new NotificationDto();
            String comment = String.format(messageService.getMessage("title.like.or.dislike.reply"), user.getUserInformation().getLastName(), reply.getUser().getUserInformation().getLastName());
            notificationDto.setBody(comment);
            notificationService.sendMultipleNotification(tokens, notificationDto);
        } else {
            UserFavoriteReply userFavoriteReply = optionalUserFavoriteReply.get();
            userFavoriteReplyRepository.delete(userFavoriteReply);
        }

    }

    @Override
    @Transactional
    public void userLikeOrDislikeReReply(Long reReplyId) throws FastoAlertException {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.re.reply.like.or.dislike.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.re.reply.like.or.dislike.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        ReReply reReply = reReplyRepository.findByIdAndReviewDeleteFlagIsFalse(reReplyId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.re.reply.like.or.dislike.failed"), messageService.getMessage("error.re.reply.not.found")));

        Optional<UserFavoriteReReply> optionalUserFavoriteReReply = userFavoriteReReplyRepository.findByUserIdAndReReplyId(user.getId(), reReply.getId());
        if (optionalUserFavoriteReReply.isEmpty()) {
            UserFavoriteReReply userFavoriteReReply = new UserFavoriteReReply();
            userFavoriteReReply.setUser(user);
            userFavoriteReReply.setReReply(reReply);
            userFavoriteReReplyRepository.saveAndFlush(userFavoriteReReply);
            List<String> tokens = reReplyRepository.getAllDeviceTokenWithoutUser(user.getId(), reReply.getReply().getId());
            if (!user.equals(reReply.getUser())) {
                tokens.addAll(user.getDeviceTokenInfos().stream().map(DeviceTokenInfo::getToken).toList());
            }
            NotificationDto notificationDto = new NotificationDto();
            String comment = String.format(messageService.getMessage("title.like.or.dislike.reply"), user.getUserInformation().getLastName(), reReply.getReply().getUser().getUserInformation().getLastName());
            notificationDto.setBody(comment);
            notificationService.sendMultipleNotification(tokens, notificationDto);
        } else {
            userFavoriteReReplyRepository.delete(optionalUserFavoriteReReply.get());
        }

    }
}
