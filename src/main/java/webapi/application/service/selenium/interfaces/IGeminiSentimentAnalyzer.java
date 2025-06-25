package webapi.application.service.selenium.interfaces;

import webapi.domain.FacebookComment;
import webapi.domain.FacebookPost;

public interface IGeminiSentimentAnalyzer {
    void analyzeCommentFacebookSentiment(FacebookComment comment);
    void analyzeAllCommentsInBatch(FacebookPost post);
}
