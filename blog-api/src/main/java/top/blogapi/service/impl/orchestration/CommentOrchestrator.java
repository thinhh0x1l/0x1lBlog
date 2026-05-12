package top.blogapi.service.impl.orchestration;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.comment.CommentEditReq;
import top.blogapi.dto.request.comment.CommentQueryRequest;
import top.blogapi.dto.request.comment.CommentUpdateRequest;
import top.blogapi.dto.request.comment.SaveCommentReq;
import top.blogapi.dto.response.comment.CommentByBlogIdResponse;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.Comment;
import top.blogapi.model.entity.Guess;
import top.blogapi.model.entity.User;
import top.blogapi.model.vo.BlogIdAndTitle;
import top.blogapi.service.BlogService;
import top.blogapi.service.CommentService;
import top.blogapi.service.GuessService;
import top.blogapi.service.auth.JwtService;
import top.blogapi.service.auth.UserServiceImpl;
import top.blogapi.util.IpAddressUtils;
import top.blogapi.util.MD5Utils;
import top.blogapi.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentOrchestrator {
    CommentService commentService;
    BlogService blogService;
    GuessService guessService;

    UserServiceImpl userService;
    JwtService jwtService;
    SecureRandom secureRandom = new SecureRandom();

    public PageInfo<Comment> getListByPageAndParentCommentId(CommentQueryRequest request) {
        try(Page<Object> page1 = PageHelper.startPage(request.getPageNum(), request.getPageSize(),
                request.getSortBy() + " " + request.getSortOrder())){
            List<Comment> comments = commentService.getListByPageAndParentCommentId(request.getPage(),
                    null,request.getBlogId());
            return new PageInfo<>(comments );
        }catch (Exception ignored){

        }
        return null;
    }

    public String updateCommentPublishedById(Long id, boolean published){
        commentService.updateCommentPublishedById(id, published);
        return "Cập nhật thành công!!";
    }
    public String updateCommentNoticeById(Long id, boolean notice){
        commentService.updateCommentNoticeById(id, notice);
        return "Cập nhật thành công!!";
    }
    public String deleteCommentById(Long id){
        commentService.deleteCommentById(id);
        return "Xóa thành công!!";
    }

    public String updateComment(CommentUpdateRequest request){
        commentService.updateComment(request);
        return "Cập nhật Comment thành công!!";
    }


    public CommentByBlogIdResponse listCommentByBlogId(int pageNum, int pageSize, Long blogId, Integer page, HttpServletRequest request){
        Long guessId = guessService.getGuessIdByTokenHash(valueByCookieName("guest_token", request));

        PageInfo<CommentByBlogIdResponse.CommentNode> pageInfo =
                commentService.commentRootTrees(pageNum,pageSize, blogId,page, guessId);

        if(pageInfo.getList().isEmpty())
            return new CommentByBlogIdResponse(pageInfo);

        List<Long> rootIds = pageInfo.getList().stream().map(CommentByBlogIdResponse.CommentNode::getId).toList();
        Map<Long, List<CommentByBlogIdResponse.CommentNode>> commentChildTrees =
                commentService.commentChildTrees(rootIds, guessId);

        for(CommentByBlogIdResponse.CommentNode commentNode: pageInfo.getList()){
            List<CommentByBlogIdResponse.CommentNode> listChild = commentChildTrees.get(commentNode.getId());
            if(listChild==null ||  listChild.isEmpty()) continue;
            commentNode.setReplyComment(listChild);
        }
        return new CommentByBlogIdResponse(pageInfo);
    }

    public boolean judgeCommentEnabled(Integer page, Long blogId) {
        if (page == 0) { // blog bình thuường
            return blogService.getCommentEnabledByBlogId(blogId);
        }
        return true;
    }

    public void saveComment(SaveCommentReq req, HttpServletRequest request, HttpServletResponse response) {

        validateRequest(req);

        Comment comment = new Comment();

        String jwtToken = resolveJwt(request);

        if (jwtToken != null && jwtService.isValid(jwtToken)) {
            applyAdminComment(comment, jwtToken);
        } else {
            Guess guess = getOrCreateGetByToken(request,response);
            comment.setGuessId(guess.getId());
            applyVisitorComment(comment, req);
        }

        fillCommonFields(comment, req, request);

        commentService.saveComment(comment);
    }
    private void validateRequest(SaveCommentReq req) {

        if (req == null
                || StringUtils.isEmpty(req.getContent(),req.getEmail(),req.getNickname())
                || req.getContent().length() > 250
                || req.getNickname().length() > 15
                || (req.getWebsite() != null && req.getWebsite().length() > 100)) {

            throw new AppException(ErrorCode.INVALID_INPUT, "Dữ liệu không hợp lệ");
        }
    }
    /**
     * SameSite rất quan trọng
     * Localhost khác port
     *
     * Thường:
     *
     * .sameSite("Lax")
     *
     * đã đủ.
     *
     * Nếu frontend/backend khác domain thật
     *
     * Ví dụ:
     *
     * api.example.com
     * frontend.vercel.app
     *
     * thì cần:
     *
     * .sameSite("None")
     * .secure(true)*/

    private String valueByCookieName(String cookieName, HttpServletRequest request){
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }

    private Guess getOrCreateGetByToken(HttpServletRequest request, HttpServletResponse response){

        String guessToken = valueByCookieName("guest_token", request);

        Guess guess = null;
        if (guessToken != null){
            // check token này thì guess có tồn tại không
            guess = guessService.getGuessByTokenHash(guessToken);
        }
        if (guess == null) {

            byte[] bytes = new byte[32];
            secureRandom.nextBytes(bytes);

            guessToken = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(bytes);

            guess = guessService.addGuess(guessToken);

            ResponseCookie cookie = ResponseCookie.from("guest_token", guessToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(Duration.ofDays(365*100))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return guess;
    }

    private String resolveJwt(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        return authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
    }

    private void applyAdminComment(Comment comment, String jwtToken) {

        Claims claims = jwtService.extractClaims(jwtToken);

        User admin = (User) userService.loadUserByUsername(
                claims.getSubject()
        );

        comment.setAdminComment(true);
        comment.setAvatar(admin.getAvatar());
        comment.setWebsite("/");
        comment.setNickname(admin.getNickname());
        comment.setEmail(admin.getEmail());
        comment.setNotice(false);
    }

    private void applyVisitorComment(Comment comment, SaveCommentReq req) {

        String emailHash = MD5Utils.getMD5(req.getEmail());

        int avatarIndex =
                (emailHash.charAt(emailHash.length() - 1) % 6) + 1;

        String website = normalizeWebsite(req.getWebsite());

        comment.setAvatar(avatarIndex + ".png");
        comment.setNotice(req.isNotice());
        comment.setAdminComment(false);
        comment.setNickname(req.getNickname().trim());
        comment.setEmail(req.getEmail().trim());
        comment.setWebsite(website);
    }

    private String normalizeWebsite(String website) {
        if (website == null || website.isBlank())
            return "";
        website = website.trim();
        if (!(website.startsWith("http://") || website.startsWith("https://")))
            website = "http://" + website;
        return website;
    }

    private void fillCommonFields(
            Comment comment,
            SaveCommentReq req,
            HttpServletRequest request
    ) {

        comment.setParentCommentId(req.getParentCommentId());
        comment.setPage(req.getPage());
        comment.setBlog(
                new BlogIdAndTitle(req.getBlogId(), "")
        );

        comment.setPublished(true);
        comment.setContent(req.getContent().trim());
        comment.setCreateTime(LocalDateTime.now());

        String ip = IpAddressUtils.getIpAddress(request);

        if (!IpAddressUtils.isLocalhost(ip)) {
            comment.setIp(ip);
        }
    }

    public void editComment(CommentEditReq req){
        // phân vân có nên set lại ip cho comment được edit không
        if(StringUtils.isEmpty(req.getContent()))
            throw new AppException(ErrorCode.INVALID_INPUT, "Dữ liệu không hợp lệ");

        commentService.editComment(req.getId(), req.getContent());
    }
}
