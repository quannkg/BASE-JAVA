package webapi.application.service.selenium.interfaces;

import webapi.application.service.selenium.dto.request.FacebookCommentRequest;
import webapi.application.service.selenium.dto.request.FacebookPostRequest;
import webapi.application.service.selenium.dto.response.FacebookCommentDto;
import webapi.application.service.selenium.dto.response.FacebookCommentResponse;
import webapi.application.service.selenium.dto.response.FacebookPostDto;
import webapi.application.service.selenium.dto.response.FacebookPostResponse;

public interface IFacebookCrawler {
    void crawlPostContent(String postUrl);
    FacebookPostResponse searchPosts(FacebookPostRequest request);
    FacebookPostDto getPostById(Long postId);
    FacebookCommentResponse searchComments(FacebookCommentRequest request);
    FacebookCommentDto getCommentById(Long postId);
}
